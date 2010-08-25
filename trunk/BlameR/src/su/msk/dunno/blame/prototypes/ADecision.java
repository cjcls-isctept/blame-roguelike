package su.msk.dunno.blame.prototypes;

import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;

public abstract class ADecision 
{
	protected ALiving al;
	protected StateMap args = new StateMap();
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
		return (int)al.getStat("Speed");
	}

	public void setSelectPoint(Point selectPoint) 
	{
		
	}
}
