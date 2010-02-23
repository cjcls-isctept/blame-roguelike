package su.msk.dunno.blame.support.listeners;

import java.util.LinkedList;

public class EventManager 
{
	private static EventManager instance;
	public static EventManager instance()
	{
		if (instance == null)
			instance = new EventManager();
		return instance;
	}
	
	private LinkedList<Listener> listeners = new LinkedList<Listener>();
	private LinkedList<Listener> to_add = new LinkedList<Listener>();
	private LinkedList<Listener> to_remove = new LinkedList<Listener>();
	
	public void addListener(int key, Listener listener)
	{
		listener.setMonitored(key);
		to_add.add(listener);
	}
	
	public void removeListener(Listener listener) {
		to_remove.add(listener);
	}
	
	public void checkEvents()
	{
		if(to_add.size() > 0) {
			listeners.addAll(to_add); to_add.clear();
		}
		if(to_remove.size() > 0) {
			listeners.removeAll(to_remove); to_remove.clear();
		}		
		for(Listener l : listeners)
		{
			l.check();
		}
	}
}
