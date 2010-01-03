package net.dunno.blame.listeners;

public abstract class Listener 
{
	protected int monitored;
	protected boolean wasPressed;
	
	public void setMonitored(int key)
	{
		monitored = key;
	}
	
	public abstract void check();	
}
