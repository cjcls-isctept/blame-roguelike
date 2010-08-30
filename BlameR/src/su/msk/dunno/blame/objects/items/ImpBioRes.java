package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class ImpBioRes extends AItem 
{
	public ImpBioRes(Point p) 
	{
		super(p);
		item_properties.put("Imp");
		item_properties.putString("Info", "Adds resist to bio damage");
		item_properties.putString("Effect", "BioResist");
		item_properties.putFloat("BioResist", 3);
	}

	@Override public String getName() 
	{
		return "BioRes Imp";
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
