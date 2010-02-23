package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Messages;


public class Open extends ADecision 
{
	public Open(ALiving al) 
	{
		super(al);
		args.put("Open", "");
	}

	@Override public void doAction(int actionMoment) 
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
		wasExecuted = true;
	}
}
