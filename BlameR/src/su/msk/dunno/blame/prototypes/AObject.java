package su.msk.dunno.blame.prototypes;

import java.util.HashMap;

import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;


public abstract class AObject 
{
	public Point curPos;	// current position
	
	public boolean wasDrawed/* = true*/;
	private int preventDrawRequests;
	
	public AObject(int i, int j)
	{
		this(new Point(i, j));
	}
	
	public AObject(Point p)
	{
		curPos = p;
	}
	
	public abstract int getSymbol();
	
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
	
	// stats
	private HashMap<String, Float> stats = new HashMap<String, Float>();
	public float getStat(String key)
	{
		if(stats.containsKey(key)) return stats.get(key);
		else return 0;
	}	
	protected void setStat(String key, float value)
	{
		stats.put(key, value);
	}	
	public void increaseStat(String key, float delta)
	{
		if(stats.containsKey(key)) 
		{
			float oldValue = stats.get(key);
			stats.put(key, oldValue + delta);
		}
	}	
	public void decreaseStat(String key, float delta)
	{
		if(stats.containsKey(key)) 
		{
			float oldValue = stats.get(key);
			stats.put(key, oldValue - delta);
		}
	}
	public int getDov()	// dov = depth of vision
	{
		return (int)getStat("Dov");
	}
	
	/*public boolean equals(AObject ao)
	{
		return cur_pos.equals(ao.cur_pos);
	}*/
}
