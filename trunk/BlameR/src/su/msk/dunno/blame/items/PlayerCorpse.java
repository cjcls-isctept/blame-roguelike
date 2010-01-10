package su.msk.dunno.blame.items;

import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AItem;

public class PlayerCorpse extends AItem 
{
	public PlayerCorpse(Point p) 
	{
		super(p);
	}

	@Override public String getName() 
	{
		return "Player Corpse";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public char getSymbol() 
	{
		return '@';
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
}
