package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class MinorSelector extends AObject 
{
	public MinorSelector(Point p) 
	{
		super(p);
	}

	public MinorSelector(int i, int j) 
	{
		super(i, j);
	}

	@Override public String getName() 
	{
		return "MinorSelecter";
	}
	
	@Override public int getSymbol()
	{
		return MyFont.MINORSELECTOR;
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
	
	@Override public boolean isAlwaysDraw()
	{
		return true;
	}
	
/*	@Override public Color getColor() 
	{
		return Color.CYAN;
	}*/
}
