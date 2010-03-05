package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AItem;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class PlayerCorpse extends AItem 
{
	String name;
	
	public PlayerCorpse(String name, Point p) 
	{
		super(p);
		this.name = name;
		item_properties.putString("Info", name+"'s genetic information. "+name+" can be restored in rebuild station.");
	}

	@Override public String getName() 
	{
		return name+"'s genetic data";
	}

	@Override public boolean getPassability() 
	{
		return true;
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
