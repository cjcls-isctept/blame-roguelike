package su.msk.dunno.blame.support.listeners;

import org.lwjgl.input.Keyboard;

public class KeyListener extends Listener 
{	
	protected boolean isRepeatable;
	protected long lastPressed;
	
	
	protected long repeatTime;
	protected long firstRepeatTime;
	protected long secondRepeatTime;
	
	public KeyListener(long firstRepeatTime, long secondRepeatTime) 
	{
		this(firstRepeatTime);
		this.firstRepeatTime = firstRepeatTime;
		this.secondRepeatTime = secondRepeatTime;
	}
	
	public KeyListener(long repeatTime) 
	{
		this.repeatTime = repeatTime;
		if(repeatTime > 0)isRepeatable = true;
	}	

	@Override public void check()
	{
		if(Keyboard.isKeyDown(monitored)) 
        {
			if(!wasPressed)
			{
				onKeyDown();
				wasPressed = true;
				if(firstRepeatTime > 0)repeatTime = firstRepeatTime;
				lastPressed = System.currentTimeMillis();
			}
			else if(isRepeatable && System.currentTimeMillis()-lastPressed > repeatTime) 
			{
				onKeyDown();
				if(secondRepeatTime > 0)repeatTime = secondRepeatTime;
				lastPressed = System.currentTimeMillis();
			}
        }
        else if(wasPressed)
        {
        	onKeyUp();
        	wasPressed = false;
        }
	}
	
	public void onKeyDown(){};
	public void keyPressed(){};
	public void onKeyUp(){};	
}
