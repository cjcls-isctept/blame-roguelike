package su.msk.dunno.blame.map.tiles;

import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.MyFont;

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
	
	@Override public int getSymbol()
	{
		return MyFont.WALL;
	}

	@Override public boolean getTransparency() 
	{
		return false;
	}

}
