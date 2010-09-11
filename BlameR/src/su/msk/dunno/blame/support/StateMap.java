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
	

	public StateMap put(String key) 
	{
		args.put(key, null);
		return this;
	}

	public StateMap putInt(String key, int int_num)
	{
		args.put(key, new StateData(int_num));
		return this;
	}
	
	public StateMap putFloat(String key, float float_num)
	{
		args.put(key, new StateData(float_num));
		return this;
	}
	
	public StateMap putString(String key, String message)
	{
		args.put(key, new StateData(message));
		return this;
	}
	
	public StateMap putObject(String key, AObject ao)
	{
		args.put(key, new StateData(ao));
		return this;
	}
	
	public StateMap putLiving(String key, ALiving al)
	{
		args.put(key, new StateData(al));
		return this;
	}
	
	public int getInt(String key)	// args.containsKey(key) unchecked
	{
		if(args.containsKey(key))
		{
			StateData sd = args.get(key);
			if(sd.getInt() != 0)return sd.getInt();
			else if(sd.getFloat() != 0)return (int)sd.getFloat();
		}
		return 0;
	}
	
	public float getFloat(String key)	// args.containsKey(key) unchecked
	{
		if(args.containsKey(key))
		{
			StateData sd = args.get(key);
			if(sd.getFloat() != 0)return sd.getFloat();
			else if(sd.getInt() != 0)return sd.getInt();
		}		
		return 0;
	}
	
	public String getString(String key)	// args.containsKey(key) unchecked
	{
		if(args.containsKey(key))
		{
			StateData sd = args.get(key);
			if(!"".equals(sd.getString()))return sd.getString();
			else if(sd.getInt() != 0)return sd.getInt()+"";
			else if(sd.getFloat() != 0)return sd.getFloat()+"";
		}		
		return "";
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
	private int int_num = 0;
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
	
	private float float_num = 0;
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
	
	private String message = "";
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
	
	private AObject ao = null;
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
	
	private ALiving l = null;
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