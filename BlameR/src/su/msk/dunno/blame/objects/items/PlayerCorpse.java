package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AItem;

public class PlayerCorpse extends AItem 
{
	String name;
	
	public PlayerCorpse(String name, Point p) 
	{
		super(p);
		this.name = name;
		item_properties.put("Info", name+"'s genetic information. "+name+" can be restored in rebuild station.");
	}

	@Override public String getName() 
	{
		return name+"'s genetic data";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public char getSymbol() 
	{
		return '@';
	}
	
	@Override public int getCode()
	{
		return MyFont.PLAYER;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
}
