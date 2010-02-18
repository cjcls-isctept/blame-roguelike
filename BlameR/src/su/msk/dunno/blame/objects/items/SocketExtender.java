package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AItem;

public class SocketExtender extends AItem 
{

	public SocketExtender(Point p) 
	{
		super(p);
		item_properties.put("Part", "");
		item_properties.put("Info", "Adds new socket places to weapon");
		item_properties.put("Extender", "");
	}

	@Override public String getName() 
	{
		return "Socket Extender";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public char getSymbol() 
	{
		return 'w';
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
