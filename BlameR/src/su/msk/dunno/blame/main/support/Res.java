package su.msk.dunno.blame.main.support;

import org.lwjgl.opengl.GL11;

public class Res 
{
	public final static int WALL   = 0;
	public final static int FLOOR  = 1;
	public final static int DOOR   = 2;
	public final static int PLAYER = 3;
	public final static int ENEMY  = 4;
	
	public static void init(Texture font_texture, int start_x, int start_y)
	{
		/*createList(WALL, font_texture, 32, 32, 0, 0, 32, 32);
		createList(FLOOR, font_texture, 32, 32, 0, 0, 32, 32);
		createList(DOOR, font_texture, 32, 32, 0, 0, 32, 32);
		createList(PLAYER, font_texture, 32, 32, 0, 0, 32, 32);
		createList(ENEMY, font_texture, 32, 32, 0, 0, 32, 32);*/
	}
	
	public static void createList(Texture texture, char ch, int list_name, float charWidth, float charHeight)
	{
		float t_width = texture.getTextureWidth();
		float t_height = texture.getTextureHeight();
		float start_x = 0;
		float start_y = 0;
		int code = Integer.valueOf(ch);
		if(code >= 32 && code <= 47)
		{
			start_x = (code - 32)*charWidth;
			start_y = charHeight*2;
		}
		else if(code >= 48 && code <= 63)
		{
			start_x = (code - 48)*charWidth;
			start_y = charHeight*3;
		}
		else if(code >= 64 && code <= 79)
		{
			start_x = (code - 64)*charWidth;
			start_y = charHeight*4;
		}
		else if(code >= 80 && code <= 95)
		{
			start_x = (code - 80)*charWidth;
			start_y = charHeight*5;
		}
		else if(code >= 96 && code <= 111)
		{
			start_x = (code - 96)*charWidth;
			start_y = charHeight*6;
		}
		else if(code >= 112 && code <= 126)
		{
			start_x = (code - 112)*charWidth;
			start_y = charHeight*7;
		}
		GL11.glNewList(list_name, GL11.GL_COMPILE);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(start_x/t_width, start_y/t_height);
	    	GL11.glVertex2f(-charWidth, charHeight);
	    	
			GL11.glTexCoord2f((start_x+charWidth)/t_width, start_y/t_height);
			GL11.glVertex2f(charWidth, charHeight);
			
			GL11.glTexCoord2f((start_x+charWidth)/t_width, (start_y+charHeight)/t_height);
			GL11.glVertex2f(charWidth, -charHeight);			
			
	    	GL11.glTexCoord2f(start_x/t_width, (start_y+charHeight)/t_height);
			GL11.glVertex2f(-charWidth, -charHeight);
		GL11.glEnd();
		GL11.glEndList();
	}
}