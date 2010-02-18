package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AItem;

public class FirePart extends AItem
{
	public FirePart(Point p) 
	{
		super(p);
		item_properties.put("Part", "");
		item_properties.put("Info", "Adds fire damage to weapon");
		item_properties.put("Fire", "");
	}

	@Override public String getName() 
	{
		return "Fire Imp";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public char getSymbol() 
	{
		return '*';
	}
	
	@Override public int getCode()
	{
		return MyFont.IMP;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}

	@Override public Color getColor()
	{
		return Color.RED;
	}
}
