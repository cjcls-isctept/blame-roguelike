package su.msk.dunno.blame.containers;

import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.Color;
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
	private AObject[][] weaponView = new AObject[Blame.N_x][Blame.N_y];
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
		for(int i = 0; i < Blame.N_x; i++)
		{
			for(int j = 0; j < Blame.N_y; j++)
			{
				MyFont.instance().drawChar(weaponView[i][j].getSymbol(), i*20, j*20, 0.2f, weaponView[i][j].getColor());
			}
		}
		if(isSelectSocket)MyFont.instance().drawChar(selector.getSymbol(), selector.cur_pos.x*20, selector.cur_pos.y*20, 0.2f, selector.getColor());
		Display.sync(Blame.framerate);
		Display.update();
	}
	
	public HashMap<String, String> getDamage()
	{
		return null;
	}
	
	public void addSocket(AObject ao)
	{
		sockets.add(ao);
		weaponView[selector.cur_pos.x][selector.cur_pos.y] = ao;
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
		isWeaponView = true;
		
	}
		
	public void initEvents()
	{
		weaponEvents.addListener(Keyboard.KEY_UP, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		selector.cur_pos = selector.cur_pos.plus(new Point(0, 1));
        		isSelectSocket = true;
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_DOWN, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		selector.cur_pos = selector.cur_pos.plus(new Point(0, -1));
        		isSelectSocket = true;
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_RIGHT, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		selector.cur_pos = selector.cur_pos.plus(new Point(1, 0));
        		isSelectSocket = true;
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_LEFT, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		selector.cur_pos = selector.cur_pos.plus(new Point(-1, 0));
        		isSelectSocket = true;
        	}
        });
		weaponEvents.addListener(Keyboard.KEY_RETURN, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		inventory.openInventory(Inventory.TO_SELECT_SOCKET);
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
