package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class ImpKick extends AItem 
{
	 	public ImpKick(Point p) 
	 	{
	 		super(p);
	 		item_properties.put("Imp");
	 		item_properties.putString("Info", "Kicks enemies back!");
	 		item_properties.putString("Effect", "Kick");
	 		item_properties.putFloat("Kick", 1);
	 	}

	 	@Override public String getName() 
	 	{
	 		return "Kick";
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
	 		return Color.BLUE;
	 	}
}
