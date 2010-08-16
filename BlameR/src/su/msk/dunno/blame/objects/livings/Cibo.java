package su.msk.dunno.blame.objects.livings;

import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.items.ImpKick;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Point;

public class Cibo extends Killy 
{
	public Cibo(Point p, Field field) 
	{
		super(p, field);
		//infection_level = 90;
		inventory.addItem(new ImpKick(new Point()));
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
