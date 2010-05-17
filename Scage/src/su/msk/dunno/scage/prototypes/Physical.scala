package su.msk.dunno.scage.prototypes

import net.phys2d.raw.Body
trait Physical {
  val body:Body

  def render() = {}
  def ::(o:Physical) = o :: List[Physical](this)
}