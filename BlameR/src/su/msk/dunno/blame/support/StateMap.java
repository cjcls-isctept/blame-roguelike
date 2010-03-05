package su.msk.dunno.blame.support;

import java.util.HashMap;

import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;

public class StateMap 
{
	private HashMap<String, StateData> args = new HashMap<String, StateData>();
	
	public StateMap() 
	{

	}
	
	public StateMap(String key)
	{
		args.put(key, null);
	}
	
	public StateMap(String s, int int_num)
	{
		args.put(s, new StateData(int_num));
	}
	
	public StateMap(String s, float float_num)
	{
		args.put(s, new StateData(float_num));
	}
	
	public StateMap(String s, String message)
	{
		args.put(s, new StateData(message));
	}
	
	public StateMap(String s, AObject ao)
	{
		args.put(s, new StateData(ao));
	}
	
	public StateMap(String s, ALiving al)
	{
		args.put(s, new StateData(al));
	}
	

	public void put(String key) 
	{
		args.put(key, null);
	}

	public void put(String key, int int_num)
	{
		args.put(key, new StateData(int_num));
	}
	
	public void put(String key, float float_num)
	{
		args.put(key, new StateData(float_num));
	}
	
	public void put(String key, String message)
	{
		args.put(key, new StateData(message));
	}
	
	public void put(String key, AObject ao)
	{
		args.put(key, new StateData(ao));
	}
	
	public void put(String key, ALiving al)
	{
		args.put(key, new StateData(al));
	}
	
	public int getInt(String key)
	{
		return args.get(key).getInt();
	}
	
	public float getFloat(String key)
	{
		return args.get(key).getFloat();
	}
	
	public String getString(String key)
	{
		return args.get(key).getString();
	}
	
	public AObject getObject(String key)
	{
		return args.get(key).getObject();
	}
	
	public ALiving getLiving(String key)
	{
		return args.get(key).getLiving();
	}

	public boolean containsKey(String key) 
	{
		return args.containsKey(key);
	}
}
