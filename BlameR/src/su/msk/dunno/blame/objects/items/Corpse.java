package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AItem;


public class Corpse extends AItem 
{
	public Corpse(Point p) 
	{
		super(p);
		state.put("Info", "Piece of silicon shit");
	}

	@Override public String getName() 
	{
		return "Corpse";
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
