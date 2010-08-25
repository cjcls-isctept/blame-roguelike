package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class ImpElectro extends AItem 
{
	public ImpElectro(Point p) 
	{
		super(p);
		
		item_properties.put("Imp");
		item_properties.putString("Info", "Adds electric damage");
		item_properties.putString("Effect", "ElectroDamage");
		item_properties.putFloat("ElectroDamage", 3);
	}

	@Override public String getName() 
	{
		return "Electricity Imp";
	}
	
	@Override public int getSymbol() 
	{
		return MyFont.IMP;
	}

	@Override public boolean getPassability() 
	{
		return true;
	}	

	@Override public boolean getTransparency() 
	{
		return true;
	}
	
	@Override public Color getColor()
	{
		return Color.BLUE_VIOLET;
	}
}
