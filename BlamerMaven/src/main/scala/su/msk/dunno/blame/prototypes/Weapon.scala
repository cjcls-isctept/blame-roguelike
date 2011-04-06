package su.msk.dunno.blame.prototypes

import su.msk.dunno.blame.field.FieldObject
import su.msk.dunno.scage.screens.ScageScreen
import su.msk.dunno.scage.screens.prototypes.ScageRender
import su.msk.dunno.scage.single.support.messages.ScageMessage._
import su.msk.dunno.scage.screens.handlers.Renderer
import org.lwjgl.input.Keyboard
import su.msk.dunno.scage.screens.support.ScageLibrary._
import su.msk.dunno.scage.single.support.Vec
import su.msk.dunno.blame.support.MyFont._
import su.msk.dunno.scage.screens.support.tracer.{PointTracer, State}
import org.lwjgl.opengl.GL11
import su.msk.dunno.blame.support.BottomMessages
import su.msk.dunno.blame.items._

private class RestrictedPlace extends Item(
  name = xml("item.restricted.name"),
  description = "The place in weapon that is restricted to use",
  symbol = BULLET,
  color = DARK_GRAY
) {
  setStat("restricted")
}

private class FreeSocket extends Item(
  name = xml("item.freesocket.name"),
  description = "The socket of the weapon that can be used to insert some imp",
  symbol = BULLET,
  color = GRAY
) {
  setStat("socket")
}

private class BasePart extends Item(
  name = xml("item.basepart.name"),
  description = "The unremovable part of the weapon",
  symbol = BULLET,
  color = WHITE
) {
  setStat("base")
}

