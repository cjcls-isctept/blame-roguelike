package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AItem;

public class PoisonPart extends AItem 
{
	public PoisonPart(Point p) 
	{
		super(p);
		item_properties.put("Part", "");
		item_properties.put("Info", "Adds poison damage to weapon");
		item_properties.put("Poison", "");
	}

	@Override public String getName() 
	{
		return "Poison";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public char getSymbol() 
	{
		return '*';
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