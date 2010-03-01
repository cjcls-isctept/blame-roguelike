package su.msk.dunno.blame.screens;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.decisions.Drop;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class Inventory implements IScreen
{
	public static final int TO_DROP = 0;
	public static final int TO_CHECK = 1;
	public static final int TO_SELECT_SOCKET = 2;
	
	private ALiving owner;
	private Field field;
	private LinkedList<AObject> items = new LinkedList<AObject>();
	private EventManager inventoryEvents = new EventManager();
	private int inventoryCapacity = 10;
	
	private boolean showInventory;
	private int mode;
	
	public AObject selectedItem;
	
	public Inventory(ALiving l, Field field)
	{
		owner = l;
		this.field = field;
		initEvents();
	}
	
	public void process()
	{
		inventoryEvents.checkEvents();
		showInventory();
	}
	
	private void showInventory()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
		GL11.glLoadIdentity();
		int k = Blame.height-25;
		int num = 1;
		TrueTypeFont.instance().drawString(owner.getName()+"'s inventory", 20, k, Color.WHITE); k -= 30;
		if(selectedItem != null)
		{
			TrueTypeFont.instance().drawString(selectedItem.getName()+":", 20, k, Color.WHITE); k -= 30;
			TrueTypeFont.instance().drawString(selectedItem.getState().get("Info"), 20, k, Color.WHITE);
		}
		else if(mode == TO_SELECT_SOCKET)
		{
			for(AObject i: items)
			{
				if(i.getState().containsKey("Part"))
				{
					TrueTypeFont.instance().drawString(num+". "+i.getName(), 20, k, i.getColor()); k -= 15;
					num++;
				}
			}
		}
		else
		{
			for(AObject i: items)
			{
				TrueTypeFont.instance().drawString(num+". "+i.getName(), 20, k, i.getColor()); k -= 15;
				num++;
			}
		}
		Display.sync(Blame.framerate);
		Display.update();
	}
	
	public boolean addItem(AObject o)
	{
		if(o.getState().containsKey("Item") && !isFull())
		{
			items.add(o);
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
		Messages.instance().clear();
		this.mode = mode;
		showInventory = true;		
	}

	private AObject getItem(int mode, int num)
	{
		switch(mode)
		{
		case TO_SELECT_SOCKET:
			int i = 0;
			for(AObject ao: items)
			{
				if(ao.getState().containsKey("Part"))
				{
					if(i == num)return ao;
					else
					{
						i++;
						continue;
					}
				}
			}
			break;
		}
		return null;
	}
	
	public void initEvents()
	{
		inventoryEvents.addListener(Keyboard.KEY_1, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(mode == TO_DROP)
    			{
        			if(items.size() > 0)owner.setDecision(new Drop(owner, items.get(0)));
            		closeInventory();
    			}
        		else if(mode == TO_SELECT_SOCKET)
    			{
    				AObject item = getItem(TO_SELECT_SOCKET, 0);
    				if(item != null)
    				{
    					owner.weapon.addImp(item);
    					items.remove(item);
    				}
    				closeInventory();
    			}
    			else
    			{
    				if(items.size() > 0)selectedItem = items.get(0);
    			}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_2, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(mode == TO_DROP)
    			{
        			if(items.size() > 1)owner.setDecision(new Drop(owner, items.get(1)));
            		closeInventory();
    			}
        		else if(mode == TO_SELECT_SOCKET)
    			{
    				AObject item = getItem(TO_SELECT_SOCKET, 1);
    				if(item != null)
    				{
    					owner.weapon.addImp(item);
    					items.remove(item);
    				}
    				closeInventory();
    			}
    			else
    			{
    				if(items.size() > 1)selectedItem = items.get(1);
    			}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_3, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(mode == TO_DROP)
    			{
        			if(items.size() > 2)owner.setDecision(new Drop(owner, items.get(2)));
            		closeInventory();
    			}
        		else if(mode == TO_SELECT_SOCKET)
    			{
    				AObject item = getItem(TO_SELECT_SOCKET, 2);
    				if(item != null)
    				{
    					owner.weapon.addImp(item);
    					items.remove(item);
    				}
    				closeInventory();
    			}
    			else
    			{
    				if(items.size() > 2)selectedItem = items.get(2);
    			}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_4, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(mode == TO_DROP)
    			{
        			if(items.size() > 3)owner.setDecision(new Drop(owner, items.get(3)));
            		closeInventory();
    			}
        		else if(mode == TO_SELECT_SOCKET)
    			{
    				AObject item = getItem(TO_SELECT_SOCKET, 3);
    				if(item != null)
    				{
    					owner.weapon.addImp(item);
    					items.remove(item);
    				}
    				closeInventory();
    			}
    			else
    			{
    				if(items.size() > 3)selectedItem = items.get(3);
    			}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_5, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(mode == TO_DROP)
    			{
        			if(items.size() > 4)owner.setDecision(new Drop(owner, items.get(4)));
            		closeInventory();
    			}
        		else if(mode == TO_SELECT_SOCKET)
    			{
    				AObject item = getItem(TO_SELECT_SOCKET, 4);
    				if(item != null)
    				{
    					owner.weapon.addImp(item);
    					items.remove(item);
    				}
    				closeInventory();
    			}
    			else
    			{
    				if(items.size() > 4)selectedItem = items.get(4);
    			}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_6, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(mode == TO_DROP)
    			{
        			if(items.size() > 5)owner.setDecision(new Drop(owner, items.get(5)));
            		closeInventory();
    			}
        		else if(mode == TO_SELECT_SOCKET)
    			{
    				AObject item = getItem(TO_SELECT_SOCKET, 5);
    				if(item != null)
    				{
    					owner.weapon.addImp(item);
    					items.remove(item);
    				}
    				closeInventory();
    			}
    			else
    			{
    				if(items.size() > 5)selectedItem = items.get(5);
    			}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_7, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(mode == TO_DROP)
    			{
        			if(items.size() > 6)owner.setDecision(new Drop(owner, items.get(60)));
            		closeInventory();
    			}
        		else if(mode == TO_SELECT_SOCKET)
    			{
    				AObject item = getItem(TO_SELECT_SOCKET, 6);
    				if(item != null)
    				{
    					owner.weapon.addImp(item);
    					items.remove(item);
    				}
    				closeInventory();
    			}
    			else
    			{
    				if(items.size() > 6)selectedItem = items.get(6);
    			}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_8, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(mode == TO_DROP)
    			{
        			if(items.size() > 7)owner.setDecision(new Drop(owner, items.get(7)));
            		closeInventory();
    			}
        		else if(mode == TO_SELECT_SOCKET)
    			{
    				AObject item = getItem(TO_SELECT_SOCKET, 7);
    				if(item != null)
    				{
    					owner.weapon.addImp(item);
    					items.remove(item);
    				}
    				closeInventory();
    			}
    			else
    			{
    				if(items.size() > 7)selectedItem = items.get(7);
    			}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_9, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(mode == TO_DROP)
    			{
        			if(items.size() > 8)owner.setDecision(new Drop(owner, items.get(8)));
            		closeInventory();
    			}
        		else if(mode == TO_SELECT_SOCKET)
    			{
    				AObject item = getItem(TO_SELECT_SOCKET, 8);
    				if(item != null)
    				{
    					owner.weapon.addImp(item);
    					items.remove(item);
    				}
    				closeInventory();
    			}
    			else
    			{
    				if(items.size() > 8)selectedItem = items.get(8);
    			}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_0, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(mode == TO_DROP)
    			{
        			if(items.size() > 9)owner.setDecision(new Drop(owner, items.get(9)));
            		closeInventory();
    			}
        		else if(mode == TO_SELECT_SOCKET)
    			{
    				AObject item = getItem(TO_SELECT_SOCKET, 9);
    				if(item != null)
    				{
    					owner.weapon.addImp(item);
    					items.remove(item);
    				}
    				closeInventory();
    			}
    			else
    			{
    				if(items.size() > 9)selectedItem = items.get(9);
    			}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_ESCAPE, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(selectedItem != null)selectedItem = null;
        		else closeInventory();
        	}
        });
	}
}
