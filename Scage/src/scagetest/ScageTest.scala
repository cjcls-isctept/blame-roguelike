package scagetest

import su.msk.dunno.scage.main.Engine
import su.msk.dunno.scage.support.{Vec}
import su.msk.dunno.scage.handlers.eventmanager.EventManager
import org.lwjgl.input.Keyboard
import su.msk.dunno.scage.objects.{Ball, DynaBox, StaticLine}

object ScageTest {
  def main(args:Array[String]):Unit = {
    Engine.addObjects(
      new StaticLine(Vec(0,240), Vec(600,100)) ::
      new Ball(Vec(320,500)) {
        EventManager.addKeyListener(Keyboard.KEY_SPACE,() => addForce(Vec(0,1000)))
      }
    )
    Engine.setDefaultHandlers
    Engine.start
  }
}