package su.msk.dunno.blame.livings;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.containers.LivingList;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Point;

public class Cibo extends Killy 
{
	public Cibo(Point p, Field field, LivingList livings) 
	{
		super(p, field, livings);
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
