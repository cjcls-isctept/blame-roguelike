package su.msk.dunno.blame.main.support;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class MyFont 
{
	private Texture myFont;
	
	private int charWidth = 32;
	private int charHeight = 32;
	
	private static MyFont instance;
	public static MyFont instance()
	{
		if (instance == null)
		{
			instance = new MyFont();
		}
		return instance;
	}
	
	public MyFont()
	{
		myFont = loadTexture("res/i/font2.png",0, 0, 1024, 1024);
	}
	
	public void drawString(String s, int i, int j, float size, Color c)
	{
		this.drawString(s, new Vector2D(i, j), size, c);
	}
	
	public void drawString(String s, Vector2D coord, float size, Color c)
	{
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		Vector2D vec = new Vector2D(60*size, 0);
		for(int i = 0; i < s.length(); i++)
		{
			drawChar(s.charAt(i), coord, size, c);
			coord = coord.plus(vec);
		}
		GL11.glPopMatrix();
	}
	
	public void drawChar(char ch, Vector2D coord, float size, Color c)
	{
		this.drawChar(ch, (int)coord.x, (int)coord.y, size, c);
	}
	
	public void drawChar(char ch, int x, int y, float size, Color c)
	{
		int code = Integer.valueOf(ch);
		int startx=0, starty=0;
		if(code >= 32 && code <= 47)
		{
			startx = (code - 32)*charWidth;
			starty = charHeight*2;
		}
		else if(code >= 48 && code <= 63)
		{
			startx = (code - 48)*charWidth;
			starty = charHeight*3;
		}
		else if(code >= 64 && code <= 79)
		{
			startx = (code - 64)*charWidth;
			starty = charHeight*4;
		}
		else if(code >= 80 && code <= 95)
		{
			startx = (code - 80)*charWidth;
			starty = charHeight*5;
		}
		else if(code >= 96 && code <= 111)
		{
			startx = (code - 96)*charWidth;
			starty = charHeight*6;
		}
		else if(code >= 112 && code <= 126)
		{
			startx = (code - 112)*charWidth;
			starty = charHeight*7;
		}
		else return;
		
		GL11.glPushMatrix();		
		GL11.glTranslatef(x, y, 0.0f);
		GL11.glScalef(size, size, 1.0f);
		GL11.glColor3f(c.getRed(), c.getGreen(), c.getBlue());
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, myFont.getTextureId());
		float t_width = myFont.getTextureWidth();
		float t_height = myFont.getTextureHeight();
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(startx/t_width, starty/t_height);
	    	GL11.glVertex2f(-charWidth, charHeight);
	    	
			GL11.glTexCoord2f((startx+charWidth)/t_width, starty/t_height);
			GL11.glVertex2f(charWidth, charHeight);
			
			GL11.glTexCoord2f((startx+charWidth)/t_width, (starty+charHeight)/t_height);
			GL11.glVertex2f(charWidth, -charHeight);			
			
	    	GL11.glTexCoord2f(startx/t_width, (starty+charHeight)/t_height);
			GL11.glVertex2f(-charWidth, -charHeight);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
	}
	
	// Offset are in term off pixel, not byte, the image loader figure out alone what is the bytesPerPixel
	private  Texture loadTexture(String path,int xOffSet, int yOffSet, int textWidth, int textHeight) 
	{	
		BufferedImage buffImage = null;
		try 
		{
			System.out.println("Loading image "+path);
			buffImage = ImageIO.read(new File(path));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
			        
		int bytesPerPixel = buffImage.getColorModel().getPixelSize() / 8;
		
		ByteBuffer scratch = ByteBuffer.allocateDirect(textWidth*textHeight*bytesPerPixel).order(ByteOrder.nativeOrder());	
		DataBufferByte data = ((DataBufferByte) buffImage.getRaster().getDataBuffer());
		
		for (int i = 0 ; i < textHeight ; i++)
			scratch.put(data.getData(),(xOffSet+(yOffSet+i)*buffImage.getWidth())*bytesPerPixel, textWidth * bytesPerPixel);
		
        scratch.rewind();
        
        // Create A IntBuffer For Image Address In Memory
        IntBuffer buf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
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

        Texture newTexture = new Texture();
        newTexture.textureId = buf.get(0);     // Return Image Addresses In Memory
        newTexture.textureHeight = textHeight;
        newTexture.textureWidth = textWidth;
        
        return newTexture;
    }
	
	public void destroy() {
		IntBuffer scratch = BufferUtils.createIntBuffer(1);
		scratch.put(0, myFont.getTextureId());
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDeleteTextures(scratch);
	}
}
