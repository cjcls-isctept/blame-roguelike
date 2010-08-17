package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.screens.HelpScreen;

public class OpenHelpScreen extends ADecision 
{
	public OpenHelpScreen(ALiving al) 
	{
		super(al);
	}

	@Override public void doAction(int actionMoment) 
	{
		HelpScreen.instance().process();
		wasExecuted = true;
	}
}
