package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class ImpLaser extends AItem
{
	public ImpLaser(Point p) 
	{
		super(p);
		item_properties.put("Imp");
		item_properties.putString("Info", "Adds fire damage to weapon (damage +2)");
		item_properties.putString("Effect", "FireDamage");
		item_properties.putFloat("FireDamage", 2);
	}

	@Override public String getName() 
	{
		return "Fire Imp";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getSymbol()
	{
		return MyFont.IMP;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}

	@Override public Color getColor()
	{
		return Color.PURPLE;
	}
}
