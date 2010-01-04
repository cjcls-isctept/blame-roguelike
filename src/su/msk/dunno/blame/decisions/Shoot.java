package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import su.msk.dunno.blame.animations.BulletFlight;
import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.main.support.Messages;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;

public class Shoot extends ADecision 
{
	Point shootTo;
	Field field;
	
	public Shoot(ALiving al, Point selectPoint, Field field) 
	{
		super(al);
		this.field = field;
		shootTo = selectPoint;
		args.put("Damage", (int)(Math.random()*20));
	}

	@Override public void doAction() 
	{
		for(AObject ao: field.getObjectsAtPoint(shootTo))
		{
			if(ao.isEnemy())
			{
				ao.changeState(args);
				if(al.isNearPlayer())
				{
					Messages.instance().addMessage(al.getName()+" deals "+args.get("Damage")+" damage to "+ao.getName());
				}
			}
		}
		// kickback
		LinkedList<Point> line = field.getLine(al.cur_pos, shootTo);
		if(line.size() > 1)
		{
			// animation
			field.playAnimation(new BulletFlight(line.get(1), shootTo, field, false));
			al.cur_pos = al.cur_pos.mul(2).minus(line.get(1));
			field.changeLocation(al);
		}
	}
}
