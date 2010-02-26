package su.msk.dunno.blame.objects.livings;

import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.Livings;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Point;

public class Cibo extends Killy 
{
	public Cibo(Point p, Field field) 
	{
		super(p, field);
		infection_level = 90;
	}

	@Override public String getName() 
	{
		return "Cibo";
	}

	@Override public Color getColor() 
	{
		/*if(isDead) return Color.WHITE;
		else */return Color.BLUE;
	}
}
