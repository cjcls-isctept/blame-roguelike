package su.msk.dunno.blame.main.support.listeners;

public abstract class Listener 
{
	protected int monitored;
	protected boolean wasPressed;
	protected boolean isRepeatable;
	protected long repeatTime;
	protected long lastPressed;
	
	public Listener(long repeatTime)
	{
		this.repeatTime = repeatTime;
		if(repeatTime > 0)isRepeatable = true;
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
