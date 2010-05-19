package su.msk.dunno.scage.handlers

import renderer.Texture
import su.msk.dunno.scage.main.Engine
import org.lwjgl.opengl.{DisplayMode, Display, GL11}
import org.lwjgl.util.glu.GLU
import su.msk.dunno.scage.support.{Vec, Color}
import su.msk.dunno.scage.prototypes.{Drawable, THandler}
import su.msk.dunno.scage.support.messages.Message
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.{DataBufferByte, BufferedImage}
import java.nio.{IntBuffer, ByteOrder, ByteBuffer}

object Renderer extends THandler {
  val CIRCLE = 1
  private var next_displaylist_key = 2
  def nextDisplayListKey() = {
    val next_key = next_displaylist_key
    next_displaylist_key += 1
    next_key
  }

  val width = Engine.getIntProperty("width");
  val height = Engine.getIntProperty("height");

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
		GL11.glBegin(GL11.GL_LINE_LOOP);
			for(i <- 0 to 100)
			{
				val cosine = Math.cos(i*2*Math.Pi/100).toFloat;
				val sine = Math.sin(i*2*Math.Pi/100).toFloat;
				GL11.glVertex2f(cosine, sine);
			}
	  GL11.glEnd();
	GL11.glEndList();

  var interface:List[Drawable] = List[Drawable]()
  def addInterfaceElement(renderFunc: () => Unit) = {
    interface = new Drawable(){ def render = renderFunc() } :: interface
  }
  def addInterfaceElement(element:Drawable) = {interface = element :: interface}
  def addInterfaceElements(elements:List[Drawable]) = {interface = elements ::: interface}

  override def doAction() = {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);
		GL11.glLoadIdentity();
      Engine.getObjects.foreach(o => o.render)
      interface.foreach(d => d.render)
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
      GL11.glScalef(radius,radius,1)
     	  GL11.glCallList(CIRCLE);
      GL11.glPopMatrix()
    GL11.glEnable(GL11.GL_TEXTURE_2D);
  }

  override def exitSequence() = Display.destroy();

  // Offset are in term off pixel, not byte, the image loader figure out alone what is the bytesPerPixel
  def loadTexture(path:String, xOffSet:Int, yOffSet:Int, textWidth:Int, textHeight:Int):Texture = {
    val buffImage:BufferedImage = ImageIO.read(new File(path))
    val bytesPerPixel = buffImage.getColorModel().getPixelSize() / 8;
    val scratch:ByteBuffer = ByteBuffer.allocateDirect(textWidth*textHeight*bytesPerPixel).order(ByteOrder.nativeOrder())
    val data:DataBufferByte = (buffImage.getRaster().getDataBuffer()).asInstanceOf[DataBufferByte]
    for(i <- 0 to textHeight) {
      scratch.put(data.getData(),(xOffSet+(yOffSet+i)*buffImage.getWidth())*bytesPerPixel, textWidth * bytesPerPixel)
    }
    scratch.rewind()
    // Create A IntBuffer For Image Address In Memory
    val buf:IntBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer()
    GL11.glGenTextures(buf); // Create Texture In OpenGL

    // Create Nearest Filtered Texture
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, buf.get(0));
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    //GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
     				  	      0,
     				  	      GL11.GL_RGBA,
     				  	      textWidth,
     				  	      textHeight,
     				  	      0,
     				  	      GL11.GL_RGBA,
     				  	      GL11.GL_UNSIGNED_BYTE,
     				  	      scratch);
    val newTexture = new Texture()
    newTexture.textureId = buf.get(0);     // Return Image Addresses In Memory
    newTexture.textureHeight = textHeight;
    newTexture.textureWidth = textWidth;
    newTexture
  }
}