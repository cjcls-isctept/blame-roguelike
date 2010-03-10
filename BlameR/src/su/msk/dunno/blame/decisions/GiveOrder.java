package su.msk.dunno.blame.decisions;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.StateMap;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class GiveOrder extends ADecision implements IScreen 
{
	private boolean isRunning;
	private EventManager orderEvents = new EventManager();
	private Field field;
	
	private boolean isFollowMe;

	public GiveOrder(ALiving al, Field field) 
	{
		super(al);
		this.field = field;
		initEvents();
	}

	@Override public void doAction(int actionMoment) 
	{
		process();
		wasExecuted = true;
	}

	public void process() 
	{
		isRunning = true;
		do
		{
			orderEvents.checkEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
			GL11.glLoadIdentity();
			field.draw(al.cur_pos);
			Blame.getCurrentPlayer().drawStats();
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
				if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" приказывает следовать за собой");
				for(AObject o: al.getMyNeighbours())
				{
					o.changeState(al, new StateMap("FollowMe"));
				}
				isRunning = false;
			}
		});
		orderEvents.addListener(Keyboard.KEY_2, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" приказывает атаковать врагов в поле зрения");
				for(AObject o: al.getMyNeighbours())
				{
					o.changeState(al, new StateMap("ShootEnemies"));
				}
				isRunning = false;
			}
		});
		orderEvents.addListener(Keyboard.KEY_3, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" приказывает оставаться на месте");
				for(AObject o: al.getMyNeighbours())
				{
					o.changeState(al, new StateMap("Wait"));
				}
				isRunning = false;
			}
		});
		orderEvents.addListener(Keyboard.KEY_4, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" приказывает не атаковать врагов");
				for(AObject o: al.getMyNeighbours())
				{
					o.changeState(al, new StateMap("StopShoot"));
				}
				isRunning = false;
			}
		});
		orderEvents.addListener(Keyboard.KEY_5, new KeyListener(0)
		{
			public void onKeyDown()
			{
				isRunning = false;
			}
		});
	}
}
