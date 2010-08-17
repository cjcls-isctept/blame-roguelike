package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;

public class OpenInventory extends ADecision 
{	
	public OpenInventory(ALiving al) 
	{
		super(al);
	}

	@Override public void doAction(int actionMoment) 
	{
		al.getInventory().process();
		wasExecuted = true;
	}
}
