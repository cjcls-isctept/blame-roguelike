package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.support.Messages;

public class DoNothing extends ADecision 
{

	public DoNothing(ALiving al) 
	{
		super(al);
	}

	@Override public void doAction(int actionMoment) 
	{
		Messages.instance().addPropMessage("decision.nothing", al.getName());
		wasExecuted = true;
	}
	
	@Override public int getActionPeriod()
	{
		return 0;
	}
}
