package scagetest

import su.msk.dunno.scage.main.Engine
import su.msk.dunno.scage.support.{Vec}
import su.msk.dunno.scage.support.messages.Message
import su.msk.dunno.scage.handlers.{Renderer}
import su.msk.dunno.scage.objects.{DynaBall, Tr0yka, StaticLine}
import su.msk.dunno.scage.handlers.eventmanager.EventManager

object ScageTest {
  def main(args:Array[String]):Unit = {
    Engine.setDefaultHandlers
    Engine.addObjects(
      new Tr0yka(Vec(320,400)) {
        Renderer.addInterfaceElement(() => Message.print("touching: "+(if(isTouching)"true" else "false"), 20, 420))
        //Renderer.addInterfaceElement(() => Message.print(touching, 20, 420))
      } ::
      new StaticLine(Vec(0,240), Vec(300,110)) ::
      new StaticLine(Vec(340,110), Vec(640,240)) ::
      new StaticLine(Vec(0,65), Vec(640,65)) ::
      new StaticLine(Vec(250,200), Vec(390,200)) ::
      new DynaBall(Vec(340,400), 15)
    )
    Renderer.addInterfaceElement(() => Message.print("fps: "+Engine.fps, 20, 460))
    Renderer.addInterfaceElement(() => Message.print("last key: "+EventManager.last_key, 20, 440))
    Engine.start
  }
}