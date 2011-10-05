package su.msk.dunno.blame.screens

import su.msk.dunno.scage.screens.ScageScreen
import su.msk.dunno.blame.prototypes.Living
import su.msk.dunno.scage.screens.handlers.Renderer._
import su.msk.dunno.blame.field.FieldTracer
import org.lwjgl.input.Keyboard._
import su.msk.dunno.blame.support.BottomMessages._
import su.msk.dunno.scage.single.support.ScageColors._
import su.msk.dunno.scage.single.support.messages.ScageMessage._
import su.msk.dunno.scage.single.support.Vec

class CommandScreen(living:Living) extends ScageScreen("Command Screen") {
  def findPlayer =
    FieldTracer.findVisibleObject(living.point, living.getState.getInt("dov"), obj => {
      obj.getState.contains("player") && obj.getState.getInt("health") > 0
    })

  private var command_num = -1
  def selectCommand = {
    command_num = -1
    run()
    command_num
  }

  private def changeCommand(new_command_num:Int) {
    command_num = new_command_num
    stop()
  }

  key(KEY_1, onKeyDown = changeCommand(1))
  key(KEY_2, onKeyDown = changeCommand(2))
  key(KEY_3, onKeyDown = changeCommand(3))
  key(KEY_4, onKeyDown = changeCommand(4))
  key(KEY_ESCAPE, onKeyDown = changeCommand(-1))

  // render on main screen
  windowCenter = Vec((screen_width - Blamer.right_messages_width)/2,
  		            bottom_messages_height + (screen_height - bottom_messages_height)/2)
  center = FieldTracer.pointCenter(living.point)

  backgroundColor = BLACK

  render {
    FieldTracer.drawField(Blamer.currentPlayer.point)
  }

  interface {
    print(xml("commands.list"), 10, bottom_messages_height)
    print(xml("commands.helpmessage"), 10, bottom_messages_height - ((font_size+2)*5), GREEN)
    Blamer.drawInterface()
  }
}