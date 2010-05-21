package su.msk.dunno.scage.objects

import su.msk.dunno.scage.handlers.Renderer
import org.lwjgl.opengl.GL11
import su.msk.dunno.scage.support.{Vec}
import java.io.{FileInputStream}
import su.msk.dunno.scage.handlers.eventmanager.EventManager
import org.lwjgl.input.Keyboard

class Tr0yka(init_coord:Vec) extends DynaBall(init_coord:Vec, 20) {
  val texture = Renderer.getTexture("PNG", new FileInputStream("img/stay2.png"))
  val TR0YKA = Renderer.nextDisplayListKey
  Renderer.createList(TR0YKA, texture, 16, 20, 0, 0, 160, 200)

  //var last_key:Int = 0
  EventManager.addKeyListener(Keyboard.KEY_SPACE,() => if(isTouching)addForce(Vec(0,3500)))
  EventManager.addKeyListener(Keyboard.KEY_UP,() => if(isTouching)addForce(Vec(0,3500)))
  EventManager.addKeyListener(Keyboard.KEY_LEFT,() => {
    if(isTouching)addForce(Vec(-3000,0)) else if(EventManager.last_key != Keyboard.KEY_LEFT) addForce(Vec(-1500,0))
  })
  EventManager.addKeyListener(Keyboard.KEY_RIGHT,() => {
    if(isTouching)addForce(Vec(3000,0)) else if(EventManager.last_key != Keyboard.KEY_RIGHT) addForce(Vec(1500,0))
  })

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