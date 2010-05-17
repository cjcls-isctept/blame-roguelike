package su.msk.dunno.scage.handlers

import net.phys2d.raw.strategies.QuadSpaceStrategy
import net.phys2d.raw.World
import net.phys2d.math.Vector2f
import su.msk.dunno.scage.prototypes.THandler
import su.msk.dunno.scage.main.Engine

object Physics extends THandler {
  val dt = Engine.getIntProperty("dt")
  val gravity = Engine.getIntProperty("gravity")
  val world = new World(new Vector2f(0.0f, gravity), 10, new QuadSpaceStrategy(20,5));
  Engine.getObjects.foreach(o => world.add(o.body))

  override def doAction() = world.step(dt)
}