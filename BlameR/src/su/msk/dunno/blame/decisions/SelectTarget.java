package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.symbols.MainSelector;
import su.msk.dunno.blame.objects.symbols.MinorSelector;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class SelectTarget extends ADecision implements IScreen 
{
	protected Field field; 
	protected EventManager selectEvents = new EventManager();
	protected ADecision actionAfter;
	
	protected Point selectPoint;
	protected LinkedList<AObject> selectLine = new LinkedList<AObject>();
	
	protected boolean isSelectTarget;
	
	public SelectTarget(ALiving al, Field field, ADecision actionAfter) 
	{
		super(al);
		this.field = field;
		this.actionAfter = actionAfter;
		initEvents();
	}
	
	@Override public void doAction(int actionMoment) 
	{
		isSelectTarget = true;
		process();
		wasExecuted = true;
	}
	
	public void process() 
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
		
		selectEvents.addListener(Keyboard.KEY_F, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(selectLine.size() > 0)
				{
					clearLine();
					actionAfter.setSelectPoint(selectPoint);
					al.setDecision(actionAfter);
					isSelectTarget = false;
				}
				else
				{
					selectPoint = al.cur_pos;
					LinkedList<AObject> neighbours = al.getMyNeighbours();
					for(AObject ao: neighbours)
					{
						if(al.isEnemy(ao) || ao.isEnemy(al))
						{
							selectPoint = ao.cur_pos;
							break;
						}
					}
					buildLine();
				}
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
	}
	
	protected void buildLine()
	{
		clearLine();
		LinkedList<Point> line = field.getLine(al.cur_pos, selectPoint);
		int i = 0;
		for(Point p: line)
		{
			i++;
			if(line.size() > 1 && i == 1)continue;	//	skip the first element if amount of elements is more than 1
			if(field.onArea(p) && field.isMapPointVisible(p, al.cur_pos, al.getDov()))
			{
				MinorSelector s = new MinorSelector(p);
				selectLine.add(s);
			}
			else break;
		}
		if(selectLine.size() > 0)
		{
			selectLine.set(selectLine.size()-1, new MainSelector(selectLine.getLast().cur_pos));
			selectPoint = selectLine.getLast().cur_pos;
		}
		for(AObject s: selectLine)
			for(AObject ao: field.getObjectsAtPoint(s.cur_pos))
				ao.preventDraw();
	}
	
	protected void clearLine()
	{
		for(AObject s: selectLine)
			for(AObject ao: field.getObjectsAtPoint(s.cur_pos))
				ao.allowDraw();
		selectLine.clear();
	}
	
	@Override public int getActionPeriod()
	{
		return 0;
	}
}
