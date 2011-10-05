package su.msk.dunno.blame.screens

import su.msk.dunno.scage.screens.ScageScreen
import org.lwjgl.input.Keyboard._
import su.msk.dunno.scage.single.support.Vec
import su.msk.dunno.scage.single.support.ScageColors._
import su.msk.dunno.scage.screens.handlers.Renderer._
import su.msk.dunno.blame.support.BottomMessages._
import su.msk.dunno.blame.support.MyFont._
import su.msk.dunno.blame.prototypes.Living
import su.msk.dunno.scage.single.support.messages.ScageMessage._
import su.msk.dunno.blame.field.{FieldObject, FieldTracer}

object SelectTarget {
  private var living:Living = null
  private var stop_key = -1
  private var target_point:Vec = null
  private var select_line:List[Vec] = Nil
  def apply(_living:Living, new_key:Int, condition: FieldObject => Boolean = _.getState.contains("enemy")):Vec = {
    living = _living
    stop_key = new_key
    target_point = findTarget(condition)
    select_line = FieldTracer.line(living.point, target_point)
    selector_screen.run()
    target_point
  }

  private def findTarget(condition: FieldObject => Boolean):Vec = {
    val dov = living.getState.getInt("dov")
    FieldTracer.findVisibleObject(living.point, dov, obj => {
      obj.getState.getInt("health") > 0 && condition(obj)
    }) match {
      case Some(live_enemy) => live_enemy.point
      case None => living.point
    }
  }

  private def clearSelectLine() {
    select_line.foreach(FieldTracer.allowDraw(_))
    select_line = Nil
  }
  private def buildSelectLine(delta:Vec) {
    target_point += delta
    clearSelectLine()
    select_line = FieldTracer.line(living.point, target_point)
    if(select_line.size > 1) select_line = select_line.tail
    select_line.foreach(FieldTracer.preventDraw(_))
    target_point = select_line.last
  }
  private def drawSelectLine() {
    if(!select_line.isEmpty) {
      select_line.init.foreach(point => {
        drawDisplayList(MINOR_SELECTOR, FieldTracer.pointCenter(point), WHITE)
      })
      drawDisplayList(MAIN_SELECTOR, FieldTracer.pointCenter(select_line.last), WHITE)
    }
  }

  private lazy val selector_screen = new ScageScreen("Target Selector") {
    key(KEY_NUMPAD9, 100, onKeyDown = buildSelectLine(Vec(1,1)))
    key(KEY_NUMPAD8, 100, onKeyDown = buildSelectLine(Vec(0,1)))
    key(KEY_NUMPAD7, 100, onKeyDown = buildSelectLine(Vec(-1,1)))
    key(KEY_NUMPAD6, 100, onKeyDown = buildSelectLine(Vec(1,0)))
    key(KEY_NUMPAD4, 100, onKeyDown = buildSelectLine(Vec(-1,0)))
    key(KEY_NUMPAD3, 100, onKeyDown = buildSelectLine(Vec(1,-1)))
    key(KEY_NUMPAD2, 100, onKeyDown = buildSelectLine(Vec(0,-1)))
    key(KEY_NUMPAD1, 100, onKeyDown = buildSelectLine(Vec(-1,-1)))

    key(KEY_UP,    100, onKeyDown = buildSelectLine(Vec(0,1)))
    key(KEY_RIGHT, 100, onKeyDown = buildSelectLine(Vec(1,0)))
    key(KEY_LEFT,  100, onKeyDown = buildSelectLine(Vec(-1,0)))
    key(KEY_DOWN,  100, onKeyDown = buildSelectLine(Vec(0,-1)))

    key(stop_key, onKeyDown = {
      clearSelectLine()
      stop()
    })

    key(KEY_ESCAPE, onKeyDown = {
      target_point = living.point
      clearSelectLine()
      stop()
    })

    // render on main screen
    windowCenter = Vec((screen_width - Blamer.right_messages_width)/2,
                    bottom_messages_height + (screen_height - bottom_messages_height)/2)
    center = FieldTracer.pointCenter(living.point)

    backgroundColor = BLACK

    render {
      FieldTracer.drawField(Blamer.currentPlayer.point)
      drawSelectLine()
    }

    interface {
      FieldTracer.objectsAtPoint(target_point) match {
        case head :: tail => print(xml("selecttarget.target")+" "+head.getState.getString("name"),
                                10, bottom_messages_height - (font_size+2))
        case _ =>
      }
      print(xml("selecttarget.helpmessage"),
        10, bottom_messages_height - ((font_size+2)*2), GREEN)
      showBottomMessages(2)
      Blamer.drawInterface()
    }

    private val description_screen = new ScageScreen("Description Screen") {
        interface {
            FieldTracer.objectsAtPoint(target_point) match {
              case head :: tail => {
                print(head.getState.getString("name")+"\n\n"+
                      head.getState.getString("description"), 10, screen_height-(font_size+2), WHITE)
              }
              case _ =>
            }
            print(xml("selecttarget.description.helpmessage"), 10, (font_size+2), GREEN)
        }

        key(KEY_ESCAPE, onKeyDown = stop())
      }
    key(KEY_I, onKeyDown = {
      description_screen.run()
    })
  }
}
