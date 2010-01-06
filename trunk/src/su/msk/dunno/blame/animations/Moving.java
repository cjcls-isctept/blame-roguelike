package su.msk.dunno.blame.animations;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AAnimation;
import su.msk.dunno.blame.prototypes.ALiving;

public class Moving extends AAnimation 
{
	Point pFrom, pTo;
	Point dir;
	ALiving al;
	
	public Moving(Field field, ALiving al, Point p_from, Point p_to) 
	{
		super(field, false);
		
		this.al = al;
		al.preventDraw = true;
		pFrom = p_from;
		pTo = p_to;

		dir = pTo.minus(pFrom);
				
		duration = Blame.fps/3;
		frames = duration;
	}

	@Override public void nextFrame()
	{
		MyFont.instance().drawChar(al.getSymbol(), pFrom.x*Blame.scale+dir.x*cur_frame*Blame.scale/frames, pFrom.y*Blame.scale+dir.y*cur_frame*Blame.scale/frames, Blame.scale*0.01f, al.getColor());
	}
	
	@Override public void stop()
	{
		al.preventDraw = false;
	}
}
