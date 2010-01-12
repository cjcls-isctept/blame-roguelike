package su.msk.dunno.blame.containers;

import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.decisions.Drop;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.listeners.EventManager;
import su.msk.dunno.blame.main.support.listeners.KeyListener;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;

public class Inventory 
{
	public static final int TO_DROP = 0;
	public static final int TO_CHECK = 1;
	
	private ALiving owner;
	private Field field;
	private LinkedList<AObject> items;
	private EventManager inventoryEvents;
	private int inventoryCapacity = 5;
	
	private boolean showInventory;
	private int mode;
	
	public AObject selectedItem;
	
	public Inventory(ALiving l, Field field)
	{
		owner = l;
		this.field = field;
		items = new LinkedList<AObject>();
		initEvents();
	}
	
	public void process()
	{
		inventoryEvents.checkEvents();
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
		GL11.glLoadIdentity();
		showInventory();
		Display.sync(Blame.framerate);
		Display.update();
	}
	
	private void showInventory()
	{
		int k = 460;
		int num = 1;
		MyFont.instance().drawString(owner.getName()+"'s inventory", 20, k, 0.2f, Color.WHITE); k -= 30; 
		for(AObject i: items)
		{
			MyFont.instance().drawString(num+". "+i.getName(), 20, k, 0.2f, Color.WHITE); k -= 15;
			num++;
		}
		if(selectedItem != null)
		{
			int pos = 430-15*items.indexOf(selectedItem);
			MyFont.instance().drawString("(d)rop/(e)quip?", (selectedItem.getName().length()+3)*12+36, pos, 0.2f, Color.WHITE);
		}
	}
	
	public boolean addItem(AObject o)
	{
		if(o.getState().containsKey("Item") && !isFull())
		{
			items.add(o);
			field.removeObject(o);
			return true;
		}
		return false;
	}
	
	public void removeItem(AObject o)
	{
		items.remove(o);
		field.addObject(owner.cur_pos, o);
	}
	
	public boolean isFull()
	{
		return items.size() == inventoryCapacity;
	}
	
	public boolean isOpen()
	{
		return showInventory;
	}
	
	private void closeInventory()
	{
		selectedItem = null;
		showInventory = false;
	}
	
	public void openInventory(int mode)
	{
		this.mode = mode;
		showInventory = true;
		
	}
	
	public void initEvents()
	{
		inventoryEvents = new EventManager();
		inventoryEvents.addListener(Keyboard.KEY_1, new KeyListener()
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 0)
        		{
        			selectedItem = items.get(0);
        			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, selectedItem));
                		closeInventory();
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_2, new KeyListener()
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 1)
        		{
        			selectedItem = items.get(1);
        			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, selectedItem));
                		closeInventory();
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_3, new KeyListener()
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 2)
        		{
        			selectedItem = items.get(2);
        			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, selectedItem));
                		closeInventory();
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_4, new KeyListener()
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 3)
        		{
        			selectedItem = items.get(3);
        			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, selectedItem));
                		closeInventory();
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_5, new KeyListener()
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 4)
        		{
        			selectedItem = items.get(4);
        			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, selectedItem));
                		closeInventory();
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_D, new KeyListener()
        {
        	public void onKeyDown()
        	{
        		if(selectedItem != null)
        		{
        			owner.setDecision(new Drop(owner, selectedItem));
        			closeInventory();
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_ESCAPE, new KeyListener()
        {
        	public void onKeyDown()
        	{
        		closeInventory();
        	}
        });
	}
}
