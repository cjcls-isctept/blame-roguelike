package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class MindPart extends AItem 
{

	public MindPart(Point p) 
	{
		super(p);
		item_properties.put("Part", "");
		item_properties.put("Info", "Allows to enter to target's mind");
		item_properties.put("EffectsCapacity", "1");
		item_properties.put("Effect1", "MindHack");
		item_properties.put("MindHack", "1");
	}

	@Override public String getName() 
	{
		return "MindHack";
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
		return Color.CORNFLOWER;
	}
}
