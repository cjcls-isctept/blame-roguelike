package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;

public class DoNothing extends ADecision 
{

	public DoNothing(ALiving al) 
	{
		super(al);
	}

	@Override public void doAction(int actionMoment) 
	{
		Messages.instance().addMessage(al.getName()+" does nothing succefully");
		wasExecuted = true;
	}
	
	@Override public int getActionPeriod()
	{
		return 0;
	}
}
