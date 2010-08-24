package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class ImpSocketExtender extends AItem 
{

	public ImpSocketExtender(Point p) 
	{
		super(p);
		item_properties.put("Imp");
		item_properties.putString("Info", "Adds new socket places to weapon");
		item_properties.putFloat("Extender", 1);
	}

	@Override public String getName() 
	{
		return "Socket Extender";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getSymbol()
	{
		return MyFont.WEAPONBASE;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
}
