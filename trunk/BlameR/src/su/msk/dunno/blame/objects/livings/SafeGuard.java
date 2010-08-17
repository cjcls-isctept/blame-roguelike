package su.msk.dunno.blame.objects.livings;

import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class SafeGuard extends SiliconCreature 
{

	public SafeGuard(Point p, Field field) 
	{
		super(p, field);
		dov = 5;
	}
	
	@Override protected void initStats() 
	{
		setStat("Health", 20);
		setStat("Speed", 3);
	}

	@Override public Color getColor() 
	{
		return Color.CORNFLOWER;
	}

	@Override public ADecision livingAI() 
	{
		return null;
	}

	@Override public int getSymbol() 
	{
		return MyFont.SAFEGUARD;
	}

	@Override public String getName() 
	{
		return "SafeGuard";
	}
}
