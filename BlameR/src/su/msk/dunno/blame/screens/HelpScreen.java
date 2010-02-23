package su.msk.dunno.blame.screens;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class HelpScreen implements IScreen 
{
	private static HelpScreen instance;
	EventManager helpEvents = new EventManager();
	private boolean isHelpOn;
	
	public static HelpScreen instance()
	{
		if(instance == null)instance = new HelpScreen();
		return instance;
	}
	
	private HelpScreen()
	{
		initEvents();
	}
	
	public void openHelp()
	{
		isHelpOn = true;
	}
	
	public boolean isOpen()
	{
		return isHelpOn;
	}
	
	public void initEvents()
	{
		helpEvents.addListener(Keyboard.KEY_ESCAPE, new KeyListener(0)
		{
			public void onKeyDown()
			{
				isHelpOn = false;
			}
		});
	}

	public void process() 
	{
		helpEvents.checkEvents();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
		GL11.glLoadIdentity();
		int k = Blame.height-20;
		MyFont.instance().drawString("Arrow Keys/Numpad Keys - movement\n", 20, k, 0.2f, Color.WHITE); k -=15;
		MyFont.instance().drawString("F - shoot\n", 20, k, 0.2f, Color.WHITE); k -=15;
		MyFont.instance().drawString("I - open Inventory\n", 20, k, 0.2f, Color.WHITE); k -=15;
		MyFont.instance().drawString("W - open weapon\n", 20, k, 0.2f, Color.WHITE); k -=15;
		MyFont.instance().drawString("F1 - open this screen\n", 20, k, 0.2f, Color.WHITE); k -=15;
		MyFont.instance().drawString("F12 - exit game", 20, k, 0.2f, Color.WHITE); k -=15;
		MyFont.instance().drawString("Esc - exit from current screen", 20, k, 0.2f, Color.WHITE); k -=15;
		MyFont.instance().drawString("      (Weapon, Inventory, etc)", 20, k, 0.2f, Color.WHITE); k -=15;
		Display.sync(Blame.framerate);
		Display.update();
	}
}
