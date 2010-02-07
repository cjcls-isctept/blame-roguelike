package su.msk.dunno.blame.prototypes;

import java.util.HashMap;

import su.msk.dunno.blame.main.support.Point;


public abstract class AItem extends AObject
{
	protected HashMap<String, String> state = new HashMap<String, String>();
	
	public AItem(Point p) 
	{
		super(p);
		state.put("Item", "");
	}
	
	@Override public HashMap<String, String> getState()
	{
		return state;
	}
}
