package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class ImpEnergy extends AItem 
{
	public ImpEnergy(Point p) 
	{
		super(p);
		item_properties.put("Imp");
		item_properties.putString("Info", "Energy capacitor for weapon (capacity +10)");
		item_properties.putString("Modifier", "Energy");
		item_properties.putFloat("Energy", 10);
	}

	@Override public String getName() 
	{
		return "Energy";
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
		return Color.YELLOW;
	}
}
