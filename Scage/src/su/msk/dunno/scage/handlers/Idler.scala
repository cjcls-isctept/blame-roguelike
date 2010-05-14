package su.msk.dunno.scage.handlers

import su.msk.dunno.scage.prototypes.THandler
import org.lwjgl.opengl.Display
import su.msk.dunno.scage.main.Engine

object Idler extends THandler {
  val framerate:Int = Engine.getIntProperty("framerate");

  override def doAction() = Thread.sleep(1000/framerate)
}