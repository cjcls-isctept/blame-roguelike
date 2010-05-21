package scagetest

import su.msk.dunno.scage.main.Engine
import su.msk.dunno.scage.support.{Vec}
import su.msk.dunno.scage.handlers.eventmanager.EventManager
import org.lwjgl.input.Keyboard
import su.msk.dunno.scage.support.messages.Message
import su.msk.dunno.scage.objects.{Tr0yka, Ball, StaticLine}
import su.msk.dunno.scage.handlers.{Physics, Renderer}

object ScageTest {
  def main(args:Array[String]):Unit = {
    Engine.addObjects(
      new Tr0yka(Vec(320,400)) {
        EventManager.addKeyListener(Keyboard.KEY_SPACE,() => if(isTouching)addForce(Vec(0,3000)))
        EventManager.addKeyListener(Keyboard.KEY_UP,() => if(isTouching)addForce(Vec(0,3000)))
        EventManager.addKeyListener(Keyboard.KEY_LEFT,() => if(isTouching)addForce(Vec(-3000,0)))
        EventManager.addKeyListener(Keyboard.KEY_RIGHT,() => if(isTouching)addForce(Vec(3000,0)))
        Renderer.addInterfaceElement(() => Message.print("touching: "+touching, 20, 440))
      } ::
      new StaticLine(Vec(0,240), Vec(300,100)) ::
      new StaticLine(Vec(340,100), Vec(640,240)) ::
      new StaticLine(Vec(0,65), Vec(640,65)) ::
      new StaticLine(Vec(250,200), Vec(390,200)) ::
      new Ball(Vec(340,400))
    )
    Physics.update
    Engine.setDefaultHandlers
    Renderer.addInterfaceElement(() => Message.print("fps: "+Engine.fps, 20, 460))
    Engine.start
  }
}