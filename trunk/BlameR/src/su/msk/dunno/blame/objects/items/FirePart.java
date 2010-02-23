package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class FirePart extends AItem
{
	public FirePart(Point p) 
	{
		super(p);
		item_properties.put("Part", "");
		item_properties.put("Info", "Adds fire damage to weapon");
		item_properties.put("EffectsCapacity", "1");
		item_properties.put("Effect1", "Damage");
		item_properties.put("Damage", "2");
	}

	@Override public String getName() 
	{
		return "Fire Imp";
	}

	@Override public boolean getPassability() 
	{
		return true;
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
