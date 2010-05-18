package su.msk.dunno.scage.objects

import su.msk.dunno.scage.prototypes.Physical
import su.msk.dunno.scage.support.Vec
import net.phys2d.raw.Body
import org.lwjgl.opengl.GL11
import net.phys2d.raw.shapes.Box
import net.phys2d.math.{Vector2f, ROVector2f}
import util.Random

class DynaBox(val coord:Vec) extends Physical {
  val box = new Box(20,30)
  val body = new Body(box, 1)
  body.setPosition(coord.x, coord.y)
  body.setRotation((Random.nextFloat() * 2 * Math.Pi).toFloat);

  override def render() = {
    val verts:Array[Vector2f] = box.getPoints(body.getPosition(), body.getRotation());
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glBegin(GL11.GL_POLYGON);
        verts.foreach(v => GL11.glVertex2f(v.getX, v.getY))
      GL11.glEnd();
    GL11.glEnable(GL11.GL_TEXTURE_2D);
  }
}