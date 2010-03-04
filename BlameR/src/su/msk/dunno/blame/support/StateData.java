package su.msk.dunno.blame.support;

import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;

public class StateData 
{
	private int int_num;
	public StateData(int i)
	{
		int_num = i;
	}
	public int getInt()
	{
		return int_num;
	}
	public StateData setInt(int int_num)
	{
		this.int_num = int_num;
		return this;
	}
	
	private float float_num;
	public StateData(float f)
	{
		float_num = f;
	}
	public float getFloat()
	{
		return float_num;
	}
	public StateData setFloat(float f)
	{
		float_num = f;
		return this;
	}
	
	private String message;
	public StateData(String s)
	{
		message = s;
	}
	public String getString()
	{
		return message;
	}
	public StateData setString(String s)
	{
		message = s;
		return this;
	}
	
	private AObject ao;
	public StateData(AObject ao)
	{
		this.ao = ao;
	}
	public AObject getObject()
	{
		return ao;
	}
	public StateData setObject(AObject ao)
	{
		this.ao = ao;
		return this;
	}
	
	private ALiving l;
	public StateData(ALiving al)
	{
		this.l = al;
	}
	public ALiving getLiving()
	{
		return l;
	}
	public StateData setLiving(ALiving al)
	{
		this.l = al;
		return this;
	}
}
