package su.msk.dunno.blame.livings;

import su.msk.dunno.blame.field.Field;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Point;

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
