package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class LightningPart extends AItem 
{
	public LightningPart(Point p) 
	{
		super(p);
		item_properties.put("Part", "");
		item_properties.put("Info", "Adds lightning damage to weapon");
		item_properties.put("EffectsCapacity", "1");
		item_properties.put("Effect1", "Energy");
		item_properties.put("Energy", "10");
	}

	@Override public String getName() 
	{
		return "Light";
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
		return Color.YELLOW;
	}
}
