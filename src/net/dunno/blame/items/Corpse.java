package net.dunno.blame.items;

import java.util.HashMap;

import net.dunno.blame.main.support.Point;
import net.dunno.blame.prototypes.AItem;

public class Corpse extends AItem 
{
	public Corpse(Point p) 
	{
		super(p);
	}

	@Override public String getName() 
	{
		return "Corpse";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}

	@Override public char getSymbol() 
	{
		return '%';
	}

	@Override public boolean getTransparency() 
	{
		return true;
	}

	@Override public HashMap<String, Integer> getState() 
	{
		HashMap<String, Integer> state = new HashMap<String, Integer>();
		state.put("Health", 10);
		return state;
	}
}
