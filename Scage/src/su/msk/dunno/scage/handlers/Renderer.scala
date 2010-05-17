package su.msk.dunno.scage.handlers

import su.msk.dunno.scage.main.Engine
import org.lwjgl.opengl.{DisplayMode, Display, GL11}
import org.lwjgl.util.glu.GLU
import su.msk.dunno.scage.prototypes.{THandler}

object Renderer extends THandler {

  val width = Engine.getIntProperty("width");
  val height:Int = Engine.getIntProperty("height");

 Display.setDisplayMode(new DisplayMode(width, height));
 Display.setTitle(Engine.getProperty("name")+" - "+Engine.getProperty("version"));
 Display.setVSyncEnabled(true);
 Display.create();

 GL11.glEnable(GL11.GL_TEXTURE_2D);
 GL11.glClearColor(0,0,0,0);
 GL11.glDisable(GL11.GL_DEPTH_TEST);

 GL11.glEnable(GL11.GL_BLEND);
 GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

 GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
 GL11.glLoadIdentity(); // Reset The Projection Matrix
 GLU.gluOrtho2D(0, width, 0, height);

 GL11.glMatrixMode(GL11.GL_MODELVIEW);
 GL11.glLoadIdentity();

  override def doAction() = {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);
		GL11.glLoadIdentity();
    Engine.getObjects.foreach(o => o.render)
    Display.update();
  }

  override def exitSequence() = Display.destroy();
}