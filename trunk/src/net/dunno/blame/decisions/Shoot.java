package net.dunno.blame.decisions;

import net.dunno.blame.main.support.Messages;
import net.dunno.blame.main.support.Point;
import net.dunno.blame.prototypes.ADecision;
import net.dunno.blame.prototypes.ALiving;
import net.dunno.blame.prototypes.AObject;

public class Shoot extends ADecision 
{
	Point shootTo;
	
	public Shoot(ALiving al, Point selectPoint) 
	{
		super(al);
		shootTo = selectPoint;
		args.put("Damage", (int)(Math.random()*20));
	}

	@Override public void doAction() 
	{
		for(AObject ao: al.getMyNeighbours())
		{
			if(ao.cur_pos.equals(shootTo) && ao.isEnemy())
			{
				ao.changeState(args);
				if(al.isNearPlayer())
				{
					Messages.instance().addMessage(al.getName()+" deals "+args.get("Damage")+" damage to "+ao.getName());
				}
			}
		}
	}
}
