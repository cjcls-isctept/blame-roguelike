package su.msk.dunno.blame.support;

import java.util.HashMap;
import java.util.Set;

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

	public void putInt(String key, int int_num)
	{
		args.put(key, new StateData(int_num));
	}
	
	public void putFloat(String key, float float_num)
	{
		args.put(key, new StateData(float_num));
	}
	
	public void putString(String key, String message)
	{
		args.put(key, new StateData(message));
	}
	
	public void putObject(String key, AObject ao)
	{
		args.put(key, new StateData(ao));
	}
	
	public void putLiving(String key, ALiving al)
	{
		args.put(key, new StateData(al));
	}
	
	public int getInt(String key)	// args.containsKey(key) unchecked
	{
		if(args.get(key).getInt() != 0)return args.get(key).getInt();
		else if(args.get(key).getFloat() != 0)return (int)args.get(key).getFloat();
		else return 0;
	}
	
	public float getFloat(String key)	// args.containsKey(key) unchecked
	{
		if(args.get(key).getFloat() != 0)return args.get(key).getFloat();
		else if(args.get(key).getInt() != 0)return args.get(key).getInt();
		else return 0;
	}
	
	public String getString(String key)	// args.containsKey(key) unchecked
	{
		if(args.get(key).getString() != null)return args.get(key).getString();
		else if(args.get(key).getInt() != 0)return args.get(key).getInt()+"";
		else if(args.get(key).getFloat() != 0)return args.get(key).getFloat()+"";
		else return "";
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

	public Set<String> getKeys() 
	{
		return args.keySet();
	}

	public void clear() 
	{
		args.clear();
	}
}

class StateData 
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