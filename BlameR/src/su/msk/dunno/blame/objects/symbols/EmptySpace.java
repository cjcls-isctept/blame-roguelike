package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AObject;

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

	@Override public char getSymbol() 
	{
		return ' ';
	}

	@Override public boolean getTransparency()	
	{
		return true;
	}
}
