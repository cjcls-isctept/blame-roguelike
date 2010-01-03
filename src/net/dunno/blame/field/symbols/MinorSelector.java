package net.dunno.blame.field.symbols;

import net.dunno.blame.main.support.Point;
import net.dunno.blame.prototypes.AObject;

public class MinorSelector extends AObject 
{
	public MinorSelector(Point p) 
	{
		super(p);
	}

	@Override public boolean isEnemy() 
	{
		return false;
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
		return false;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
}
