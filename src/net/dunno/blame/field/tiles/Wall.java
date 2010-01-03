package net.dunno.blame.field.tiles;

import net.dunno.blame.prototypes.AObject;

public class Wall extends AObject
{
	public Wall(int i, int j) 
	{
		super(i, j);
	}

	@Override
	public String getName() 
	{
		return "Wall";
	}

	@Override
	public boolean getPassability() 
	{
		return false;
	}

	@Override
	public char getSymbol() 
	{
		return '#';
	}

	@Override
	public boolean getTransparency() 
	{
		return false;
	}

}
