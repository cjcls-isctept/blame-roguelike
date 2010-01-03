package net.dunno.blame.prototypes;

import java.util.HashMap;

public abstract class ADecision 
{
	protected ALiving al;
	protected HashMap<String, Integer> args;
	
	public ADecision(ALiving al)
	{
		this.al = al;
		args = new HashMap<String, Integer>();
	}
	
	public abstract void doAction();
}
