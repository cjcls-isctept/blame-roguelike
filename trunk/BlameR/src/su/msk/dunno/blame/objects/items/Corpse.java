package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;


public class Corpse extends AItem 
{
	public Corpse(Point p) 
	{
		super(p);
		item_properties.put("Info", "Piece of silicon shit");
	}

	@Override public String getName() 
	{
		return "Corpse";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getCode()
	{
		return MyFont.CORPSE;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
}
