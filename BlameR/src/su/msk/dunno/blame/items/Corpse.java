package su.msk.dunno.blame.items;

import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AItem;


public class Corpse extends AItem 
{
	public Corpse(Point p) 
	{
		super(p);
		state.put("HealthPlus", 10);
	}

	@Override public String getName() 
	{
		return "CorpseLand";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public char getSymbol() 
	{
		return '%';
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
}
