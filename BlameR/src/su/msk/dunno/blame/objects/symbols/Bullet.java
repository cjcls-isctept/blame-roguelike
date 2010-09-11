package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class Bullet extends AObject
{
	private Color color;
	public Bullet(Point p, Color c) 
	{
		super(p);
		color = c;
		setStat("Dov", 3);
	}

	@Override public String getName() 
	{
		return "Bullet";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getSymbol()
	{
		return MyFont.BULLET;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}

	@Override public Color getColor()
	{
		return color;
	}
	
	@Override public boolean isLightSource()
	{
		return true;
	}
}
