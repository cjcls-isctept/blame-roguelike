package su.msk.dunno.blame.animations;

import java.util.HashMap;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.main.support.Vector2D;
import su.msk.dunno.blame.prototypes.AAnimation;
import su.msk.dunno.blame.prototypes.AObject;

public class Moving extends AAnimation 
{
	Point pFrom, pTo;
	Point dir;
	AObject ao;
	
	public Moving(Field field, AObject ao, Point p_from, Point p_to) 
	{
		super(field, false);
		
		this.ao = ao;
		ao.preventDraw = true;
		HashMap<String, Integer> args = new HashMap<String, Integer>();
		args.put("MovingStarted", 1);
		ao.changeState(args);
		pFrom = p_from;
		pTo = p_to;

		dir = pTo.minus(pFrom);
				
		duration = Blame.fps/4;
		frames = duration;
		
		if((Blame.playCibo?"Cibo":"Killy").equals(ao.getName()))
		{
			field.isPlayerMoving = true;
			field.playerMovingCoord = new Vector2D(pFrom.x*Blame.scale, pFrom.y*Blame.scale);
		}
	}

	@Override public void nextFrame()
	{
		MyFont.instance().drawChar(ao.getSymbol(), 
								   pFrom.x*Blame.scale+dir.x*cur_frame*Blame.scale/frames, 
								   pFrom.y*Blame.scale+dir.y*cur_frame*Blame.scale/frames, 
								   Blame.scale*0.01f, ao.getColor());
		if((Blame.playCibo?"Cibo":"Killy").equals(ao.getName()))
		{
			field.playerMovingCoord = new Vector2D(pFrom.x*Blame.scale+dir.x*cur_frame*Blame.scale/frames, 
					   							   pFrom.y*Blame.scale+dir.y*cur_frame*Blame.scale/frames);
		}
		
	}
	
	@Override public void stop()
	{
		ao.preventDraw = false;
		HashMap<String, Integer> args = new HashMap<String, Integer>();
		args.put("MovingEnded", 1);
		ao.changeState(args);
		if((Blame.playCibo?"Cibo":"Killy").equals(ao.getName()))field.isPlayerMoving = false;
	}
}
