package su.msk.dunno.scage.prototypes

import net.phys2d.raw.Body
import net.phys2d.math.Vector2f
import su.msk.dunno.scage.support.Vec

trait Physical extends Drawable {
  val body:Body

  def addForce(f:Vec) = body.addForce(new Vector2f(f.x, f.y))
  def coord() = {
    val pos = body.getPosition
    Vec(pos.getX, pos.getY)
  }
  def velocity = {
    val vel = body.getVelocity
    Vec(vel.getX, vel.getY)
  }

  def ::(o:Physical) = o :: List[Physical](this)
}