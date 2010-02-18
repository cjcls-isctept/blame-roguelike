package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AObject;

public class Bullet extends AObject
{
	public Bullet(Point p) 
	{
		super(p);
		dov = 3;
	}

	@Override public String getName() 
	{
		return "Bullet";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public char getSymbol() 
	{
		return '*';
	}
	
	@Override public int getCode()
	{
		return MyFont.BULLET;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}

	@Override public Color getColor()
	{
		return Color.YELLOW;
	}
	
	@Override public boolean isLightSource()
	{
		return true;
	}
}
