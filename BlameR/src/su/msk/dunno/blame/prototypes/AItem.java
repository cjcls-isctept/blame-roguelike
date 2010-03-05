package su.msk.dunno.blame.prototypes;

import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;


public abstract class AItem extends AObject
{
	protected StateMap item_properties = new StateMap();
	
	public AItem(Point p) 
	{
		super(p);
		item_properties.put("Item");
	}
	
	@Override public StateMap getState()
	{
		return item_properties;
	}
}
