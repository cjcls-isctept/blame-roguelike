package su.msk.dunno.blame.decisions;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class SayOrder extends ADecision implements IScreen 
{
	private boolean isRunning;
	private EventManager orderEvents = new EventManager();

	public SayOrder(ALiving al) 
	{
		super(al);
		initEvents();
	}

	@Override public void doAction(int actionMoment) 
	{
	
		wasExecuted = true;
	}

	@Override public void process() 
	{
		isRunning = true;
		do
		{
			orderEvents.checkEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
			GL11.glLoadIdentity();
			int h = 75;
			TrueTypeFont.instance().drawString("1. Follow me", 20, h, Color.WHITE); h -=15;
			TrueTypeFont.instance().drawString("2. Shoot enemies on sight", 20, h, Color.WHITE); h -=15;
			TrueTypeFont.instance().drawString("3. Wait here", 20, h, Color.WHITE); h -=15;
			TrueTypeFont.instance().drawString("4. Stop shooting", 20, h, Color.WHITE); h -=15;
			TrueTypeFont.instance().drawString("5. Exit", 20, h, Color.WHITE); h -=15;
			Display.sync(Blame.framerate);
			Display.update();
		}
		while(isRunning);
	}
	
	private void showOrders()
	{
		
	}
	
	private void initEvents() 
	{
		orderEvents.addListener(Keyboard.KEY_1, new KeyListener(0)
		{
			public void onKeyDown()
			{
				
			}
		});
	}
}
