package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class SelectLook extends SelectTarget 
{
	public SelectLook(ALiving al, Field field, ADecision actionAfter) 
	{
		super(al, field, actionAfter);
	}
	
	@Override public void process() 
	{
		while(isSelectTarget)
		{
			selectEvents.checkEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
			GL11.glLoadIdentity();
			field.draw(al.cur_pos);
			field.drawLine(selectLine);
			Blame.getCurrentPlayer().drawStats();
			Messages.instance().showMessages();
			if(selectLine.size() > 0)
			{
				String name = field.getObjectsAtPoint(selectPoint).getLast().getName();
				boolean isHostile = field.getObjectsAtPoint(selectPoint).getLast().isEnemy(al);
				TrueTypeFont.instance().drawString(name+(isHostile?" (Hostile!)":""), 20, 90);
			}
			Display.sync(Blame.framerate);
			Display.update();
		}
	}
	
	public void initEvents()
	{
		selectEvents.addListener(Keyboard.KEY_UP, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y+1);
				buildLine();
			}
		});
		selectEvents.addListener(Keyboard.KEY_NUMPAD9, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y+1);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_NUMPAD8, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y+1);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_NUMPAD7, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y+1);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_RIGHT, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_NUMPAD6, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_LEFT, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_NUMPAD4, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_DOWN, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y-1);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_NUMPAD3, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y-1);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_NUMPAD2, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y-1);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_NUMPAD1, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y-1);
				buildLine();
			}
		});		
		selectEvents.addListener(Keyboard.KEY_ESCAPE, new KeyListener(0)
		{
			public void onKeyDown()
			{
				clearLine();
				isSelectTarget = false;
				al.changeState(al, new StateMap("MoveFail"));
			}
		});
		selectEvents.addListener(Keyboard.KEY_L, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(selectLine.size() > 0)
				{
					clearLine();
					isSelectTarget = false;
					al.changeState(al, new StateMap("MoveFail"));
				}
				else
				{
					selectPoint = al.cur_pos;
					buildLine();
				}
			}
		});
	}
}
