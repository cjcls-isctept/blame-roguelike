package su.msk.dunno.blame.map.tiles;

import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.prototypes.AObject;

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

	@Override public char getSymbol() 
	{
		return '.';
	}
	
	@Override public int getCode()
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
