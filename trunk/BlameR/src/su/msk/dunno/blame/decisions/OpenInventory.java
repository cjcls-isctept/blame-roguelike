package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.screens.Inventory;

public class OpenInventory extends ADecision 
{	
	public OpenInventory(ALiving al) 
	{
		super(al);
	}

	@Override public void doAction(int actionMoment) 
	{
		al.inventory.process();
		wasExecuted = true;
	}
}
