package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class SocketExtender extends AItem 
{

	public SocketExtender(Point p) 
	{
		super(p);
		item_properties.put("Part", "");
		item_properties.put("Info", "Adds new socket places to weapon");
		item_properties.put("EffectsCapacity", "1");
		item_properties.put("Effect1", "Extender");
		item_properties.put("Extender", "1");
	}

	@Override public String getName() 
	{
		return "Socket Extender";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getCode()
	{
		return MyFont.WEAPONBASE;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
}
