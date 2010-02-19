package su.msk.dunno.blame.objects.buildings;

import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;

public class RebuildStation extends ALiving 
{

	public RebuildStation(int i, int j, Field field) 
	{
		super(i, j, field);
	}

	@Override public Color getColor() 
	{
		return Color.RED;
	}

	@Override public boolean isEnemy(AObject ao) 
	{
		return false;
	}

	@Override public boolean isPlayer() 
	{
		return false;
	}

	@Override public ADecision livingAI() 
	{
		return null;
	}

	@Override public String getName() 
	{
		return "Rebuild Station";
	}

	@Override public char getSymbol() 
	{
		return 'A';
	}

	@Override public int getCode()
	{
		return MyFont.STATION;
	}
}
