package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import su.msk.dunno.blame.main.support.Messages;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;


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
