package su.msk.dunno.blame.screens;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.decisions.Drop;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Messages;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.listeners.EventManager;
import su.msk.dunno.blame.main.support.listeners.KeyListener;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreenInterface;

public class Inventory implements IScreenInterface
{
	public static final int TO_DROP = 0;
	public static final int TO_CHECK = 1;
	public static final int TO_SELECT_SOCKET = 2;
	
	private ALiving owner;
	private Field field;
	private LinkedList<AObject> items;
	private EventManager inventoryEvents = new EventManager();
	private int inventoryCapacity = 10;
	
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
		showInventory();
	}
	
	private void showInventory()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
		GL11.glLoadIdentity();
		int k = 460;
		int num = 1;
		MyFont.instance().drawString(owner.getName()+"'s inventory", 20, k, 0.2f, Color.WHITE); k -= 30;
		if(selectedItem != null)
		{
			int pos = 430-15*items.indexOf(selectedItem);
			MyFont.instance().drawString(selectedItem.getState().get("Info"), (selectedItem.getName().length()+3)*12+36, pos, 0.2f, Color.WHITE);
		}
		else if(mode == TO_SELECT_SOCKET)
		{
			for(AObject i: items)
			{
				if(i.getState().containsKey("Part"))
				{
					MyFont.instance().drawString(num+". "+i.getName(), 20, k, 0.2f, Color.WHITE); k -= 15;
					num++;
				}
			}
		}
		else
		{
			for(AObject i: items)
			{
				MyFont.instance().drawString(num+". "+i.getName(), 20, k, 0.2f, Color.WHITE); k -= 15;
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
		Messages.instance().clear();
		this.mode = mode;
		showInventory = true;
		
	}
	
	public void initEvents()
	{
		inventoryEvents.addListener(Keyboard.KEY_1, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 0)
        		{
        			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, items.get(0)));
                		closeInventory();
        			}
        			else if(mode == TO_SELECT_SOCKET)
        			{
        				owner.weapon.addPart(items.get(0));
        				items.remove(0);
        				closeInventory();
        			}
        			else
        			{
        				selectedItem = items.get(0);
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_2, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 1)
        		{
          			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, items.get(1)));
                		closeInventory();
        			}
          			else if(mode == TO_SELECT_SOCKET)
        			{
        				owner.weapon.addPart(items.get(1));
        				items.remove(1);
        				closeInventory();
        			}
        			else
        			{
        				selectedItem = items.get(1);
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_3, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 2)
        		{
          			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, items.get(2)));
                		closeInventory();
        			}
          			else if(mode == TO_SELECT_SOCKET)
        			{
        				owner.weapon.addPart(items.get(2));
        				items.remove(2);
        				closeInventory();
        			}
        			else
        			{
        				selectedItem = items.get(2);
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_4, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 3)
        		{
          			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, items.get(3)));
                		closeInventory();
        			}
          			else if(mode == TO_SELECT_SOCKET)
        			{
        				owner.weapon.addPart(items.get(3));
        				items.remove(3);
        				closeInventory();
        			}
        			else
        			{
        				selectedItem = items.get(3);
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_5, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 4)
        		{
          			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, items.get(4)));
                		closeInventory();
        			}
          			else if(mode == TO_SELECT_SOCKET)
        			{
        				owner.weapon.addPart(items.get(4));
        				items.remove(4);
        				closeInventory();
        			}
        			else
        			{
        				selectedItem = items.get(4);
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_6, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 5)
        		{
          			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, items.get(5)));
                		closeInventory();
        			}
          			else if(mode == TO_SELECT_SOCKET)
        			{
        				owner.weapon.addPart(items.get(5));
        				items.remove(5);
        				closeInventory();
        			}
        			else
        			{
        				selectedItem = items.get(5);
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_7, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 6)
        		{
          			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, items.get(6)));
                		closeInventory();
        			}
          			else if(mode == TO_SELECT_SOCKET)
        			{
        				owner.weapon.addPart(items.get(6));
        				items.remove(6);
        				closeInventory();
        			}
        			else
        			{
        				selectedItem = items.get(6);
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_8, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 7)
        		{
          			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, items.get(7)));
                		closeInventory();
        			}
          			else if(mode == TO_SELECT_SOCKET)
        			{
        				owner.weapon.addPart(items.get(7));
        				items.remove(7);
        				closeInventory();
        			}
        			else
        			{
        				selectedItem = items.get(7);
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_9, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 8)
        		{
          			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, items.get(8)));
                		closeInventory();
        			}
          			else if(mode == TO_SELECT_SOCKET)
        			{
        				owner.weapon.addPart(items.get(8));
        				items.remove(8);
        				closeInventory();
        			}
        			else
        			{
        				selectedItem = items.get(8);
        			}
        		}
        	}
        });
		inventoryEvents.addListener(Keyboard.KEY_0, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(items.size() > 9)
        		{
          			if(mode == TO_DROP)
        			{
        				owner.setDecision(new Drop(owner, items.get(9)));
                		closeInventory();
        			}
          			else if(mode == TO_SELECT_SOCKET)
        			{
        				owner.weapon.addPart(items.get(9));
        				items.remove(9);
        				closeInventory();
        			}
        			else
        			{
        				selectedItem = items.get(9);
        			}
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
