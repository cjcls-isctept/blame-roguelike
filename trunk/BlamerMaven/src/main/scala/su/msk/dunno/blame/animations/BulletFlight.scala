package su.msk.dunno.blame.animations

import su.msk.dunno.scage.screens.ScageScreen
import su.msk.dunno.blame.support.BottomMessages
import su.msk.dunno.blame.screens.Blamer
import su.msk.dunno.scage.single.support.{ScageColor, Vec}
import su.msk.dunno.scage.single.support.ScageColors._
import su.msk.dunno.scage.single.support.ScageProperties._
import su.msk.dunno.blame.field.{FieldObject, FieldTracer}
import su.msk.dunno.blame.support.MyFont._
import su.msk.dunno.scage.screens.handlers.Renderer._
import su.msk.dunno.scage.screens.support.tracer.{Trace, State}

class BulletFlight(val start_point:Vec, val end_point:Vec, val color:ScageColor, val delay:Long = property("animation.bulletflight.delay", 30.toLong))
extends ScageScreen("Bullet Flight") {
  private val line = FieldTracer.line(start_point, end_point)

  val trace = FieldTracer.addTrace(start_point, new FieldObject {
    def getSymbol = BULLET
    def getColor = color
    def isTransparent = true
    def isPassable = true

    def getState = new State
    def changeState(сhanger:Trace, s:State) {}
  })

  FieldTracer.addLightSource(trace.point, 5, trace.id)

  private var count = 0
  private var last_move_time = System.currentTimeMillis
  action {
    if(System.currentTimeMillis - last_move_time > delay) {
      if(count < line.length-1) {
        FieldTracer.updateLocation(trace, line({count+=1; count}))
        last_move_time = System.currentTimeMillis
      }
      else stop()
    }
  }

  exit {
    FieldTracer.removeTraces(trace)
  }

  // render on main screen
  windowCenter = Vec((screen_width - Blamer.right_messages_width)/2,
  		     BottomMessages.bottom_messages_height + (screen_height - BottomMessages.bottom_messages_height)/2)
  center = FieldTracer.pointCenter(Blamer.currentPlayer.point)

  backgroundColor = BLACK

  render {
    FieldTracer.drawField(Blamer.currentPlayer.point)
  }

  interface {
    BottomMessages.showBottomMessages(0)
    Blamer.drawInterface()
  }

  run()
}
