package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.main.support.Messages;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;

public class Take extends ADecision
{
	public Take(ALiving al) 
	{
		super(al);
	}

	@Override public void doAction(int actionMoment) 
	{
		AObject item = al.getObjectsAtDir(Move.STAY).getLast();
		if(item.getState().containsKey("Item"))
		{
			if(!al.inventory.isFull())
			{
				al.inventory.addItem(item);
			}
			else if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+"'s inventory is full");
			if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" picks up "+item.getName()+" from the floor");
		}
		wasExecuted = true;
	}
}
