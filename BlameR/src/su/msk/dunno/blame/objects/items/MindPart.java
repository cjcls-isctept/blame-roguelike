package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AItem;

public class MindPart extends AItem 
{

	public MindPart(Point p) 
	{
		super(p);
		item_properties.put("Part", "");
		item_properties.put("Info", "Allows to enter to target's mind");
		item_properties.put("MindHack", "");
	}

	@Override public String getName() 
	{
		return "MindHack";
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
		return MyFont.IMP;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
	
	@Override public Color getColor()
	{
		return Color.CORNFLOWER;
	}
}
