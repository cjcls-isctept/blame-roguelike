package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class ImpHealth extends AItem
{

	public ImpHealth(Point p) 
	{
		super(p);
		item_properties.put("Imp");
		item_properties.putString("Info", "Increase amount of health");
		item_properties.putString("Modifier", "Health");
		item_properties.putFloat("Health", 20);
	}

	@Override public int getSymbol() 
	{
		return MyFont.IMP;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public String getName() 
	{
		return "Health";
	}

	@Override public Color getColor()
	{
		return Color.CREAM;
	}
}
