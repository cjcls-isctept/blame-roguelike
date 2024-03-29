package su.msk.dunno.blame.objects.items;

import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;


public class SiliconCorpse extends AObject 
{
	public SiliconCorpse(Point p) 
	{
		super(p);
	}
	
	@Override public StateMap getState()
	{
		return new StateMap("OnFloor");
	}

	@Override public String getName() 
	{
		return "Corpse";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getSymbol()
	{
		return MyFont.CORPSE;
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}
}
