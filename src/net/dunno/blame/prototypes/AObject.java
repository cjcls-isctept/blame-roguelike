package net.dunno.blame.prototypes;

import java.util.HashMap;

import net.dunno.blame.main.support.Color;
import net.dunno.blame.main.support.Point;

public abstract class AObject 
{
	public Point cur_pos;	// current position
	public boolean wasDrawed;
	protected int dov;	// dov = depth of vision
	
	public AObject(int i, int j)
	{
		this(new Point(i, j));
	}
	
	public AObject(Point p)
	{
		cur_pos = p;
	}
	
	public abstract char getSymbol();
	
	public abstract boolean getTransparency();
	
	public abstract boolean getPassability();
	
	public abstract String getName();

	public Color getColor() 
	{
		return Color.WHITE;
	}
	
	public void changeState(HashMap<String, Integer> args)
	{
		
	}
	
	public HashMap<String, Integer> getState()
	{
		return new HashMap<String, Integer>();
	}
	
	public boolean isEnemy() 
	{
		return false;
	}
	
	public boolean isLightSource()
	{
		return false;
	}

	public int getDov()	// dov = depth of vision
	{
		return dov;
	}
}
