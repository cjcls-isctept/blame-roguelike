package su.msk.dunno.blame.objects.livings;

import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.decisions.Close;
import su.msk.dunno.blame.decisions.MeleeAttack;
import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.decisions.Open;
import su.msk.dunno.blame.decisions.Shoot;
import su.msk.dunno.blame.decisions.Take;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Messages;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.main.support.listeners.EventManager;
import su.msk.dunno.blame.main.support.listeners.KeyListener;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.Livings;
import su.msk.dunno.blame.objects.items.PlayerCorpse;
import su.msk.dunno.blame.objects.symbols.MainSelector;
import su.msk.dunno.blame.objects.symbols.MinorSelector;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreenInterface;
import su.msk.dunno.blame.screens.Inventory;


public class Killy extends ALiving implements IScreenInterface
{
	protected EventManager playerEvents;
	protected boolean isNextStep;
	
	//0 - up/2; 1 - left/4; 2 - down/2; 3 - right/6; 4 - 9; 5 - 7; 6 - 1; 7 - 3; 8 - 5
	protected boolean[] keys = new boolean[9];

	protected boolean wantOpen;
	protected boolean wantClose;
	protected boolean wantTake;
	protected boolean wantShoot;
	
	protected boolean isSelectTarget;
	protected Point selectPoint;
	protected LinkedList<MinorSelector> selectLine = new LinkedList<MinorSelector>();
	
	boolean isCancelMove;
	
	public Killy(Point p, Field field) 
	{
		this(p.x, p.y, field);
	}
	
	public Killy(int i, int j, Field field) 
	{
		super(i, j, field);
		initEvents();
		dov = 5;
		health = 100;
		speed = 4;
	}

