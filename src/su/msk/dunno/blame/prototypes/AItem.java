package su.msk.dunno.blame.prototypes;

import java.util.HashMap;

import su.msk.dunno.blame.main.support.Point;


public abstract class AItem extends AObject
{
	protected HashMap<String, Integer> state;
	
	public AItem(Point p) 
	{
		super(p);
		state = new HashMap<String, Integer>();
		state.put("Item", 1);
	}
	
	public HashMap<String, Integer> getState()
	{
		return state;
	}
}
