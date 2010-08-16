package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

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
	
	@Override public int getSymbol()
	{
		return MyFont.MAINSELECTOR;
	}
}
