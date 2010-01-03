package net.dunno.blame.listeners;

import org.lwjgl.input.Keyboard;

public class KeyListener extends Listener 
{	
	public void check()
	{
		if(Keyboard.isKeyDown(monitored)) 
        {
    		wasPressed = true;
    		onKeyDown();        	
        }
        else
        	if (wasPressed)
        	{
        		onKeyUp();
        		wasPressed = false;
        	}
	}
	
	public void onKeyDown(){};
	public void keyPressed(){};
	public void onKeyUp(){};	
}
