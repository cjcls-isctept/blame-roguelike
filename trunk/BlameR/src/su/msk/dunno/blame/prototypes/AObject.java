package su.msk.dunno.blame.prototypes;

import java.util.HashMap;

import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;


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
	
	public void changeState(HashMap<String, String> args)
	{
		
	}
	
	public HashMap<String, String> getState()
	{
		return new HashMap<String, String>();
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
