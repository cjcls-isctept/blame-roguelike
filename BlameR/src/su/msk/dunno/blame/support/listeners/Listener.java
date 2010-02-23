package su.msk.dunno.blame.support.listeners;

public abstract class Listener 
{
	protected int monitored;
	protected boolean wasPressed;
	
	public Listener()
	{
		
	}
	
	public void setMonitored(int key)
	{
		monitored = key;
	}
	
	public int getKey()
	{
		return monitored;
	}
	
	public abstract void check();	
}
