package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Messages;


public class OpenDoor extends ADecision 
{
	public OpenDoor(ALiving al) 
	{
		super(al);
		args.put("Open");
	}

	@Override public void doAction(int actionMoment) 
	{
		LinkedList<AObject> neighbours = al.getMyNearestNeighbours();
		for(AObject ao: neighbours)
		{
			if("Close door".equals(ao.getName()))	// rewrite this!
			{
				ao.changeState(al, args);
				if(al.isNearPlayer())Messages.instance().addPropMessage("decision.open", al.getName());
			}
		}
		wasExecuted = true;
	}
}
