package su.msk.dunno.blame.objects.buildings;

import java.util.ListIterator;

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
import su.msk.dunno.blame.support.StateMap;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

// Rewrite this!!!!!! (initEvents section, changeState() arguments)
public class RebuildStation extends ALiving implements IScreen
{
	private boolean isGreetingsMessageDone;
	private boolean isGuardsDropped;
	private Killy player;
	private boolean isRunning;
	private EventManager events = new EventManager();
	private int mixture_capacity = 100;
	private boolean isCorrupted = true;
	
	private StateMap args = new StateMap();
	
	public RebuildStation(int i, int j, Field field) 
	{
		super(i, j, field);
		initEvents();		
	}
	
	@Override protected void initStats() 
	{
		setStat("Health", 50);
		setStat("Dov", 2);
	}
	
	@Override protected void initItemDrop() {}

	@Override public Color getColor() 
	{
		if(isCorrupted)return Color.GREEN;
		else return Color.RED;
	}

	@Override public boolean isEnemy(AObject ao) 
	{
		StateMap sm = ao.getState();
		if(sm.containsKey("Player")) return isCorrupted;
		else if(sm.containsKey("SiliconCreature")) return !isCorrupted;
		else return false;
	}

	@Override public ADecision livingAI() 
	{
		if(isNearPlayer())
		{
			if(!isGreetingsMessageDone)
			{
				Messages.instance().addPropMessage("rebuild.enter",getName());
				isGreetingsMessageDone = true;
			}
			if(isCorrupted && !isGuardsDropped)
			{
				Messages.instance().addMessage("Intruder! Primary task: termination");
				dropGuards();
			}
		}
		else if(isGreetingsMessageDone)
		{
			isGreetingsMessageDone = false;
		}
		return null;
	}

	private void dropGuards() 
	{
		// TODO: drop guards
		isGuardsDropped = true;
	}

	@Override public String getName() 
	{
		return "Rebuild Station";
	}

	@Override public int getSymbol()
	{
		return MyFont.STATION;
	}
	
	@Override public boolean getPassability()
	{
		return true;
	}
	
	@Override public StateMap getState()
	{
		StateMap sm = new StateMap("Station");
		if(isCorrupted) sm.put("Corrupted");
		return sm;
	}
	
	@Override public void changeState(ALiving changer, StateMap effects)
	{
		super.changeState(changer, effects);
		if(effects.containsKey("Enter"))
		{
			if(!isCorrupted)
			{
				player = Blame.getPlayer(effects.getString("Enter"));
				Messages.instance().clear();
				process();
			}
			else if(isNearPlayer()) Messages.instance().addMessage("Cannot enter the corrupted station!");
		}
	}
	
	@Override public boolean checkStatus(ListIterator<ALiving> li) 
	{
		if(getStat("Health") <= 0)
		{
			isCorrupted = !isCorrupted;
			setStat("Health", 50);
			if(isNearPlayer())
			{
				if(!isCorrupted) Messages.instance().addMessage(getName()+" was liberated");
				else Messages.instance().addMessage(getName()+" was corrupted");
			}
		}
		return false;
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
		TrueTypeFont.instance().drawString(Messages.instance().getPropMessage("rebuild.welcome", getName(), player.getName()), 20, k, Color.WHITE); k-=20; k-=20;
		
		TrueTypeFont.instance().drawString(Messages.instance().getPropMessage("rebuild.mixture.capacity"), 20, k, Color.WHITE); k-=20;		
		StringBuilder sb = new StringBuilder();
		int n = mixture_capacity/5;
		Color c = Color.WHITE;
		if(n >= 14)c = Color.CYAN;
		else if(n >= 9)c = Color.YELLOW;
		else c = Color.RED;
		for(int i = 0; i < n; i++)sb.append("#");
		TrueTypeFont.instance().drawString(sb.toString(), 20, k, c); k-=20; k-=20;
		
		TrueTypeFont.instance().drawString(Messages.instance().getPropMessage("rebuild.player.health", player.getName(), player.getStat("Health")+""), 20, k, Color.WHITE); k-=20;
		/*TrueTypeFont.instance().drawString(Messages.instance().getPropMessage("rebuild.player.infection", player.getName(), player.getInfectionLevel()), 20, k, Color.WHITE); k-=20; k-=20;*/
		TrueTypeFont.instance().drawString("1. Improve health by 10", 20, k, Color.WHITE); k-=20;
		TrueTypeFont.instance().drawString("2. Improve health by 1", 20, k, Color.WHITE); k-=20;
		/*TrueTypeFont.instance().drawString("3. Reduce infection level by 10", 20, k, Color.WHITE); k-=20;
		TrueTypeFont.instance().drawString("4. Reduce infection level by 1", 20, k, Color.WHITE); k-=20;*/
		TrueTypeFont.instance().drawString("5. Exit", 20, k, Color.WHITE); k-=20;
		
		Messages.instance().showMessages();
	}
	
	@Override public boolean isMovable()  // stations cannot move
	{
		return false;
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
					args.putInt("HealthPlus", 10);
					player.changeState(player, args);
					mixture_capacity -= 10;
				}
				else Messages.instance().addPropMessage("rebuild.nomixture");
			}
		});
		events.addListener(Keyboard.KEY_2, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(mixture_capacity >= 1)
				{
					args.clear();
					args.putInt("HealthPlus", 1);
					player.changeState(player, args);
					mixture_capacity -= 1;
				}
				else Messages.instance().addPropMessage("rebuild.nomixture");
			}
		});
		/*events.addListener(Keyboard.KEY_3, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(mixture_capacity >= 10)
				{
					args.clear();
					args.putInt("InfectionHeal", 10);
					player.changeState(player, args);
					mixture_capacity -= 10;
				}
				else Messages.instance().addPropMessage("rebuild.nomixture");
			}
		});
		events.addListener(Keyboard.KEY_4, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(mixture_capacity >= 1)
				{
					args.clear();
					args.putInt("InfectionHeal", 1);
					player.changeState(player, args);
					mixture_capacity -= 1;
				}
				else Messages.instance().addPropMessage("rebuild.nomixture");
			}
		});*/
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
