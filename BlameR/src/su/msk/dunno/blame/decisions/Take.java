package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.main.support.Messages;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;

public class Take extends ADecision
{
	Field field;
	
	public Take(ALiving al, Field field) 
	{
		super(al);
		this.field = field;
	}

	@Override public void doAction(int actionMoment) 
	{
		
		AObject item = al.getObjectsAtDir(Move.STAY).getLast();
		if(item.getState().containsKey("Item"))
		{
			if(!al.inventory.isFull())
			{
				if(field.removeObject(item))
				{
					al.inventory.addItem(item);
					if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" picks up "+item.getName()+" from the floor");
				}
				else if(al.isNearPlayer())Messages.instance().addMessage(item.getName()+" doesn't lie on the floor!");
				
			}
			else if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+"'s inventory is full");
		}
		wasExecuted = true;
	}
}
