package net.dunno.blame.livings;

import net.dunno.blame.field.Field;
import net.dunno.blame.main.support.Color;
import net.dunno.blame.main.support.Point;

public class Cibo extends Killy 
{
	public Cibo(Point p, Field field) 
	{
		super(p, field);
	}

	@Override public String getName() 
	{
		return "Cibo";
	}

	@Override public Color getColor() 
	{
		return Color.BLUE;
	}
}
