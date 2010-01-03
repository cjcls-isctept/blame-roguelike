package net.dunno.blame.decisions;

import java.util.LinkedList;

import net.dunno.blame.main.support.Messages;
import net.dunno.blame.prototypes.ADecision;
import net.dunno.blame.prototypes.ALiving;
import net.dunno.blame.prototypes.AObject;

public class Open extends ADecision 
{
	public Open(ALiving al) 
	{
		super(al);
		args.put("Open", 1);
	}

	@Override public void doAction() 
	{
		LinkedList<AObject> neighbours = al.getMyNearestNeighbours();
		for(AObject ao: neighbours)
		{
			if("Close door".equals(ao.getName()))
			{
				ao.changeState(args);
				if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" opens a door");
			}
		}
	}
}
