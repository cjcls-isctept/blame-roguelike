package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.Livings;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class SelectEmitter extends SelectTarget 
{
	private boolean isNextStep;
	private int weaponPower = 0;
	private int powerIncreaseSpeed = 20;
	private boolean isTargetSelected;

	public SelectEmitter(ALiving al, Field field) 
	{
		super(al, field, new EmitterShoot(al, field));
	}
	
	public void process() 
	{
		while(isSelectTarget && !al.isDead)
		{
			selectEvents.checkEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
			GL11.glLoadIdentity();
			if(isNextStep)
			{
				Livings.instance().nextStep();
				weaponPower += powerIncreaseSpeed;
				isNextStep = false;
			}
			field.draw(al.cur_pos);
			field.drawLine(selectLine);
			Blame.getCurrentPlayer().drawStats();
			Messages.instance().showMessages();
			drawPercentage();
			Display.sync(Blame.framerate);
			Display.update();
			if(isTargetSelected)
			{
				if(weaponPower < 100)
				{
					Shoot sh = new Shoot(al, field);
					sh.setSelectPoint(selectPoint);
					al.setDecision(sh);
				}
				else 
				{
					actionAfter.setSelectPoint(selectPoint);
					al.setDecision(actionAfter);
				}
			}
		}
		clearLine();
	}
	
	public void drawPercentage()
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		MyFont.instance().drawDisplayList(MyFont.CIRCLE, Blame.width/2, 150, Color.WHITE);
		if(weaponPower > 25)MyFont.instance().drawDisplayList(MyFont.RECTANGLE1, Blame.width/2-5, 165, Color.WHITE);
		if(weaponPower > 50)MyFont.instance().drawDisplayList(MyFont.RECTANGLE2, Blame.width/2+15, 145, Color.WHITE);
		if(weaponPower > 75)MyFont.instance().drawDisplayList(MyFont.RECTANGLE1, Blame.width/2-5, 115, Color.WHITE);
		if(weaponPower >= 100)MyFont.instance().drawDisplayList(MyFont.RECTANGLE2, Blame.width/2-35, 145, Color.WHITE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void initEvents()
	{
		selectEvents.addListener(Keyboard.KEY_UP, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y+1);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_NUMPAD9, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y+1);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_NUMPAD8, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y+1);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_NUMPAD7, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y+1);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_RIGHT, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_NUMPAD6, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_NUMPAD5, new KeyListener(100)
		{
			public void onKeyDown()
			{
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_LEFT, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_NUMPAD4, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_DOWN, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y-1);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_NUMPAD3, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y-1);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_NUMPAD2, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y-1);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_NUMPAD1, new KeyListener(100)
		{
			public void onKeyDown()
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y-1);
				buildLine();
				isNextStep = true;
			}
		});
		
		selectEvents.addListener(Keyboard.KEY_F, new KeyListener(0)
		{
			public void onKeyDown()
			{
				if(selectLine.size() > 0)
				{
					clearLine();
					isTargetSelected = true;
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
	
	@Override public int getActionPeriod()
	{
		return al.getStat("Speed");
	}
}
