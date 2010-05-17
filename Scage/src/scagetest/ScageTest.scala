package scagetest

import su.msk.dunno.scage.main.Engine
import su.msk.dunno.scage.support.{Vec}
import su.msk.dunno.scage.objects.{StaticLine}

object ScageTest {
  def main(args:Array[String]):Unit = {
    Engine.addObject(
      new StaticLine(Vec(300,240), Vec(10,0))
    )
    Engine.setDefaultHandlers
    Engine.start
  }
}