package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AItem;

public class ColdPart extends AItem 
{
	public ColdPart(Point p) 
	{
		super(p);
		state.put("Part", "");
		state.put("Info", "Adds cold damage to weapon");
		state.put("Cold", "");
	}

	@Override public String getName() 
	{
		return "Cold";
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
		return Color.CYAN;
	}
}