package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class WeaponBase extends AObject 
{

	public WeaponBase(Point p) 
	{
		super(p);
	}

	public WeaponBase(int i, int j) 
	{
		super(i, j);
	}

	@Override public String getName() 
	{
		return "Weapon Sceleton";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getSymbol()
	{
		return MyFont.WEAPONBASE;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}

}
