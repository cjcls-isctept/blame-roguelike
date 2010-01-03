package net.dunno.blame.decisions;

import net.dunno.blame.main.support.Messages;
import net.dunno.blame.prototypes.ADecision;
import net.dunno.blame.prototypes.ALiving;
import net.dunno.blame.prototypes.AObject;

public class Take extends ADecision
{
	public Take(ALiving al) 
	{
		super(al);
	}

	@Override public void doAction() 
	{
		AObject item = al.getObjectsAtDir(Move.STAY).getLast();
		if(!"Floor".equals(item.getName()))	// override this! not only the floor could be under legs
		{
			al.changeState(item.getState());
			al.removeObject(item);
			if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" picks up "+item.getName()+" from the floor");
		}
	}
}
