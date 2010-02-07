package su.msk.dunno.blame.prototypes;

import java.util.HashMap;

public abstract class ADecision 
{
	protected ALiving al;
	protected HashMap<String, String> args = new HashMap<String, String>();
	protected boolean wasExecuted;
	private int actionPeriod;
	
	public ADecision(ALiving al)
	{
		this.al = al;
	}
	
	public abstract void doAction(int actionMoment);
	
	public boolean wasExecuted()
	{
		return wasExecuted;
	}

	public int getActionPeriod()
	{
		return al.getStat("Speed");
	}
}
