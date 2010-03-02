package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Messages;

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
					if(al.isNearPlayer())Messages.instance().addPropMessage("decision.take", al.getName(), item.getName());
				}
				else if(al.isNearPlayer())Messages.instance().addPropMessage("decision.take.wrongitem", item.getName());
				
			}
			else if(al.isNearPlayer())Messages.instance().addPropMessage("decision.take.inventoryfull", al.getName());
		}
		else if(al.isNearPlayer())Messages.instance().addPropMessage("decision.take.noitem");
		wasExecuted = true;
	}
}
