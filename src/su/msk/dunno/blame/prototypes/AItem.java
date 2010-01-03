package su.msk.dunno.blame.prototypes;

import java.util.HashMap;

import su.msk.dunno.blame.main.support.Point;


public abstract class AItem extends AObject
{
	public AItem(Point p) 
	{
		super(p);
	}
	
	public abstract HashMap<String, Integer> getState();
}
