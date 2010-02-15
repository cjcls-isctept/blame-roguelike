package su.msk.dunno.blame.prototypes;

import java.util.HashMap;

import su.msk.dunno.blame.main.support.Point;


public abstract class AItem extends AObject
{
	protected HashMap<String, String> item_properties = new HashMap<String, String>();
	
	public AItem(Point p) 
	{
		super(p);
		item_properties.put("Item", "");
	}
	
	@Override public HashMap<String, String> getState()
	{
		return item_properties;
	}
}
