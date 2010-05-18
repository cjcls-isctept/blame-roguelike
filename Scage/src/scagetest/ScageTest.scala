package scagetest

import su.msk.dunno.scage.main.Engine
import su.msk.dunno.scage.support.{Vec}
import su.msk.dunno.scage.objects.{DynaBox, StaticLine}

object ScageTest {
  def main(args:Array[String]):Unit = {
    Engine.addObjects(
      new StaticLine(Vec(0,240), Vec(600,100)) :: new DynaBox(Vec(320,500))
    )
    Engine.setDefaultHandlers
    Engine.start
  }
}