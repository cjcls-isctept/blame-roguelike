package net.dunno.blame.field.tiles;

import net.dunno.blame.prototypes.AObject;

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

	@Override public boolean getTransparency()
	{
		return true;
	}

	@Override public String getName() 
	{
		return "Floor";
	}
}
