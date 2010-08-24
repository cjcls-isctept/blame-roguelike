package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class ImpMind extends AItem 
{

	public ImpMind(Point p) 
	{
		super(p);
		item_properties.put("Imp");
		item_properties.putString("Info", "Allows to enter to target's mind");
		item_properties.putString("Effect", "MindHack");
		item_properties.putFloat("MindHack", 1);
	}

	@Override public String getName() 
	{
		return "MindHack";
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
		return Color.CORNFLOWER;
	}
}
