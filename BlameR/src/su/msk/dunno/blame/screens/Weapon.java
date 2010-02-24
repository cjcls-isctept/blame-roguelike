package su.msk.dunno.blame.screens;

import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.decisions.Drop;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.objects.items.ColdPart;
import su.msk.dunno.blame.objects.items.FirePart;
import su.msk.dunno.blame.objects.items.LightningPart;
import su.msk.dunno.blame.objects.items.MindPart;
import su.msk.dunno.blame.objects.items.PoisonPart;
import su.msk.dunno.blame.objects.items.SocketExtender;
import su.msk.dunno.blame.objects.symbols.EmptySpace;
import su.msk.dunno.blame.objects.symbols.MinorSelector;
import su.msk.dunno.blame.objects.symbols.SocketSymbol;
import su.msk.dunno.blame.objects.symbols.WeaponBase;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class Weapon implements IScreen
{
	private int weapon_width = 52;
	private int weapon_height = 32;
	
	private ALiving owner;
	private Inventory inventory;
	private EventManager weaponEvents = new EventManager();
	private AObject[][] weaponView = new AObject[weapon_width][weapon_height];
	private LinkedList<AObject> sockets = new LinkedList<AObject>();
	private HashMap<String, String> effects = new HashMap<String, String>();
	private HashMap<String, String> bonuses = new HashMap<String, String>();
	
	private MinorSelector selector = new MinorSelector(0, 0);
	private boolean isSelectSocket;	
	private boolean isWeaponView;
	
	private float maxEnergy;
	private float energy;
	public float energy_fill_rate;
	
	private int maxShield;
	private int shield;
	
	private float damage;
	
	public Weapon(ALiving l, Inventory inv)
	{
		owner = l;
		inventory = inv;
		initEvents();
		initWeaponView();
		fillWeapon(11);
	}
	
	public void process()
	{
		if(inventory.isOpen())inventory.process();
		else 
		{
			weaponEvents.checkEvents();
			showWeapon();
		}
	}
	
	public void showWeapon()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
		GL11.glLoadIdentity();
		MyFont.instance().drawString(owner.getName()+"'s weapon", 20, Blame.height-20, 0.2f, Color.WHITE);
		GL11.glTranslatef(20, 100, 0.0f);
		GL11.glScalef(0.2f, 0.2f, 1.0f);
		for(int i = 0; i < weapon_width; i++)
		{
			for(int j = 0; j < weapon_height; j++)
			{
				if(!"Empty".equals(weaponView[i][j].getName()) && 
				  (!"SocketPlace".equals(weaponView[i][j].getName()) || isSelectSocket))
					MyFont.instance().drawDisplayList(/*isSelectSocket?*/weaponView[i][j].getCode()/*:MyFont.WEAPONBASE*/, 
													  i*100*3/4, j*100, 
											   		  weaponView[i][j].getColor());
			}
		}
		if(isSelectSocket)MyFont.instance().drawDisplayList(selector.getCode(), 
													 		selector.cur_pos.x*100*3/4, 
													 		selector.cur_pos.y*100, 
													 		selector.getColor());
		int k = Blame.height-40;
		for(String s: effects.keySet())
		{
			MyFont.instance().drawString(s+": "+String.format("%1.0f", Float.valueOf(effects.get(s)))+
					(bonuses.containsKey(s)?" (+"+String.format("%1$.0f", Float.valueOf(bonuses.get(s))/Float.valueOf(effects.get(s))*100)+"% bonus)":""), 20, k, 0.2f, Color.GREEN); k -= 15;
		}
		Messages.instance().showMessages();
		Display.sync(Blame.framerate);
		Display.update();
	}
	
	public void calculateEffects()
	{
		effects.clear();
		bonuses.clear();
		for(AObject ao: sockets)
		{
			int num = Integer.valueOf(ao.getState().get("EffectsCapacity"));
			for(int i = 1; i <= num; i++)
			{
				String effect = ao.getState().get("Effect"+i);
				float toAdd = Float.valueOf(ao.getState().get(effect));
				int sameImps = sameImpsNear(ao, ao.cur_pos, null);
				if(effects.containsKey(effect))
				{
					float value = Float.valueOf(effects.get(effect)) + toAdd;
					if(sameImps > 0 && !ao.getState().containsKey("Extender"))
					{
						effects.put(effect, (value+sameImps*toAdd/10.0f)+"");
						if(bonuses.containsKey(effect))bonuses.put(effect, (Float.valueOf(bonuses.get(effect))+sameImps*toAdd/10.0f)+"");
						else bonuses.put(effect, sameImps*toAdd/10.0f+"");
					}
					else effects.put(effect, (value)+"");
				}
				else 
				{
					if(sameImps > 0 && !ao.getState().containsKey("Extender"))
					{
						effects.put(effect, (toAdd+sameImps*toAdd/10.0f)+"");
						if(bonuses.containsKey(effect))bonuses.put(effect, (Float.valueOf(bonuses.get(effect))+sameImps*toAdd/10.0f)+"");
						else bonuses.put(effect, sameImps*toAdd/10.0f+"");
					}
					else effects.put(effect, toAdd+"");
				}
			}
		}
		if(effects.containsKey("Damage"))damage = Float.valueOf(effects.get("Damage"));
		else damage = 0;
		if(effects.containsKey("Energy"))maxEnergy = Float.valueOf(effects.get("Energy"));
		else maxEnergy = 0;
		if(energy > maxEnergy)energy = maxEnergy;
		energy_fill_rate = maxEnergy/20.0f;
	}
	
	public HashMap<String, String> applyEffects()
	{
		if(energy - damage/5 > 0)
		{
			energy -= damage/5;
			return effects;
		}
		else return null;
	}
	
	public int showDamage()
	{
		return (int)damage;
	}
	
	public String showEnergy()
	{
		return (int)energy+"/"+(int)maxEnergy;
	}
	
	public void energyRefill()
	{
		if(energy + energy_fill_rate < maxEnergy)energy += energy_fill_rate;
		else energy = maxEnergy;
	}
	
	public void addImp(AObject ao)
	{
		ao.cur_pos = selector.cur_pos;
		sockets.add(ao);
		weaponView[selector.cur_pos.x][selector.cur_pos.y] = ao;
		if("Socket Extender".equals(ao.getName()))
		{
			addNewSockets(ao);
		}
		calculateEffects();
	}
	
	private void addNewSockets(AObject ao) 
	{
		for(int i = Math.max(0, ao.cur_pos.x-1); i <= Math.min(weaponView.length-1, ao.cur_pos.x+1); i++)
		{
			for(int j = Math.max(0, ao.cur_pos.y-1); j <= Math.min(weaponView[0].length-1, ao.cur_pos.y+1); j++)
			{
				if("Empty".equals(weaponView[i][j].getName()) && !isRestrictedPlace(i, j) && !isDiagonal(ao.cur_pos, new Point(i,j)))
				{
					weaponView[i][j] = new SocketSymbol(i,j);
				}
			}
		}
	}
	
	private boolean isRestrictedPlace(int i, int j)
	{
		if((i == weaponView.length/2-4 && (j == weaponView[0].length/2-1 || j == weaponView[0].length/2-2)) ||
		   (i == weaponView.length/2-2 && (j == weaponView[0].length/2-1 || j == weaponView[0].length/2-2)))
		   return true;
		else return false;
	}
	
	private boolean isDiagonal(Point pos, Point new_pos)
	{
		Point dif = new_pos.minus(pos);
		if(dif.x != 0 && dif.y != 0) return true;
		else return false;
	}
	
	public boolean removeImp(AObject ao)
	{
		sockets.remove(ao);
		weaponView[ao.cur_pos.x][ao.cur_pos.y] = new SocketSymbol(ao.cur_pos);
		calculateEffects();
		if("Socket Extender".equals(ao.getName()))
		{			
			removeSockets(ao);
		}
		if(!inventory.isFull())
		{			
			inventory.addItem(ao);			
			return true;
		}
		else 
		{
			owner.setDecision(new Drop(owner, ao));
			return false;
		}
	}

	private void removeSockets(AObject ao) 
	{
		for(int i = Math.max(0, ao.cur_pos.x-1); i <= Math.min(weaponView.length-1, ao.cur_pos.x+1); i++)
		{
			for(int j = Math.max(0, ao.cur_pos.y-1); j <= Math.min(weaponView[0].length-1, ao.cur_pos.y+1); j++)
			{
				if("SocketPlace".equals(weaponView[i][j].getName()))
				{
					if(noBasePartsNear(weaponView[i][j], null))weaponView[i][j] = new EmptySpace(i,j);
				}
				else if(weaponView[i][j].getState().containsKey("Part"))
				{
					if(noBasePartsNear(weaponView[i][j], null))
					{
						removeImp(weaponView[i][j]);
						weaponView[i][j] = new EmptySpace(i,j);
					}
				}
			}
		}
	}
	
	private int sameImpsNear(AObject ao, Point initP, AObject prev)
	{
		if(ao.cur_pos.equals(initP) && prev != null)return 0;	// prevents circularity
		int num = 0;
		int x = ao.cur_pos.x;
		int y = ao.cur_pos.y;
		if(x < weapon_width-1 && weaponView[x+1][y].getName().equals(ao.getName()) && (prev == null || !weaponView[x+1][y].equals(prev)))
		{
			num++;
			num += (sameImpsNear(weaponView[x+1][y], initP, ao));
		}
		if(x >= 1 && weaponView[x-1][y].getName().equals(ao.getName()) && (prev == null || !weaponView[x-1][y].equals(prev)))
		{
			num++;
			num += (sameImpsNear(weaponView[x-1][y], initP, ao));
		}
		if(y < weapon_height-1 && weaponView[x][y+1].getName().equals(ao.getName()) && (prev == null || !weaponView[x][y+1].equals(prev)))
		{
			num++;
			num += (sameImpsNear(weaponView[x][y+1], initP, ao));
		}
		if(y >= 1 && weaponView[x][y-1].getName().equals(ao.getName()) && (prev == null || !weaponView[x][y-1].equals(prev)))
		{
			num++;
			num += (sameImpsNear(weaponView[x][y-1], initP, ao));
		}
		return num;
	}
	
	private boolean noBasePartsNear(AObject ao, AObject prev)
	{
		boolean b = true;
		int x = ao.cur_pos.x;
		int y = ao.cur_pos.y;
		if(x < weapon_width-1 && (prev == null || !weaponView[x+1][y].equals(prev)) && true)
		{
			if("Weapon Sceleton".equals(weaponView[x+1][y].getName()))return false;
			else if("Socket Extender".equals(weaponView[x+1][y].getName()))b = b && noBasePartsNear(weaponView[x+1][y], ao); 
		}
		if(x >= 1 && (prev == null || !weaponView[x-1][y].equals(prev)) && true)
		{
			if("Weapon Sceleton".equals(weaponView[x-1][y].getName()))return false;
			else if("Socket Extender".equals(weaponView[x-1][y].getName()))b = b && noBasePartsNear(weaponView[x-1][y], ao);
		}
		if(y < weapon_height-1 && (prev == null || !weaponView[x][y+1].equals(prev)) && true)
		{
			if("Weapon Sceleton".equals(weaponView[x][y+1].getName()))return false;
			else if("Socket Extender".equals(weaponView[x][y+1].getName()))b = b && noBasePartsNear(weaponView[x][y+1], ao);
		}
		if(y >= 1 && (prev == null || !weaponView[x][y-1].equals(prev)) && true)
		{
			if("Weapon Sceleton".equals(weaponView[x][y-1].getName()))return false;
			else if("Socket Extender".equals(weaponView[x][y-1].getName()))b = b && noBasePartsNear(weaponView[x][y-1], ao);
		}
		return b;
	}

	public void initWeaponView()
	{
		for(int i = 0; i < weaponView.length; i++)
		{
			for(int j = 0; j < weaponView[0].length; j++)
			{
				weaponView[i][j] = new EmptySpace(i, j);
			}
		}
		
		for(int i = 0; i < 6; i++)
		{
			weaponView[weaponView.length/2-3+i][weaponView[0].length/2] = new WeaponBase(weaponView.length/2-3+i, 
																						 weaponView[0].length/2);
		}
		for(int i = 0; i < 2; i++)
		{
			weaponView[weaponView.length/2-3][weaponView[0].length/2-2+i] = new WeaponBase(weaponView.length/2-3, 
					 																	   weaponView[0].length/2-2+i);
		}
		for(int i = 0; i < 6; i++)
		{
			weaponView[weaponView.length/2-3+i][weaponView[0].length/2+1] = new SocketSymbol(weaponView.length/2-3+i, 
					 																	     weaponView[0].length/2+1);
		}
		for(int i = 0; i < 4; i++)
		{
			weaponView[weaponView.length/2-1+i][weaponView[0].length/2-1] = new SocketSymbol(weaponView.length/2-1+i, 
					 																	     weaponView[0].length/2-1);
		}
		weaponView[weaponView.length/2-4][weaponView[0].length/2] = new SocketSymbol(weaponView.length/2-4, 
			     																	 weaponView[0].length/2);
		weaponView[weaponView.length/2-3][weaponView[0].length/2-3] = new SocketSymbol(weaponView.length/2-3, 
				 																	 weaponView[0].length/2-3);
		weaponView[weaponView.length/2+3][weaponView[0].length/2] = new SocketSymbol(weaponView.length/2+3, 
				 																	 weaponView[0].length/2);
	}
	
	public boolean isOpen()
	{
		return isWeaponView;
	}
	
	public void openWeaponView()
	{
		Messages.instance().clear();
		isWeaponView = true;
	}
	
	private void fillWeapon(int num)
	{
		int amount = Math.min(getFreeSocketsNum(), num);
		Point p;
		int rand;
		AObject ao;
		p = getFreeRandomSocket();
		if(p != null)
		{
			selector.cur_pos = p;
			addImp(new MindPart(p));
		}
		for(int i = 0; i < amount-1; i++)
		{
			p = getFreeRandomSocket();
			if(p != null)
			{
				rand = (int)(Math.random()*5);
				selector.cur_pos = p;
				switch(rand)
				{
				case 0: ao = new ColdPart(p); addImp(ao); continue;
				case 1: ao = new FirePart(p); addImp(ao); continue;
				case 2: ao = new LightningPart(p); addImp(ao); continue;
				case 3: ao = new PoisonPart(p); addImp(ao); continue;
				case 4: ao = new SocketExtender(p); addImp(ao); continue;			
				}
			}
		}
		energy = maxEnergy;
		energy_fill_rate = maxEnergy/20.0f;
	}
	
	private int getFreeSocketsNum()
	{
		int sum = 0;
		for(int i = 0; i < weapon_width; i++)
		{
			for(int j = 0; j < weapon_height; j++)
			{
				if("SocketPlace".equals(weaponView[i][j].getName()))sum++;
			}
		}
		return sum;
	}
	
	private Point getFreeRandomSocket()
	{
		if(getFreeSocketsNum() > 0)
		{
			int x = (int)(Math.random()*weapon_width);
			int y = (int)(Math.random()*weapon_height);
			while(!"SocketPlace".equals(weaponView[x][y].getName()))
			{
				x = (int)(Math.random()*weapon_width);
				y = (int)(Math.random()*weapon_height);
			}
			return new Point(x,y);
		}
		return null;
	}
		
	public void initEvents()
	{
		weaponEvents.addListener(Keyboard.KEY_NUMPAD9, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.x < weaponView.length-1 && selector.cur_pos.y < weaponView[0].length-1)
        		{
        			selector.cur_pos = selector.cur_pos.plus(1,1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_UP, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.y < weaponView[0].length-1)
        		{
        			selector.cur_pos = selector.cur_pos.plus(0,1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD8, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.y < weaponView[0].length-1)
        		{
        			selector.cur_pos = selector.cur_pos.plus(0,1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD7, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.x > 0 && selector.cur_pos.y < weaponView[0].length-1)
        		{
        			selector.cur_pos = selector.cur_pos.plus(-1,1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_RIGHT, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.x < weaponView.length-1)
        		{
        			selector.cur_pos = selector.cur_pos.plus(1,0);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD6, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.x < weaponView.length-1)
        		{
        			selector.cur_pos = selector.cur_pos.plus(1,0);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_LEFT, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.x > 0)
        		{
        			selector.cur_pos = selector.cur_pos.plus(-1,0);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD4, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.x > 0)
        		{
        			selector.cur_pos = selector.cur_pos.plus(-1,0);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD3, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.x < weaponView.length-1 && selector.cur_pos.y > 0)
        		{
        			selector.cur_pos = selector.cur_pos.plus(1,-1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_DOWN, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.y > 0)
        		{
        			selector.cur_pos = selector.cur_pos.plus(0,-1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD2, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.y > 0)
        		{
        			selector.cur_pos = selector.cur_pos.plus(0,-1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD1, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.cur_pos.x > 0 && selector.cur_pos.y > 0)
        		{
        			selector.cur_pos = selector.cur_pos.plus(-1,-1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_RETURN, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if("SocketPlace".equals(weaponView[selector.cur_pos.x][selector.cur_pos.y].getName()))
        				inventory.openInventory(Inventory.TO_SELECT_SOCKET);
        		else if("Weapon Sceleton".equals(weaponView[selector.cur_pos.x][selector.cur_pos.y].getName()))
        		{
        			Messages.instance().addMessage("Can't remove this part");
        		}
        		else if(weaponView[selector.cur_pos.x][selector.cur_pos.y].getState().containsKey("Part"))
        		{
        			if(!removeImp(weaponView[selector.cur_pos.x][selector.cur_pos.y]))
        				Messages.instance().addMessage("Inventory is full. Drop the part to the ground");
        		}
        		else
        			Messages.instance().addMessage("There is no socket or weapon part here");
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_ESCAPE, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(isSelectSocket)isSelectSocket = false;
        		else isWeaponView = false;;
        	}
        });
	}
}
