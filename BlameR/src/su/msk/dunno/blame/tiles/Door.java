package su.msk.dunno.blame.tiles;

import java.util.HashMap;

import su.msk.dunno.blame.prototypes.AObject;


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

	@Override public char getSymbol() 
	{
		if(!isOpen)return '+';
		else return '\'';
	}

	@Override public boolean getTransparency() 
	{
		return isOpen;
	}
	
	@Override public void changeState(HashMap<String, String> args)
	{// maybe need to check for monster staying in door, but its seems not to throw any error now...
		if(args.containsKey("Open"))isOpen = true;
		if(args.containsKey("Close"))isOpen = false;
	}
}
