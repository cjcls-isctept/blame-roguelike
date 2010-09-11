package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Point;

public class EmitterBullet extends Bullet 
{
	public EmitterBullet(Point p) 
	{
		super(p, Color.WHITE);
		setStat("Dov", 7);
	}
	
	@Override public Color getColor()
	{
		return Color.WHITE;
	}
}
