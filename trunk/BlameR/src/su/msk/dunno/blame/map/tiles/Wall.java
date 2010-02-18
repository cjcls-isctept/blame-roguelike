package su.msk.dunno.blame.map.tiles;

import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.prototypes.AObject;

public class Wall extends AObject
{
	public Wall(int i, int j) 
	{
		super(i, j);
	}

	@Override public String getName() 
	{
		return "Wall";
	}

	@Override public boolean getPassability() 
	{
		return false;
	}

	@Override public char getSymbol() 
	{
		return '#';
	}
	
	@Override public int getCode()
	{
		return MyFont.WALL;
	}

	@Override public boolean getTransparency() 
	{
		return false;
	}

}
