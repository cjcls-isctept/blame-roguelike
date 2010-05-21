package su.msk.dunno.scage.objects

import su.msk.dunno.scage.handlers.Renderer
import org.lwjgl.opengl.GL11
import su.msk.dunno.scage.support.{Color, Vec}
import java.io.{InputStream, FileInputStream}
import org.newdawn.slick.opengl.{Texture, TextureLoader}

class Tr0yka(init_coord:Vec) extends Ball(init_coord:Vec) {
  val texture = Renderer.getTexture("PNG", new FileInputStream("img/stay2.png"))
  val TR0YKA = Renderer.nextDisplayListKey
  Renderer.createList(TR0YKA, texture, 16, 20, 0, 0, 160, 200)
  

  override def render() = {
   /* Renderer.setColor(Color.BLACK)
    Renderer.drawCircle(coord, 15)
    Renderer.drawLine(coord, coord+velocity)*/

    GL11.glPushMatrix();
		  GL11.glTranslatef(coord.x, coord.y, 0.0f);
      val x_dir = -Math.signum(velocity*Vec(1,0)) 
      GL11.glScalef(if(x_dir==0)1 else x_dir,1,1)
      GL11.glColor3f(1, 1, 1)
      GL11.glCallList(TR0YKA);
    GL11.glPopMatrix()
  }
}