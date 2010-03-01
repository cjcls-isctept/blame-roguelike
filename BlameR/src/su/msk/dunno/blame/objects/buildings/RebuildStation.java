package su.msk.dunno.blame.objects.buildings;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.livings.Killy;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

// Rewrite this!!!!!! (initEvents section, changeState() arguments)
public class RebuildStation extends ALiving implements IScreen
{
	private boolean isGreetingsMessageDone;
	private Killy player;
	private boolean isRunning;
	private EventManager events = new EventManager();
	private int mixture_capacity = 100;
	
	private HashMap<String, String> args = new HashMap<String, String>();
	
	public RebuildStation(int i, int j, Field field) 
	{
		super(i, j, field);
		health = 10;
		dov = 2;
		initEvents();		
	}

	@Override public Color getColor() 
	{
		return Color.RED;
	}

	@Override public boolean isEnemy(AObject ao) 
	{
		return false;
	}

	@Override public boolean isPlayer() 
	{
		return false;
	}

	@Override public ADecision livingAI() 
	{
		if(isNearPlayer())
		{
			if(!isGreetingsMessageDone)
			{
				Messages.instance().addMessage("Entering "+getName()+"!");
				isGreetingsMessageDone = true;
			}
		}
		else if(isGreetingsMessageDone)
		{
			isGreetingsMessageDone = false;
		}
		return null;
	}

	@Override public String getName() 
	{
		return "Rebuild Station";
	}

	@Override public int getCode()
	{
		return MyFont.STATION;
	}
	
	@Override public boolean getPassability()
	{
		return true;
	}
	
	@Override public HashMap<String, String> getState()
	{
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("Station", "");
		return args;
	}
	
	@Override public void changeState(ALiving changer, HashMap<String, String> args)
	{
		if(args.containsKey("Enter"))
		{
			player = Blame.getPlayer(args.get("Enter"));
			Messages.instance().clear();
			process();
		}
	}

	public void process() 
	{
		isRunning = true;
		while(isRunning)
		{
			events.checkEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
			GL11.glLoadIdentity();
			showStationInterface();
			Display.sync(Blame.framerate);
			Display.update();
		}
	}
	
	public void showStationInterface()
	{
		int k = Blame.height-20;
		TrueTypeFont.instance().drawString(getName()+" welcomes you, "+player.getName()+"!", 20, k, Color.WHITE); k-=20; k-=20;
		
		TrueTypeFont.instance().drawString("Mixture capacity:", 20, k, Color.WHITE); k-=20;		
		StringBuilder sb = new StringBuilder();
		int n = mixture_capacity/5;
		Color c = Color.WHITE;
		if(n >= 14)c = Color.CYAN;
		else if(n >= 9)c = Color.YELLOW;
		else c = Color.RED;
		for(int i = 0; i < n; i++)sb.append("#");
		TrueTypeFont.instance().drawString(sb.toString(), 20, k, c); k-=20; k-=20;
		
		TrueTypeFont.instance().drawString(player.getName()+"'s health: "+player.getHealth(), 20, k, Color.WHITE); k-=20;
		TrueTypeFont.instance().drawString(player.getName()+"'s infection level: "+player.getInfectionLevel(), 20, k, Color.WHITE); k-=20; k-=20;
		TrueTypeFont.instance().drawString("1. Improve health by 10", 20, k, Color.WHITE); k-=20;
		TrueTypeFont.instance().drawString("2. Improve health by 1", 20, k, Color.WHITE); k-=20;
		TrueTypeFont.instance().drawString("3. Reduce infection level by 10", 20, k, Color.WHITE); k-=20;
		TrueTypeFont.instance().drawString("4. Reduce infection level by 1", 20, k, Color.WHITE); k-=20;
		TrueTypeFont.instance().drawString("5. Exit", 20, k, Color.WHITE); k-=20;
		
		Messages.instance().showMessages();
	}
	
	private void initEvents() 
	{
		events.addListener(Keyboard.KEY_1, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(mixture_capacity >= 10)
				{
					args.clear();
					args.put("HealthPlus", "10");
					player.changeState(player, args);
					mixture_capacity -= 10;
				}
				else Messages.instance().addMessage("Not enough mixture!");
			}
		});
		events.addListener(Keyboard.KEY_2, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(mixture_capacity >= 1)
				{
					args.clear();
					args.put("HealthPlus", "1");
					player.changeState(player, args);
					mixture_capacity -= 1;
				}
				else Messages.instance().addMessage("Not enough mixture!");
			}
		});
		events.addListener(Keyboard.KEY_3, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(mixture_capacity >= 10)
				{
					args.clear();
					args.put("InfectionHeal", "10");
					player.changeState(player, args);
					mixture_capacity -= 10;
				}
				else Messages.instance().addMessage("Not enough mixture!");
			}
		});
		events.addListener(Keyboard.KEY_4, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(mixture_capacity >= 1)
				{
					args.clear();
					args.put("InfectionHeal", "1");
					player.changeState(player, args);
					mixture_capacity -= 1;
				}
				else Messages.instance().addMessage("Not enough mixture!");
			}
		});
		events.addListener(Keyboard.KEY_5, new KeyListener(0)
		{
			public void onKeyDown()
			{
				isRunning = false;
			}
		});
		events.addListener(Keyboard.KEY_ESCAPE, new KeyListener(0)
		{
			public void onKeyDown()
			{
				isRunning = false;
			}
		});
	}
}
