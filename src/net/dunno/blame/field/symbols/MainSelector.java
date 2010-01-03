package net.dunno.blame.field.symbols;

import net.dunno.blame.main.support.Point;

public class MainSelector extends MinorSelector 
{
	public MainSelector(Point p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "MainSelector";
	}

	@Override
	public char getSymbol() {
		// TODO Auto-generated method stub
		return 'X';
	}
}
