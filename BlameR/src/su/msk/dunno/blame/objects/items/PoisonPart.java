package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class PoisonPart extends AItem 
{
	public PoisonPart(Point p) 
	{
		super(p);
		item_properties.put("Part");
		item_properties.putString("Info", "Adds poison damage to weapon (damage +3)");
		item_properties.putInt("EffectsCapacity", 1);
		item_properties.putString("Effect1", "Damage");
		item_properties.putFloat("Damage", 3);
	}

	@Override public String getName() 
	{
		return "Poison";
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
		return Color.GREEN;
	}
}