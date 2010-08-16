package su.msk.dunno.blame.map.tiles;

import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.MyFont;

public class Floor extends AObject 
{
	public Floor(int i, int j) 
	{
		super(i, j);
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getSymbol()
	{
		return MyFont.FLOOR;
	}

	@Override public boolean getTransparency()
	{
		return true;
	}

	@Override public String getName() 
	{
		return "Floor";
	}
}
