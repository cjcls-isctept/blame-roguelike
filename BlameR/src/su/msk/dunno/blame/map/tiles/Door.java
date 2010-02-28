package su.msk.dunno.blame.map.tiles;

import java.util.HashMap;

import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.MyFont;


public class Door extends AObject 
{
	boolean isOpen;
	
	public Door(int i, int j) 
	{
		super(i, j);
	}

	@Override public String getName()
	{
		if(isOpen) return "Open door";
		else return "Close door";
	}

	@Override public boolean getPassability()
	{
		return isOpen;
	}
	
	@Override public int getCode()
	{
		if(isOpen)return MyFont.DOOR_OPENED;
		else return MyFont.DOOR_CLOSED;
	}

	@Override public boolean getTransparency() 
	{
		return isOpen;
	}
	
	@Override public void changeState(ALiving changer, HashMap<String, String> args)
	{// maybe need to check for monster staying in door, but its seems not to throw any error now...
		if(args.containsKey("Open"))isOpen = true;
		if(args.containsKey("Close"))isOpen = false;
	}
}
