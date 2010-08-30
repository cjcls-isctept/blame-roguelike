package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class ImpLaserRes extends AItem 
{
	public ImpLaserRes(Point p) 
	{
		super(p);
		item_properties.put("Imp");
		item_properties.putString("Info", "Adds resist to laser damage");
		item_properties.putString("Effect", "LaserResist");
		item_properties.putFloat("LaserResist", 3);
	}

	@Override public String getName() 
	{
		return "LaserRes Imp";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getSymbol()
	{
		return MyFont.IMP;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
	
	@Override public Color getColor()
	{
		return Color.CYAN;
	}
}
