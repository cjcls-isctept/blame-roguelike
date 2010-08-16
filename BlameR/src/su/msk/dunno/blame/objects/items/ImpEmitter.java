package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class ImpEmitter extends AItem
{

	public ImpEmitter(Point p) 
	{
		super(p);
		item_properties.put("Imp");
		item_properties.putString("Info", "Killy's emitter module");
		item_properties.putInt("EffectsCapacity", 1);
		item_properties.putString("Effect1", "Level2");
		item_properties.putFloat("Level2", 1);
	}

	@Override public int getSymbol() 
	{
		return MyFont.IMP;
	}

	@Override public String getName() 
	{
		return "Emitter";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
	
	@Override public Color getColor()
	{
		return Color.WHITE;
	}
}