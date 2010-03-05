package su.msk.dunno.blame.screens;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class EnemyInterface implements IScreen 
{
	ALiving al;
	EventManager events = new EventManager();
	private boolean isRunning;
	
	public EnemyInterface(ALiving al)
	{
		this.al = al;
		initEvents();
	}
	
	private void initEvents() 
	{
		events.addListener(Keyboard.KEY_1, new KeyListener(0)
		{
			public void onKeyDown()
			{
				isRunning = false;
			}
		});
	}
	
	public void showInterface()
	{
		int k = Blame.height-25;
		TrueTypeFont.instance().drawString(al.getName()+"'s mind", 20, k, Color.WHITE); k-=20; k-=20;
		TrueTypeFont.instance().drawString("ERROR: Unknown data format!", 20, k, Color.WHITE); k-=20;
		TrueTypeFont.instance().drawString("1. Exit", 20, k, Color.WHITE);
	}

	public void process() 
	{
		isRunning = true;
		while(isRunning)
		{
			events.checkEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
			GL11.glLoadIdentity();
			showInterface();
			Display.sync(Blame.framerate);
			Display.update();
		}
	}
}
