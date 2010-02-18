package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.AObject;

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

	@Override public char getSymbol() 
	{
		return 'w';
	}
	
	@Override public int getCode()
	{
		return MyFont.WEAPONBASE;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}

}
