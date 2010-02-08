package su.msk.dunno.blame.items;

import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AItem;

public class PoisonPart extends AItem 
{
	public PoisonPart(Point p) 
	{
		super(p);
		state.put("Part", "");
		state.put("Info", "Adds poison damage to weapon");
		state.put("Poison", "");
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
		return 'w';
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