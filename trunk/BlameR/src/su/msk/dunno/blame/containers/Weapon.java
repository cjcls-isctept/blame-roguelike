package su.msk.dunno.blame.containers;

import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.decisions.Drop;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Messages;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.main.support.listeners.EventManager;
import su.msk.dunno.blame.main.support.listeners.KeyListener;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.symbols.EmptySpace;
import su.msk.dunno.blame.symbols.MinorSelector;
import su.msk.dunno.blame.symbols.SocketSymbol;
import su.msk.dunno.blame.symbols.WeaponBase;

public class Weapon 
{
	private ALiving owner;
	private Inventory inventory;
	private EventManager weaponEvents = new EventManager();
	private AObject[][] weaponView = new AObject[20][20];
	private LinkedList<AObject> sockets = new LinkedList<AObject>();
	
	private MinorSelector selector = new MinorSelector(0, 0);
	private boolean isSelectSocket;
	
	private boolean isWeaponView;
	
	public Weapon(ALiving l, Inventory inv)
	{
		owner = l;
		inventory = inv;
		initEvents();
		initWeaponView();
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
		GL11.glTranslatef(120, 20, 0.0f);
		MyFont.instance().drawString(owner.getName()+"'s weapon", 20, 460, 0.2f, Color.WHITE);
		for(int i = 0; i < 20; i++)
		{
			for(int j = 0; j < 20; j++)
			{
				if(!"SocketPlace".equals(weaponView[i][j].getName()) || isSelectSocket)
					MyFont.instance().drawChar(weaponView[i][j].getSymbol(), i*20, j*20, 0.2f, 
											   weaponView[i][j].getColor());
			}
		}
		if(isSelectSocket)MyFont.instance().drawChar(selector.getSymbol(), selector.cur_pos.x*20, selector.cur_pos.y*20, 0.2f, selector.getColor());
		Messages.instance().showMessages();
		Display.sync(Blame.framerate);
		Display.update();
	}
	
	public HashMap<String, String> getDamage()
	{
		return null;
	}
	
	public void addPart(AObject ao)
	{
		ao.cur_pos = selector.cur_pos;
		sockets.add(ao);
		weaponView[selector.cur_pos.x][selector.cur_pos.y] = ao;
		if("Socket Extender".equals(ao.getName()))
		{
			addNewSockets(ao);
		}
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
	
	public boolean removePart(AObject ao)
	{
		sockets.remove(ao);
		weaponView[ao.cur_pos.x][ao.cur_pos.y] = new SocketSymbol(ao.cur_pos);
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
					if(noBasePartsNear(weaponView[i][j]))weaponView[i][j] = new EmptySpace(i,j);
				}
				else if(weaponView[i][j].getState().containsKey("Part"))
				{
					if(noBasePartsNear(weaponView[i][j]))
					{
						removePart(weaponView[i][j]);
						weaponView[i][j] = new EmptySpace(i,j);
					}
				}
			}
		}
	}

	private boolean noBasePartsNear(AObject ao) 
	{
		for(int i = Math.max(0, ao.cur_pos.x-1); i <= Math.min(weaponView.length-1, ao.cur_pos.x+1); i++)
		{
			for(int j = Math.max(0, ao.cur_pos.y-1); j <= Math.min(weaponView[0].length-1, ao.cur_pos.y+1); j++)
			{
				if(!ao.equals(weaponView[i][j]))
				{
					String name = weaponView[i][j].getName();
					if(("Weapon Sceleton".equals(name) || "Socket Extender".equals(name)) && 
						!isDiagonal(ao.cur_pos, new Point(i,j)))return false;
				}
			}
		}
		return true;
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
	
	private void closeWeaponView()
	{
		isWeaponView = false;
	}
	
	public void openWeaponView()
	{
		Messages.instance().clear();
		isWeaponView = true;
		
	}
		
	public void initEvents()
	{
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
        			if(!removePart(weaponView[selector.cur_pos.x][selector.cur_pos.y]))
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
        		else closeWeaponView();
        	}
        });
	}
}
