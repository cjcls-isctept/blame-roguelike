package scagetest

import su.msk.dunno.scage.main.Engine
import su.msk.dunno.scage.support.{Vec}
import su.msk.dunno.scage.handlers.eventmanager.EventManager
import org.lwjgl.input.Keyboard
import su.msk.dunno.scage.objects.{Ball, StaticLine}
import su.msk.dunno.scage.handlers.Renderer
import su.msk.dunno.scage.support.messages.Message
object ScageTest {
  def main(args:Array[String]):Unit = {
    Engine.addObjects(
      new StaticLine(Vec(0,240), Vec(300,100)) ::
      new StaticLine(Vec(340,100), Vec(640,240)) ::
      new StaticLine(Vec(0,65), Vec(640,65)) ::
      new StaticLine(Vec(250,200), Vec(390,200)) ::
      new Ball(Vec(320,400)) {
        EventManager.addKeyListener(Keyboard.KEY_SPACE,() => addForce(Vec(0,3000)))
        EventManager.addKeyListener(Keyboard.KEY_UP,() => addForce(Vec(0,3000)))
        EventManager.addKeyListener(Keyboard.KEY_LEFT,() => addForce(Vec(-3000,0)))
        EventManager.addKeyListener(Keyboard.KEY_RIGHT,() => addForce(Vec(3000,0)))
      } ::
      new Ball(Vec(340,400))
    )
    Renderer.addInterfaceElement(() => Message.print("fps: "+Engine.fps, 20, 460))
    Engine.setDefaultHandlers
    Engine.start
  }
}