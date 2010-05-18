package su.msk.dunno.scage.objects

import su.msk.dunno.scage.prototypes.Physical
import net.phys2d.raw.Body
import net.phys2d.raw.shapes.Circle
import java.lang.Math
import org.lwjgl.opengl.GL11
import su.msk.dunno.scage.handlers.Renderer
import su.msk.dunno.scage.support.{Color, Vec}

class Ball(init_coord:Vec) extends Physical {
  val body = new Body(new Circle(15), 2);
  body.setPosition(init_coord.x, init_coord.y)

  override def render() = {
    val coord = body.getPosition
    GL11.glDisable(GL11.GL_TEXTURE_2D);
      GL11.glPushMatrix();
      GL11.glTranslatef(coord.getX, coord.getY, 0.0f);
        Renderer.setColor(Color.BLACK)    	
     	  GL11.glCallList(Renderer.CIRCLE);
      GL11.glPopMatrix()
    GL11.glEnable(GL11.GL_TEXTURE_2D);
  }
}