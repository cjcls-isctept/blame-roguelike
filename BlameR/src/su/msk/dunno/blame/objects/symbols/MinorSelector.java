package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AObject;

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

	@Override public char getSymbol() 
	{
		return 'x';
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
