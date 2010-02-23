package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class EmptySpace extends AObject
{

	public EmptySpace(int i, int j) 
	{
		super(i, j);
	}

	public EmptySpace(Point cur_pos) 
	{
		super(cur_pos);
	}

	@Override public String getName() 
	{
		return "Empty";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getCode()
	{
		return MyFont.EMPTY;
	}

	@Override public boolean getTransparency()	
	{
		return true;
	}
}
