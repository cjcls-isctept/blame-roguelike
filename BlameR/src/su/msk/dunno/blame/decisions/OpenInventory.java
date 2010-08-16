package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.screens.Inventory;

public class OpenInventory extends ADecision 
{
	Inventory inventory;
	
	public OpenInventory(ALiving al, Inventory inv) 
	{
		super(al);
		inventory = inv;
	}

	@Override public void doAction(int actionMoment) 
	{
		inventory.process();
		wasExecuted = true;
	}
}
