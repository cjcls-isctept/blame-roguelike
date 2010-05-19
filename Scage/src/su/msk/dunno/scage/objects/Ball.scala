package su.msk.dunno.scage.objects

import su.msk.dunno.scage.prototypes.Physical
import net.phys2d.raw.Body
import net.phys2d.raw.shapes.Circle
import su.msk.dunno.scage.handlers.Renderer
import su.msk.dunno.scage.support.{Color, Vec}

class Ball(init_coord:Vec) extends Physical {
  val body = new Body(new Circle(15), 2);
  body.setPosition(init_coord.x, init_coord.y)

  override def render() = {
    Renderer.setColor(Color.BLACK)
    Renderer.drawCircle(coord, 15)
    Renderer.drawLine(coord, coord+velocity)
  }
}