	@Override public ADecision livingAI() 
	{
		
		if(keys[0])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y+1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.UP))return new MeleeAttack(this, Move.UP);
				else/* if(!isMoving)*/
				{
					return new Move(this, Move.UP, field);
				}
			}
		}
		else if(keys[1])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.LEFT))return new MeleeAttack(this, Move.LEFT);
				else/* if(!isMoving)*/
				{
					return new Move(this, Move.LEFT, field);
				}
			}
		}
		else if(keys[2])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y-1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.DOWN))return new MeleeAttack(this, Move.DOWN);
				else/* if(!isMoving)*/
				{
					return new Move(this, Move.DOWN, field);
				}
			}
		}
		else if(keys[3])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.RIGHT))return new MeleeAttack(this, Move.RIGHT);
				else/* if(!isMoving)*/
				{
					return new Move(this, Move.RIGHT, field);
				}
			}
		}
		else if(keys[4])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y+1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.UPRIGHT))return new MeleeAttack(this, Move.UPRIGHT);
				else/* if(!isMoving)*/
				{
					return new Move(this, Move.UPRIGHT, field);
				}
			}
		}
		else if(keys[5])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y+1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.UPLEFT))return new MeleeAttack(this, Move.UPLEFT);
				else/* if(!isMoving)*/
				{
					return new Move(this, Move.UPLEFT, field);
				}
			}
		}
		else if(keys[6])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y-1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.DOWNLEFT))return new MeleeAttack(this, Move.DOWNLEFT);
				else/* if(!isMoving)*/
				{
					return new Move(this, Move.DOWNLEFT, field);
				}
			}
		}
		else if(keys[7])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y-1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.DOWNRIGHT))return new MeleeAttack(this, Move.DOWNRIGHT);
				else/* if(!isMoving)*/
				{
					return new Move(this, Move.DOWNRIGHT, field);
				}
			}
		}
		else if(keys[8])return new Move(this, Move.STAY, field);
		else if(wantOpen)return new Open(this);
		else if(wantClose)return new Close(this);
		else if(wantTake)return new Take(this, field);
		else if(wantShoot) 
		{
			if(selectLine.size() > 0)
			{
				clearLine();
				Shoot sh = new Shoot(this, selectPoint, field);
				selectPoint = null;
				return sh;
			}
			else 
			{
				selectPoint = cur_pos;
				LinkedList<AObject> neighbours = getMyNeighbours();
				for(AObject ao: neighbours)
				{
					if(isEnemy(ao))
					{
						selectPoint = ao.cur_pos;
						break;
					}
				}
				buildLine();
				isSelectTarget = true;
			}
		}
		return null;
	}

	@Override public char getSymbol() 
	{
		return '@';
	}

	@Override public String getName() 
	{
		return "Killy";
	}
	
	@Override public Color getColor()
	{
		/*if(isDead) return Color.WHITE;
		else */return Color.RED;
	}
	
	public void reset_keys()
	{
		for(int i = 0; i < keys.length; i++)keys[i] = false;
		wantOpen = false;
		wantClose = false;
		wantTake = false;
		wantShoot = false;
		isSelectTarget = false;
	}
	
	@Override public void changeState(HashMap<String, String> args)
	{
		if(args.containsKey("Damage"))
		{
			health -= Integer.valueOf(args.get("Damage"));
		}
		if(args.containsKey("HealthPlus"))
		{
			health += Integer.valueOf(args.get("HealthPlus"));
		}
		if(args.containsKey("MoveFail"))
		{
			isCancelMove = true;
		}
	}
	
	@Override public HashMap<String, String> getState() 
	{
		HashMap<String, String> state = new HashMap<String, String>();
		if(isSelectTarget || isCancelMove || inventory.isOpen())
		{
			state.put("CancelMove", "");
			isCancelMove = false;
		}
		return state;
	}
	
	protected void buildLine()
	{
		clearLine();
		LinkedList<Point> line = field.getLine(cur_pos, selectPoint);
		int i = 0;
		for(Point p: line)
		{
			i++;
			if(line.size() > 1 && i == 1)continue;	//	skip the first element if amount of elements is more than 1
			if(field.onArea(p) && field.isMapVisible(p, cur_pos, dov))
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
		for(MinorSelector s: selectLine)field.addObject(s);
	}
	
	public void clearLine()
	{
		for(MinorSelector s: selectLine)
		{
			field.removeObject(s);
		}
		selectLine.clear();
	}
	
	@Override public boolean isLightSource()
	{
		return true;
	}

	@Override public boolean isEnemy(AObject ao) 
	{
		return "Silicon Creature".equals(ao.getName());
	}
	
	public void checkPlayerStatus()
	{
		if(health < 0)isDead = true;
		if(isDead)
		{
			field.removeObject(this);
			field.addObject(new PlayerCorpse(cur_pos));
		}
	}
	
	public void initEvents()
	{
		playerEvents = new EventManager();
		playerEvents.addListener(Keyboard.KEY_UP, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[0] = true;
        		isNextStep = true;
         	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD8, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[0] = true;
        		isNextStep = true;
         	}
        });
		playerEvents.addListener(Keyboard.KEY_LEFT, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[1] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD4, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[1] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_DOWN, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[2] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD2, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[2] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_RIGHT, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[3] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD6, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[3] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD9, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[4] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD7, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[5] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD1, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[6] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD3, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[7] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD5, new KeyListener(100)
        {
        	public void onKeyDown()
        	{
        		keys[8] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_O, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		wantOpen = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_C, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		wantClose = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_F, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		wantShoot = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_ESCAPE, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(selectLine.size() > 0)
        		{
        			clearLine();
            		selectPoint = null;
        		}
        		//else System.exit(0);
        	}
        });
		playerEvents.addListener(Keyboard.KEY_F12, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		System.exit(0);
        	}
        });
		playerEvents.addListener(Keyboard.KEY_COMMA, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		wantTake = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_I, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		inventory.openInventory(Inventory.TO_CHECK);
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_D, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		inventory.openInventory(Inventory.TO_DROP);
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_W, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		weapon.openWeaponView();
        		isNextStep = true;
        	}
        });
	}
	
	public void process()
	{
		if(inventory.isOpen())inventory.process();
		else if(weapon.isOpen())weapon.process();
		else
		{
			playerEvents.checkEvents();
			if(isNextStep)
			{
				Livings.instance().nextStep();
				isNextStep = false;
				reset_keys();	
			}
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
			GL11.glLoadIdentity();
			
			field.draw(cur_pos);		
			Messages.instance().showMessages();
			// statistics
			int k = Blame.height-20;
			MyFont.instance().drawString(getName(),                             
					Blame.width-190, k, 0.2f, Color.WHITE); k-= 15;
			MyFont.instance().drawString("HP: "+health,                         
					Blame.width-190, k, 0.2f, Color.WHITE); k-= 15;
			MyFont.instance().drawString("Time: "+Livings.instance().getTime(), 
					Blame.width-190, k, 0.2f, Color.WHITE); k-= 15;
			MyFont.instance().drawString("FPS: "+Blame.fps,                     
					Blame.width-190, k, 0.2f, Color.WHITE); k-= 15;
			MyFont.instance().drawString("Anima: "+field.animations.size(),     
					Blame.width-190, k, 0.2f, Color.WHITE); k-= 15;
			MyFont.instance().drawString("PlayerMoves: "+field.playerMoves,     
					Blame.width-190, k, 0.2f, Color.WHITE);
			
			Display.sync(Blame.framerate);
			Display.update();
		}
	}

	@Override public boolean isPlayer() 
	{
		return true;
	}
}