class Weapon(val owner:Living) extends PointTracer[FieldObject] (
  field_from_x    = property("weapon.from.x", 0),
  field_to_x      = property("weapon.to.x",   800),
  field_from_y    = property("weapon.from.y", 0),
  field_to_y      = property("weapon.to.y",   600),
  N_x             = property("weapon.N_x",    16),
  N_y             = property("weapon.N_y",    12),
  are_solid_edges = true
) {
  private def removeAllTracesFromPoint(point:Vec) {
    if(isPointOnArea(point)) {
      coord_matrix(point.ix)(point.iy) = Nil
      free_sockets = free_sockets.filterNot(_ == point)
    }
  }

  private var free_sockets:List[Vec] = Nil
  override def addPointTrace(fo:FieldObject) = {
    println("incoming object "+fo+" to point "+fo.getPoint+"; free_sockets size="+free_sockets.size)
    var tmp = free_sockets.size
    if(fo.getState.contains("socket")) {
      free_sockets = fo.getPoint :: free_sockets
    }
    else free_sockets = free_sockets.filterNot(_ == fo.getPoint)
    println("object received; free_sockets size="+free_sockets.size)
    if(free_sockets.size == tmp) {
      println("HURR DURR!!!")
    }
    super.addPointTrace(fo)
  }
  override def removeTraceFromPoint(trace_id:Int, point:Vec) {
    coord_matrix(point.ix)(point.iy).find(_.id == trace_id) match {
      case Some(fo) => if(fo.getState.contains("socket")) {
        free_sockets = free_sockets.filterNot(_ == point)
      }
      case _ =>
    }
    super.removeTraceFromPoint(trace_id, point)
  }

  private var tmp = 0
  private def randomFreeSocket = {
    if(free_sockets.isEmpty) None
    else {
      val position = (math.random*free_sockets.length).toInt
      val free_point = free_sockets(position)
      if(!free_sockets.contains(free_point)) {
        println("HURR DURR!!")
      }
      Some(free_point)
    }
  }
  private def fillWeapon(num:Int) {
    println("started to add modifiers; free_sockets size="+free_sockets.size)
    for {
      i <- 0 until num
      free_socket_point = randomFreeSocket
    } {
      free_socket_point match {
        case Some(point) => {
          (math.random*4).toInt match {
            case 0 => addToWeapon(new DamageItem,     point)
            case 1 => addToWeapon(new EnergyItem,     point)
            case 2 => addToWeapon(new ShieldItem,     point)
            case 3 => addToWeapon(new SocketExtender, point)
          }
          println("added "+point+"; fres_sockets size="+free_sockets.size)
        }
        case None =>
      }
    }
  }

  {
    val points = List(Vec(-5,0), Vec(-4,0), Vec(-3,0), Vec(-2,0), Vec(-1,0), Vec(0,0), Vec(1,0), Vec(2,0), Vec(3,0), Vec(4,0), Vec(5,0), Vec(6,0)) :::
                 List(Vec(-4,-1), Vec(-3,-1), Vec(-2,-1), Vec(-1,-1), Vec(0,-1)) :::
                 List(Vec(-4,-2), Vec(-3,-2)) :::
                 List(Vec(-4,-3), Vec(-3,-3))
    for(point <- points) addBasePart(point + Vec(N_x/2, N_y/2))

    val restricted_points = List(Vec(-5,-1), Vec(-5,-2), Vec(-5,-3), Vec(-2, -2), Vec(-2,-3))
    for {
      point <- restricted_points
      restricted_point = point + Vec(N_x/2, N_y/2)
    } {
      removeAllTracesFromPoint(restricted_point)
      addTrace({
        val rp = new RestrictedPlace
        rp.changeState(new State("point", restricted_point))
        rp
      })
    }
    fillWeapon(90)
  }

  private def addBasePart(point:Vec) {
    removeAllTracesFromPoint(point)
    addTrace({
      val fs = new BasePart
      fs.changeState(new State("point", point))
      fs
    })
    addSockets(point)
  }

  private def addSockets(point:Vec) {
    val points = List(Vec(-1,0)+point, Vec(1,0)+point, Vec(0,-1)+point, Vec(0,1)+point)
    for {
      cur_point <- points
      if isPointOnArea(cur_point)
      objects_at_point = coord_matrix(cur_point.ix)(cur_point.iy)
      if objects_at_point.isEmpty
    } {
      addTrace({
        val fs = new FreeSocket
        fs.changeState(new State("point", cur_point))
        fs
      })
    }
  }

  private def removeSockets(point:Vec) {
    val points = List(Vec(-1,0)+point, Vec(1,0)+point, Vec(0,-1)+point, Vec(0,1)+point)
    for {
      cur_point <- points
      if isPointOnArea(cur_point) && isNoBasePartConnection(cur_point)
      item <- coord_matrix(cur_point.ix)(cur_point.iy).filterNot(_.getState.contains("restricted"))
    } {
      removeTraceFromPoint(item.id, item.getPoint)
      if(item.getState.contains("extender")) removeSockets(item.getPoint)
    }
  }

  private def isNoBasePartConnection(point:Vec):Boolean = {
    def _isNoExtenderOrBasePartNear(point:Vec, excluded:List[FieldObject]):Boolean = {
      val point1 = checkPointEdges(point + Vec(-1,0))
      val point2 = checkPointEdges(point + Vec(1,0))
      val point3 = checkPointEdges(point + Vec(0,-1))
      val point4 = checkPointEdges(point + Vec(0,1))

      val items:List[FieldObject] = coord_matrix(point1.ix)(point1.iy) :::
                                    coord_matrix(point2.ix)(point2.iy) :::
                                    coord_matrix(point3.ix)(point3.iy) :::
                                    coord_matrix(point4.ix)(point4.iy)
      items.find(_.getState.contains("base")) match {
        case Some(item) => false
        case None => {
          items.filter(item => item.getState.contains("extender") && !excluded.contains(item)).foldLeft(true)((result, extender) => {
            result && _isNoExtenderOrBasePartNear(extender.getPoint, extender :: excluded)
          })
        }
      }
    }
    _isNoExtenderOrBasePartNear(point, Nil)
  }

  private def removeFromWeapon(item:FieldObject, cursor:Vec) {
    removeTraceFromPoint(item.id, cursor)
    val state = item.getState
    for {
      key <- state.keys
      if state.getState(key).contains("effect")
    } owner.changeStat(key, -state.getState(key).getFloat("effect"))
    owner.checkMax
    if(state.contains("extender")) removeSockets(item.getPoint)
  }

  private def addToWeapon(item:FieldObject, cursor:Vec) {
     addPointTrace({
       item.changeState(new State("point", cursor))
       item
     })
     val state = item.getState
     for {
       key <- state.keys
       if state.getState(key).contains("effect")
     } owner.changeStat(key, state.getState(key).getFloat("effect"))
     if(state.contains("extender")) addSockets(item.getPoint)
  }

  private lazy val weapon_screen = new ScageScreen("Weapon Screen") {
    private var cursor = new Vec(N_x/2, N_y/2)
    private var is_show_cursor = false
    private def moveCursor(delta:Vec) {
      is_show_cursor = true
      val new_point = cursor + delta
      if(isPointOnArea(new_point)) cursor is new_point
    }

    center = Vec((field_to_x - field_from_x)/2,
                 (field_to_y - field_from_y)/2)
    addRender(new ScageRender {
      override def render {
        for {
          x <- 0 until N_x
          y <- 0 until N_y
          coord = pointCenter(x, y)
        } {
          if(coord_matrix(x)(y).length > 0) {
            if(is_show_cursor ||
               (!coord_matrix(x)(y).head.getState.contains("socket") &&
                !coord_matrix(x)(y).head.getState.contains("restricted"))) {
              GL11.glDisable(GL11.GL_TEXTURE_2D);
              GL11.glPushMatrix();
              Renderer.color = coord_matrix(x)(y).head.getColor
              GL11.glTranslatef(coord.x, coord.y, 0.0f);
              GL11.glRectf(-h_x/2+1, -h_y/2+1, h_x/2-1, h_y/2-1);
              GL11.glPopMatrix();
              GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
          }
          if(is_show_cursor && cursor == Vec(x,y)) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPushMatrix();
            Renderer.color = GREEN
            GL11.glTranslatef(coord.x, coord.y, 0.0f);
            GL11.glBegin(GL11.GL_LINE_LOOP)
              GL11.glVertex2f(-h_x/2, -h_y/2)
              GL11.glVertex2f(-h_x/2, h_y/2)
              GL11.glVertex2f(h_x/2, h_y/2)
              GL11.glVertex2f(h_x/2, -h_y/2)
            GL11.glEnd()
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
          }
        }
      }

      override def interface {
        print(xml("weapon.ownership", owner.stat("name")), 10, Renderer.height-20)
        if(is_show_cursor) {
          if(!coord_matrix(cursor.ix)(cursor.iy).isEmpty)
              print(coord_matrix(cursor.ix)(cursor.iy).head.getState.getString("name"),
                    10, BottomMessages.bottom_messages_height - row_height)
        }
        print(xml("weapon.helpmessage"), 10, row_height*2, GREEN)
      }
    })

    keyListener(Keyboard.KEY_UP,    100, onKeyDown = moveCursor(Vec(0,1)))
    keyListener(Keyboard.KEY_DOWN,  100, onKeyDown = moveCursor(Vec(0,-1)))
    keyListener(Keyboard.KEY_RIGHT, 100, onKeyDown = moveCursor(Vec(1,0)))
    keyListener(Keyboard.KEY_LEFT,  100, onKeyDown = moveCursor(Vec(-1,0)))

    keyListener(Keyboard.KEY_NUMPAD9, 100, onKeyDown = moveCursor(Vec(1,1)))
    keyListener(Keyboard.KEY_NUMPAD8, 100, onKeyDown = moveCursor(Vec(0,1)))
    keyListener(Keyboard.KEY_NUMPAD7, 100, onKeyDown = moveCursor(Vec(-1,1)))
    keyListener(Keyboard.KEY_NUMPAD6, 100, onKeyDown = moveCursor(Vec(1,0)))
    keyListener(Keyboard.KEY_NUMPAD4, 100, onKeyDown = moveCursor(Vec(-1,0)))
    keyListener(Keyboard.KEY_NUMPAD3, 100, onKeyDown = moveCursor(Vec(1,-1)))
    keyListener(Keyboard.KEY_NUMPAD2, 100, onKeyDown = moveCursor(Vec(0,-1)))
    keyListener(Keyboard.KEY_NUMPAD1, 100, onKeyDown = moveCursor(Vec(-1,-1)))

    keyListener(Keyboard.KEY_RETURN, onKeyDown = {
      val objects_at_cursor = coord_matrix(cursor.ix)(cursor.iy)
      if(objects_at_cursor.exists(item => item.getState.contains("socket"))) {
        objects_at_cursor.find(!_.getState.contains("socket")) match {
          case Some(item) => {
            removeFromWeapon(item, cursor)
            owner.inventory.addItem(item)
          }
          case None => {
            owner.inventory.selectItem(xml("weapon.selectmodifier"), fo => fo.getState.contains("modifier")) match {
              case Some(item) => {
                owner.inventory.removeItem(item)
                addToWeapon(item, cursor)
              }
              case None =>
            }
          }
        }
      }
    })
    keyListener(Keyboard.KEY_ESCAPE, onKeyDown = {
      if(is_show_cursor) is_show_cursor = false
      else stop
    })
  }

  def showWeapon() {
    weapon_screen.run
  }
}