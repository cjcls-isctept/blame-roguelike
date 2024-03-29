package su.msk.dunno.blame.screens;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.decisions.Drop;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.objects.items.ImpAcid;
import su.msk.dunno.blame.objects.items.ImpBio;
import su.msk.dunno.blame.objects.items.ImpEnergy;
import su.msk.dunno.blame.objects.items.ImpLaser;
import su.msk.dunno.blame.objects.items.ImpSocketExtender;
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
import su.msk.dunno.blame.support.StateMap;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class WeaponScreen implements IScreen
{
	private int weapon_width = 40;
	private int weapon_height = 20;
	
	private ALiving owner;
	private EventManager weaponEvents = new EventManager();
	private AObject[][] weaponView = new AObject[weapon_width][weapon_height];
	private LinkedList<AObject> sockets = new LinkedList<AObject>();
	
	private StateMap modifiers = new StateMap();
	private StateMap effects = new StateMap();
	private StateMap bonuses = new StateMap();
	
	private MinorSelector selector = new MinorSelector(0, 0);
	private boolean isSelectSocket;
	
	private boolean isWeaponOpen;
	private boolean isShowWeapon;	// if false - show weapon stats
	
	private float maxEnergy;
	private float energy;
	public float energy_fill_rate;
	
	//private float damage;
	
	public WeaponScreen(ALiving l)
	{
		owner = l;
		initEvents();
		initWeaponView();
		//fillWeapon(11);
	}
	
	public void process()
	{
		Messages.instance().clear();
		isWeaponOpen = true;
		isShowWeapon = false;
		while(isWeaponOpen)
		{
			weaponEvents.checkEvents();			
			showWeapon();
		}
	}
	
	private void showWeapon()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
		GL11.glLoadIdentity();
		
		TrueTypeFont.instance().drawString(owner.getName()+"'s weapon", 20, Blame.height-25, Color.WHITE);
		int k = Blame.height-55;
		
		if(isShowWeapon)
		{
			if(isSelectSocket)
			{
				k -= 15;
				TrueTypeFont.instance().drawString(weaponView[selector.curPos.x][selector.curPos.y].getName(), 20, k, 
											 	   weaponView[selector.curPos.x][selector.curPos.y].getColor());
			}		
			GL11.glTranslatef((Blame.width - weapon_width*100*3/4/5)/2, (Blame.height - weapon_height*100/5)/2, 0.0f);
			GL11.glScalef(0.2f, 0.2f, 1.0f);
			for(int i = 0; i < weapon_width; i++)
			{
				for(int j = 0; j < weapon_height; j++)
				{
					if(!"Empty".equals(weaponView[i][j].getName()) && 
					  (!"SocketPlace".equals(weaponView[i][j].getName()) || isSelectSocket))
						MyFont.instance().drawDisplayList(/*isSelectSocket?*/weaponView[i][j].getSymbol()/*:MyFont.WEAPONBASE*/, 
														  i*100*3/4, j*100, 
												   		  weaponView[i][j].getColor());
				}
			}
			if(isSelectSocket)
			{
				MyFont.instance().drawDisplayList(selector.getSymbol(), 
				 								  selector.curPos.x*100*3/4, 
				 								  selector.curPos.y*100, 
				 								  selector.getColor());
			}
			Messages.instance().showMessages();
		}
		else
		{
			for(String key: effects.getKeys())
			{
				TrueTypeFont.instance().drawString(key+": "+effects.getInt(key)+
												   (bonuses.containsKey(key)?" (+"+(int)(bonuses.getFloat(key)/effects.getFloat(key)*100)+"% bonus)":""), 
												   20, k, effects.getColor(key)); k -= 15;
			}
			for(String key: modifiers.getKeys())
			{
				TrueTypeFont.instance().drawString(key+": "+modifiers.getInt(key)+
												   (bonuses.containsKey(key)?" (+"+(int)(bonuses.getFloat(key)/modifiers.getFloat(key)*100)+"% bonus)":""), 
												   20, k, modifiers.getColor(key)); k -= 15;
			}
		}
		
		Display.sync(Blame.framerate);
		Display.update();
	}
	
	private void calculateEffects()
	{
		modifiers.clear();
		effects.clear();
		bonuses.clear();
		for(AObject socket: sockets)
		{
			if(socket.getState().containsKey("Effect")) 
			{
				String effect = socket.getState().getString("Effect");
				addEffect(effects, effect, socket);
			}
			else if(socket.getState().containsKey("Modifier")) 
			{
				String modifier = socket.getState().getString("Modifier");
				addEffect(modifiers, modifier, socket);
			}
		}
		
		if(modifiers.containsKey("Energy")) maxEnergy = modifiers.getFloat("Energy");
		else maxEnergy = 0;
		if(energy > maxEnergy) energy = maxEnergy;
		energy_fill_rate = maxEnergy/20.0f;
	}
	
	private void addEffect(StateMap container, String effect, AObject socket)
	{
		float toAdd = socket.getState().getFloat(effect);
		int sameImps = sameImpsNear(socket, socket.curPos, null);
		if(container.containsKey(effect))
		{
			float value = container.getFloat(effect) + toAdd;
			if(sameImps > 0)
			{
				container.putFloat(effect, (value+sameImps*toAdd/10.0f));
				if(bonuses.containsKey(effect))
					bonuses.putFloat(effect, (bonuses.getFloat(effect)+sameImps*toAdd/10.0f));
				else 
					bonuses.putFloat(effect, sameImps*toAdd/10.0f);
			}
			else container.putFloat(effect, value);
		}
		else
		{
			if(sameImps > 0)
			{
				container.putFloat(effect, (toAdd+sameImps*toAdd/10.0f));
				if(bonuses.containsKey(effect)) bonuses.putFloat(effect, (bonuses.getFloat(effect)+sameImps*toAdd/10.0f));
				else bonuses.putFloat(effect, sameImps*toAdd/10.0f);			
			}
			else container.putFloat(effect, toAdd);
			container.putColor(effect, socket.getColor());
		}
	}
	
	public int getEffect(String effectName)
	{
		if(effects.containsKey(effectName)) return effects.getInt(effectName);
		else return 0;
	}
	
	public int getModifier(String modifierName)
	{
		if(modifiers.containsKey(modifierName)) return modifiers.getInt(modifierName);
		else return 0;
	}
	
	public StateMap applyEffects()
	{
		if(energy - 1 > 0)
		{
			energy -= Math.max(getDamage()/10, 1);
			return effects;
		}
		else return null;
	}
	
	public StateMap showEffects()
	{
		return effects;
	}
	
	public String showEnergy()
	{
		return (int)energy+"/"+(int)maxEnergy;
	}
	
	public int getDamage()
	{
		return (int)(effects.getFloat("AcidDamage") + 
					 effects.getFloat("BioDamage") +
					 effects.getFloat("ElectroDamage") +
					 effects.getFloat("LaserDamage"));
	}
	
	public void energyRefill()
	{
		if(energy + energy_fill_rate < maxEnergy)energy += energy_fill_rate;
		else energy = maxEnergy;
	}
	
	public void addImp(AObject ao)
	{
		addImp(ao, false);
	}
	
	public void addImpRandom(AObject ao)
	{
		addImp(ao, true);
	}
	
	public void addImp(AObject ao, boolean isRandomPlace)
	{
		if(isRandomPlace)
		{
			Point p = getRandomFreeSocket();
			if(p != null)
			{
				selector.curPos = p;
				ao.curPos = p;
			}
			else return;
		}
		else ao.curPos = selector.curPos;
		sockets.add(ao);
		weaponView[selector.curPos.x][selector.curPos.y] = ao;
		if(ao.getState().containsKey("Extender"))
		{
			addNewSockets(ao);
		}
		calculateEffects();
	}
	
	private void addNewSockets(AObject ao) 
	{
		for(int i = Math.max(0, ao.curPos.x-1); i <= Math.min(weaponView.length-1, ao.curPos.x+1); i++)
		{
			for(int j = Math.max(0, ao.curPos.y-1); j <= Math.min(weaponView[0].length-1, ao.curPos.y+1); j++)
			{
				if("Empty".equals(weaponView[i][j].getName()) && !isRestrictedPlace(i, j) && !isDiagonal(ao.curPos, new Point(i,j)))
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
	
	public boolean removeImp(AObject socket)
	{
		sockets.remove(socket);
		weaponView[socket.curPos.x][socket.curPos.y] = new SocketSymbol(socket.curPos);
		calculateEffects();
		if(socket.getState().containsKey("Extender")) removeSockets(socket);
		if(!owner.getInventory().isFull())
		{			
			owner.getInventory().addItem(socket);			
			return true;
		}
		else 
		{
			Messages.instance().addPropMessage("weapon.inventoryfull");
			owner.setDecision(new Drop(owner, socket));
			return false;
		}
	}

	private void removeSockets(AObject ao) 
	{
		for(int i = Math.max(0, ao.curPos.x-1); i <= Math.min(weaponView.length-1, ao.curPos.x+1); i++)
		{
			for(int j = Math.max(0, ao.curPos.y-1); j <= Math.min(weaponView[0].length-1, ao.curPos.y+1); j++)
			{
				if("SocketPlace".equals(weaponView[i][j].getName()))
				{
					if(noBasePartsNear(weaponView[i][j], null))weaponView[i][j] = new EmptySpace(i,j);
				}
				else if(weaponView[i][j].getState().containsKey("Imp"))
				{
					if(noBasePartsNear(weaponView[i][j], null))
					{
						weaponView[i][j] = new EmptySpace(i,j);
						removeImp(weaponView[i][j]);
					}
				}
			}
		}
	}
	
	private int sameImpsNear(AObject ao, Point initP, AObject prev)
	{
		if(ao.curPos.equals(initP) && prev != null)return 0;	// prevents circularity
		int num = 0;
		int x = ao.curPos.x;
		int y = ao.curPos.y;
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
		int x = ao.curPos.x;
		int y = ao.curPos.y;
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

	private void initWeaponView()
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
	
	private void fillWeapon(int num)
	{
		int amount = Math.min(getFreeSocketsNum(), num);
		Point p;
		int rand;
		AObject ao;
		/*p = getRandomFreeSocket();
		if(p != null)
		{
			selector.cur_pos = p;
			addImp(new ImpMind(p));
		}*/
		for(int i = 0; i < amount-1; i++)
		{
			p = getRandomFreeSocket();
			if(p != null)
			{
				rand = (int)(Math.random()*5);
				selector.curPos = p;
				switch(rand)
				{
				case 0: ao = new ImpBio(p); addImp(ao); continue;
				case 1: ao = new ImpLaser(p); addImp(ao); continue;
				case 2: ao = new ImpEnergy(p); addImp(ao); continue;
				case 3: ao = new ImpAcid(p); addImp(ao); continue;
				case 4: ao = new ImpSocketExtender(p); addImp(ao); continue;			
				}
			}
		}
		energy = maxEnergy;
		energy_fill_rate = maxEnergy/20.0f;
	}
	
	public int getFreeSocketsNum()
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
	
	private Point getRandomFreeSocket()
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
	
	private boolean isFreeSocket(Point p)
	{
		return "SocketPlace".equalsIgnoreCase(weaponView[p.x][p.y].getName());
	}
	
	public Color getDamageColor() 
	{
		float max_damage_type = Math.max(Math.max(effects.getFloat("AcidDamage"), effects.getFloat("BioDamage")), 
										 Math.max(effects.getFloat("ElectroDamage"), effects.getFloat("LaserDamage")));
		if(max_damage_type == effects.getFloat("AcidDamage")) return Color.CYAN;
		else if(max_damage_type == effects.getFloat("BioDamage")) return Color.GREEN;
		else if(max_damage_type == effects.getFloat("ElectroDamage")) return Color.BLUE_VIOLET;
		else return Color.PURPLE;
	}

	public int getImpAmount() 
	{
		return sockets.size();
	}
		
	private void initEvents()
	{
		weaponEvents.addListener(Keyboard.KEY_NUMPAD9, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.x < weaponView.length-1 && selector.curPos.y < weaponView[0].length-1)
        		{
        			selector.curPos = selector.curPos.plus(1,1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_UP, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.y < weaponView[0].length-1)
        		{
        			selector.curPos = selector.curPos.plus(0,1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD8, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.y < weaponView[0].length-1)
        		{
        			selector.curPos = selector.curPos.plus(0,1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD7, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.x > 0 && selector.curPos.y < weaponView[0].length-1)
        		{
        			selector.curPos = selector.curPos.plus(-1,1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_RIGHT, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.x < weaponView.length-1)
        		{
        			selector.curPos = selector.curPos.plus(1,0);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD6, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.x < weaponView.length-1)
        		{
        			selector.curPos = selector.curPos.plus(1,0);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_LEFT, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.x > 0)
        		{
        			selector.curPos = selector.curPos.plus(-1,0);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD4, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.x > 0)
        		{
        			selector.curPos = selector.curPos.plus(-1,0);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD3, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.x < weaponView.length-1 && selector.curPos.y > 0)
        		{
        			selector.curPos = selector.curPos.plus(1,-1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_DOWN, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.y > 0)
        		{
        			selector.curPos = selector.curPos.plus(0,-1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD2, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.y > 0)
        		{
        			selector.curPos = selector.curPos.plus(0,-1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_NUMPAD1, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		if(selector.curPos.x > 0 && selector.curPos.y > 0)
        		{
        			selector.curPos = selector.curPos.plus(-1,-1);
        			isSelectSocket = true;
        		}
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_RETURN, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if("SocketPlace".equals(weaponView[selector.curPos.x][selector.curPos.y].getName()))
        		{
        			owner.getInventory().setMode(InventoryScreen.TO_SELECT_IMP);
        			owner.getInventory().process();
        		}
        		else if("Weapon Sceleton".equals(weaponView[selector.curPos.x][selector.curPos.y].getName()))
        		{
        			Messages.instance().addPropMessage("weapon.cantremove");
        		}
        		else if(weaponView[selector.curPos.x][selector.curPos.y].getState().containsKey("Imp"))
        		{
        			removeImp(weaponView[selector.curPos.x][selector.curPos.y]);
        		}
        		else
        			Messages.instance().addPropMessage("weapon.nosocket");
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_ESCAPE, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(isSelectSocket)isSelectSocket = false;
        		else isWeaponOpen = false;
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_W, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		isShowWeapon = !isShowWeapon;
        	}
        });
	}
}
