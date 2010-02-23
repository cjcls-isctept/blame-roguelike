package su.msk.dunno.blame.support;

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

import su.msk.dunno.blame.main.Blame;

public class MyFont 
{
	public final static int WALL            = 1;
	public final static int FLOOR           = 2;
	public final static int DOOR_CLOSED     = 3;
	public final static int DOOR_OPENED     = 4;
	public final static int PLAYER          = 5;
	public final static int SILICONCREATURE = 6;
	public final static int BULLET 			= 7;
	public final static int IMP				= 8;
	public final static int MAINSELECTOR	= 9;
	public final static int MINORSELECTOR	= 10;
	public final static int WEAPONBASE		= 11;
	public final static int SOCKET			= 12;
	public final static int CORPSE			= 13;
	public final static int EMPTY			= 14;
	public static final int STATION 		= 15;
	
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
	
	public void initDisplayLists()
	{
		createList(myFont, '#', WALL);
		createList(myFont, '.', FLOOR);
		createList(myFont, '+', DOOR_CLOSED);
		createList(myFont, '\'', DOOR_OPENED);
		createList(myFont, '@', PLAYER);
		createList(myFont, 'S', SILICONCREATURE);
		createList(myFont, '*', BULLET);
		createList(myFont, '*', IMP);
		createList(myFont, 'X', MAINSELECTOR);
		createList(myFont, 'x', MINORSELECTOR);
		createList(myFont, 'w', WEAPONBASE);
		createList(myFont, 'o', SOCKET);
		createList(myFont, '%', CORPSE);
		//createList(myFont, ' ', EMPTY);	// its suppose to draw emptiness anyway... :)
		createList(myFont, 'A', STATION);
	}
	
	public void createList(Texture texture, char ch, int list_name)
	{
		float t_width = texture.getTextureWidth();
		float t_height = texture.getTextureHeight();
		Vector2D start_v = getCharCoord(ch);
		GL11.glNewList(list_name, GL11.GL_COMPILE);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(start_v.x/t_width, start_v.y/t_height);
	    	GL11.glVertex2f(-charWidth, charHeight+50);
	    	
			GL11.glTexCoord2f((start_v.x+charWidth)/t_width, start_v.y/t_height);
			GL11.glVertex2f(charWidth, charHeight+50);
			
			GL11.glTexCoord2f((start_v.x+charWidth)/t_width, (start_v.y+charHeight)/t_height);
			GL11.glVertex2f(charWidth, -charHeight);			
			
	    	GL11.glTexCoord2f(start_v.x/t_width, (start_v.y+charHeight)/t_height);
			GL11.glVertex2f(-charWidth, -charHeight);
		GL11.glEnd();
		GL11.glEndList();
	}
	
	private Vector2D getCharCoord(char ch)
	{
		Vector2D start_v = new Vector2D();
		int code = Integer.valueOf(ch);
		if(code >= 32 && code <= 47)
		{
			start_v.x = (code - 32)*charWidth;
			start_v.y = charHeight*2;
		}
		else if(code >= 48 && code <= 63)
		{
			start_v.x = (code - 48)*charWidth;
			start_v.y = charHeight*3;
		}
		else if(code >= 64 && code <= 79)
		{
			start_v.x = (code - 64)*charWidth;
			start_v.y = charHeight*4;
		}
		else if(code >= 80 && code <= 95)
		{
			start_v.x = (code - 80)*charWidth;
			start_v.y = charHeight*5;
		}
		else if(code >= 96 && code <= 111)
		{
			start_v.x = (code - 96)*charWidth;
			start_v.y = charHeight*6;
		}
		else if(code >= 112 && code <= 126)
		{
			start_v.x = (code - 112)*charWidth;
			start_v.y = charHeight*7;
		}
		return start_v;
	}
	
	public void drawString(String s, int i, int j, float size, Color c)
	{
		this.drawString(s, new Vector2D(i, j), size, c);
	}
	
	public void drawString(String s, Vector2D coord, float size, Color c)
	{
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glScalef(size, size, 1.0f);
		coord = coord.mul(1.0f/size);
		Vector2D vec = new Vector2D(60, 0);
		for(int i = 0; i < s.length(); i++)
		{
			drawChar(s.charAt(i), coord, c);
			coord = coord.plus(vec);
		}
		GL11.glPopMatrix();
	}
	
	public void drawChar(char ch, Vector2D coord, Color c)
	{
		this.drawChar(ch, coord.x, coord.y, c);
	}
	
	public void drawChar(char ch, float x, float y, Color c)
	{
		Vector2D start_v = getCharCoord(ch);
		
		GL11.glPushMatrix();		
		GL11.glTranslatef(x, y, 0.0f);
		GL11.glColor3f(c.getRed(), c.getGreen(), c.getBlue());
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, myFont.getTextureId());
		float t_width = myFont.getTextureWidth();
		float t_height = myFont.getTextureHeight();
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(start_v.x/t_width, start_v.y/t_height);
	    	GL11.glVertex2f(-charWidth, charHeight+50);	// +50: lifts up symbols
	    	
			GL11.glTexCoord2f((start_v.x+charWidth)/t_width, start_v.y/t_height);
			GL11.glVertex2f(charWidth, charHeight+50);
			
			GL11.glTexCoord2f((start_v.x+charWidth)/t_width, (start_v.y+charHeight)/t_height);
			GL11.glVertex2f(charWidth, -charHeight);			
			
	    	GL11.glTexCoord2f(start_v.x/t_width, (start_v.y+charHeight)/t_height);
			GL11.glVertex2f(-charWidth, -charHeight);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public void drawDisplayList(int code, int x, int y, Color c)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0f);
		GL11.glColor3f(c.getRed(), c.getGreen(), c.getBlue());
    	GL11.glCallList(code);				
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
