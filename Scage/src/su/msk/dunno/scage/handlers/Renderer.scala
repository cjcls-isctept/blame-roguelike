package su.msk.dunno.scage.handlers

import su.msk.dunno.scage.main.Engine
import org.lwjgl.opengl.{DisplayMode, Display, GL11}
import org.lwjgl.util.glu.GLU
import su.msk.dunno.scage.prototypes.{THandler}
import su.msk.dunno.scage.support.{Vec, Color}

object Renderer extends THandler {
  val CIRCLE = 1

  val width = Engine.getIntProperty("width");
  val height:Int = Engine.getIntProperty("height");

  Display.setDisplayMode(new DisplayMode(width, height));
  Display.setTitle(Engine.getProperty("name")+" - "+Engine.getProperty("version"));
  Display.setVSyncEnabled(true);
  Display.create();

  GL11.glEnable(GL11.GL_TEXTURE_2D);
  GL11.glClearColor(1,1,1,0);
  GL11.glDisable(GL11.GL_DEPTH_TEST);

  GL11.glEnable(GL11.GL_BLEND);
  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

  GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
  GL11.glLoadIdentity(); // Reset The Projection Matrix
  GLU.gluOrtho2D(0, width, 0, height);

  GL11.glMatrixMode(GL11.GL_MODELVIEW);
  GL11.glLoadIdentity();

  GL11.glNewList(CIRCLE, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_POLYGON);
			for(i <- 0 to 100)
			{
				val cosine = Math.cos(i*2*Math.Pi/100).toFloat;
				val sine = Math.sin(i*2*Math.Pi/100).toFloat;
				GL11.glVertex2f(cosine*1, sine*1);
			}
	  GL11.glEnd();
	GL11.glEndList();

  override def doAction() = {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);
		GL11.glLoadIdentity();
    Engine.getObjects.foreach(o => o.render)
    Display.update();
  }

  def setColor(c:Color) = GL11.glColor3f(c.getRed, c.getGreen, c.getBlue)
  def drawLine(v1:Vec, v2:Vec) = {
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glBegin(GL11.GL_LINES);
    		GL11.glVertex2f(v1.x, v1.y);
    		GL11.glVertex2f(v2.x, v2.y);
    	GL11.glEnd();
    GL11.glEnable(GL11.GL_TEXTURE_2D);
  }
  def drawCircle(coord:Vec, radius:Float) = {
    GL11.glDisable(GL11.GL_TEXTURE_2D);
      GL11.glPushMatrix();
      GL11.glTranslatef(coord.x, coord.y, 0.0f);
      GL11.glScalef(radius,radius,radius)
     	  GL11.glCallList(CIRCLE);
      GL11.glPopMatrix()
    GL11.glEnable(GL11.GL_TEXTURE_2D);
  }

  override def exitSequence() = Display.destroy();
}