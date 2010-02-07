package su.msk.dunno.blame.main.support.listeners;

import org.lwjgl.input.Keyboard;

public class KeyListener extends Listener 
{	
	public KeyListener(long repeatTime) 
	{
		super(repeatTime);
	}	

	@Override public void check()
	{
		if(Keyboard.isKeyDown(monitored)) 
        {
			if(!wasPressed)
			{
				onKeyDown();
				wasPressed = true;
				lastPressed = System.currentTimeMillis();
			}
			else if(isRepeatable && System.currentTimeMillis()-lastPressed > repeatTime) 
			{
				onKeyDown();
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
