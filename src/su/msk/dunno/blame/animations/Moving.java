package su.msk.dunno.blame.animations;

import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AAnimation;
import su.msk.dunno.blame.prototypes.ALiving;

public class Moving extends AAnimation 
{
	Point player_point;
	Point pFrom, pTo;
	Point dir;
	ALiving al;
	
	public Moving(Field field, ALiving al, Point p_from, Point p_to) 
	{
		super(field, false);
		
		this.al = al;
		al.preventDraw = true;
		//field.removeObject(al);
		player_point = Blame.playCibo?Blame.cibo.cur_pos:Blame.killy.cur_pos;
		pFrom = p_from;
		pTo = p_to;

		dir = pTo.minus(pFrom);
				
		duration = Blame.fps/5;
		frames = duration;
	}

	@Override public void nextFrame()
	{
		GL11.glTranslatef(220-player_point.x*Blame.scale, 
		  		  		  240-player_point.y*Blame.scale, 
		  		  		  0.0f);
		MyFont.instance().drawChar(al.getSymbol(), pFrom.x*Blame.scale+dir.x*cur_frame*Blame.scale/frames, pFrom.y*Blame.scale+dir.y*cur_frame*Blame.scale/frames, Blame.scale*0.01f, al.getColor());
	}
	
	@Override public void stop()
	{
		al.preventDraw = false;
	}
}
