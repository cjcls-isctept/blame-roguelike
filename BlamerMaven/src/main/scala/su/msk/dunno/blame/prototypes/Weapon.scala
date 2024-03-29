package su.msk.dunno.blame.prototypes

import su.msk.dunno.blame.field.FieldObject
import su.msk.dunno.scage.screens.ScageScreen
import su.msk.dunno.scage.single.support.messages.ScageMessage._
import su.msk.dunno.scage.screens.handlers.Renderer._
import org.lwjgl.input.Keyboard._
import su.msk.dunno.scage.single.support.Vec
import su.msk.dunno.scage.single.support.ScageColors._
import su.msk.dunno.scage.single.support.ScageProperties._
import su.msk.dunno.blame.support.MyFont._
import org.lwjgl.opengl.GL11
import su.msk.dunno.blame.support.BottomMessages
import su.msk.dunno.blame.items._
import su.msk.dunno.scage.screens.support.tracer.{ScageTracer, State}

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

class Weapon(val owner:Living) extends ScageTracer[FieldObject] (
  field_from_x    = property("weapon.from.x", 0),
  field_to_x      = property("weapon.to.x",   800),
  field_from_y    = property("weapon.from.y", 0),
  field_to_y      = property("weapon.to.y",   600),
  init_N_x        = property("weapon.N_x",    16),
  init_N_y        = property("weapon.N_y",    12),
  are_solid_edges = true
) {
  private def removeAllTracesFromPoint(point:Vec) {
    if(isPointOnArea(point)) {
      point_matrix(point.ix)(point.iy) = Nil
      free_sockets = free_sockets.filterNot(_ == point)
    }
  }

  private var free_sockets:List[Vec] = Nil
  override def addTrace(point:Vec, fo:FieldObject) = {
    if(fo.getState.contains("socket")) {
      free_sockets = fo.point :: free_sockets
    }
    else free_sockets = free_sockets.filterNot(_ == fo.point)
    super.addTrace(point, fo)
  }
  override def removeTracesById(trace_ids:Int*) {
    val sockets = trace_ids.filter(traces_by_ids.contains(_)).map(traces_by_ids(_)).filter(_.getState.contains("socket"))
    free_sockets = free_sockets.filterNot(sockets.contains(_))
    super.removeTracesById(trace_ids:_*)
  }

  private def randomFreeSocket = {
    if(free_sockets.isEmpty) None
    else {
      val position = (math.random*free_sockets.length).toInt
      val free_point = free_sockets(position)
      Some(free_point)
    }
  }
  private def fillWeapon(num:Int) {
    for (i <- 0 until num) {
      randomFreeSocket match {
        case Some(point) => {
          if(free_sockets.length == 1) addToWeapon(new SocketExtender, point)
          else  {
            (math.random*4).toInt match {
              case 0 => addToWeapon(new DamageItem,     point)
              case 1 => addToWeapon(new EnergyItem,     point)
              case 2 => addToWeapon(new ShieldItem,     point)
              case 3 => addToWeapon(new SocketExtender, point)
            }
          }
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
    for {
      point <- points
      base_point = point + Vec(N_x/2, N_y/2)
    } {
      removeAllTracesFromPoint(base_point)
      addTrace(base_point, new BasePart)
      addSockets(base_point)
    }

    val restricted_points = List(Vec(-5,-1), Vec(-5,-2), Vec(-5,-3), Vec(-2, -2), Vec(-2,-3))
    for {
      point <- restricted_points
      restricted_point = point + Vec(N_x/2, N_y/2)
    } {
      removeAllTracesFromPoint(restricted_point)
      addTrace(restricted_point, new RestrictedPlace)
    }
    
    fillWeapon(10)
  }

  private def addSockets(point:Vec) {
    val points = List(Vec(-1,0)+point, Vec(1,0)+point, Vec(0,-1)+point, Vec(0,1)+point)
    for {
      cur_point <- points
      if isPointOnArea(cur_point)
      objects_at_point = point_matrix(cur_point.ix)(cur_point.iy)
      if objects_at_point.isEmpty
    } {
      addTrace(cur_point, new FreeSocket)
    }
  }

  private def removeSockets(point:Vec) {
    val points = List(Vec(-1,0)+point, Vec(1,0)+point, Vec(0,-1)+point, Vec(0,1)+point)
    for {
      cur_point <- points
      if isPointOnArea(cur_point) && isNoBasePartConnection(cur_point)
      item <- point_matrix(cur_point.ix)(cur_point.iy).filterNot(item => item.getState.contains("restricted") && item.getState.contains("base"))
    } {
      if(item.getState.contains("socket")) removeTraces(item)
      else if(item.getState.contains("extender")) {
        removeTraces(item)
        owner.inventory.addItem(item)
        removeSockets(item.point)
      }
      else {
        removeFromWeapon(item)
        owner.inventory.addItem(item)
      }
    }
  }

  private def isNoBasePartConnection(point:Vec):Boolean = {
    def _isNoExtenderOrBasePartNear(point:Vec, excluded:List[FieldObject]):Boolean = {
      val point1 = outsidePoint(point + Vec(-1,0))
      val point2 = outsidePoint(point + Vec(1,0))
      val point3 = outsidePoint(point + Vec(0,-1))
      val point4 = outsidePoint(point + Vec(0,1))

      val items:List[FieldObject] = point_matrix(point1.ix)(point1.iy) :::
                                    point_matrix(point2.ix)(point2.iy) :::
                                    point_matrix(point3.ix)(point3.iy) :::
                                    point_matrix(point4.ix)(point4.iy)
      items.find(_.getState.contains("base")) match {
        case Some(item) => false
        case None => {
          items.filter(item => item.getState.contains("extender") && !excluded.contains(item)).foldLeft(true)((result, extender) => {
            result && _isNoExtenderOrBasePartNear(extender.point, extender :: excluded)
          })
        }
      }
    }
    _isNoExtenderOrBasePartNear(point, Nil)
  }

  private var modifiers_installed = 0

  private def removeFromWeapon(item:FieldObject) {
    removeTraces(item)
    val state = item.getState
    for {
      key <- state.keys
      if state.getState(key).contains("effect")
    } owner.changeStat(key, -state.getState(key).getFloat("effect"))
    owner.checkMax()
    modifiers_installed -= 1
    if(state.contains("extender")) removeSockets(item.point)
  }

  private def addToWeapon(item:FieldObject, cursor:Vec) {
    addTrace(cursor, item)
    val state = item.getState
    for {
      key <- state.keys
      if state.getState(key).contains("effect")
    } owner.changeStat(key, state.getState(key).getFloat("effect"))
    modifiers_installed += 1
    val level = owner.intStat("level")
    if(modifiers_installed/10 > level && level < 10) owner.levelUp()
    if(state.contains("extender")) addSockets(item.point)
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
    render {
        for {
          x <- 0 until N_x
          y <- 0 until N_y
          vec = Vec(x,y)
          coord = pointCenter(vec)
        } {
          if(point_matrix(x)(y).length > 0) {
            if(is_show_cursor ||
               (!point_matrix(x)(y).head.getState.contains("socket") &&
                !point_matrix(x)(y).head.getState.contains("restricted"))) {
              GL11.glDisable(GL11.GL_TEXTURE_2D);
              GL11.glPushMatrix();
              color = point_matrix(x)(y).head.getColor
              GL11.glTranslatef(coord.x, coord.y, 0.0f);
              GL11.glRectf(-h_x/2+1, -h_y/2+1, h_x/2-1, h_y/2-1);
              GL11.glPopMatrix();
              GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
          }
          if(is_show_cursor && cursor == Vec(x,y)) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPushMatrix();
            color = GREEN
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

    interface {
        print(xml("weapon.ownership", owner.stat("name")), 10, screen_height-20)
        if(is_show_cursor) {
          if(!point_matrix(cursor.ix)(cursor.iy).isEmpty)
              print(point_matrix(cursor.ix)(cursor.iy).head.getState.getString("name"),
                    10, BottomMessages.bottom_messages_height - (font_size+2))
        }
        print(xml("weapon.helpmessage"), 10, (font_size+2)*2, GREEN)
    }

    key(KEY_UP,    100, onKeyDown = moveCursor(Vec(0,1)))
    key(KEY_DOWN,  100, onKeyDown = moveCursor(Vec(0,-1)))
    key(KEY_RIGHT, 100, onKeyDown = moveCursor(Vec(1,0)))
    key(KEY_LEFT,  100, onKeyDown = moveCursor(Vec(-1,0)))

    key(KEY_NUMPAD9, 100, onKeyDown = moveCursor(Vec(1,1)))
    key(KEY_NUMPAD8, 100, onKeyDown = moveCursor(Vec(0,1)))
    key(KEY_NUMPAD7, 100, onKeyDown = moveCursor(Vec(-1,1)))
    key(KEY_NUMPAD6, 100, onKeyDown = moveCursor(Vec(1,0)))
    key(KEY_NUMPAD4, 100, onKeyDown = moveCursor(Vec(-1,0)))
    key(KEY_NUMPAD3, 100, onKeyDown = moveCursor(Vec(1,-1)))
    key(KEY_NUMPAD2, 100, onKeyDown = moveCursor(Vec(0,-1)))
    key(KEY_NUMPAD1, 100, onKeyDown = moveCursor(Vec(-1,-1)))

    key(KEY_RETURN, onKeyDown = {
      val objects_at_cursor = point_matrix(cursor.ix)(cursor.iy)
      if(objects_at_cursor.exists(_.getState.contains("socket"))) {
        objects_at_cursor.find(!_.getState.contains("socket")) match {
          case Some(item) => {
            removeFromWeapon(item)
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
    key(KEY_ESCAPE, onKeyDown = {
      if(is_show_cursor) is_show_cursor = false
      else stop()
    })
  }

  def showWeapon() {
    weapon_screen.run()
  }
}
