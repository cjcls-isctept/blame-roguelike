package su.msk.dunno.blame.prototypes;

import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;


public abstract class AObject 
{
	public Point cur_pos;	// current position
	
	public boolean wasDrawed = true;
	private int preventDrawRequests;
	
	protected int dov;	// dov = depth of vision
	
	public AObject(int i, int j)
	{
		this(new Point(i, j));
	}
	
	public AObject(Point p)
	{
		cur_pos = p;
	}
	
	public abstract int getCode();
	
	public abstract boolean getTransparency();
	
	public abstract boolean getPassability();
	
	public abstract String getName();

	public Color getColor() 
	{
		return Color.WHITE;
	}
	
	public void changeState(ALiving changer, StateMap args)
	{
		
	}
	
	public StateMap getState()
	{
		return new StateMap();
	}
	
	public boolean isEnemy(AObject ao)
	{
		return false;
	}
	
	public boolean isLightSource()	// if true dov must be more than 0 to effect take place!
	{
		return false;
	}

	public int getDov()	// dov = depth of vision
	{
		return dov;
	}
	
	public boolean isAlwaysDraw()
	{
		return false;
	}
	
	public void preventDraw()
	{
		preventDrawRequests++;
	}
	
	public void allowDraw()
	{
		if(preventDrawRequests > 0)preventDrawRequests--;
	}
	
	public boolean isDrawPrevented()
	{
		return preventDrawRequests > 0;
	}
	
/*	public boolean equals(AObject ao)
	{
		return cur_pos.equals(ao.cur_pos);
	}*/
}
