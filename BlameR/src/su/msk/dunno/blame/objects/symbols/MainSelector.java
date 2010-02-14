package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.main.support.Point;

public class MainSelector extends MinorSelector 
{
	public MainSelector(Point p) 
	{
		super(p);
	}

	@Override public String getName() 
	{
		return "MainSelector";
	}

	@Override public char getSymbol() 
	{
		return 'X';
	}
}
