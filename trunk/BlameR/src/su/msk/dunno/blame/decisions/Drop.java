package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.main.support.Messages;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;

public class Drop extends ADecision
{
	AObject toDrop;
	
	public Drop(ALiving al, AObject item) 
	{
		super(al);
		toDrop = item;
	}

	@Override public void doAction(int actionMoment) 
	{
		al.inventory.removeItem(toDrop);
		if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" drops "+toDrop.getName()+" to the floor");
	}
}
