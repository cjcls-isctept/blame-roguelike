package su.msk.dunno.blame.objects.livings;

import java.util.HashMap;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.decisions.Close;
import su.msk.dunno.blame.decisions.EnterStation;
import su.msk.dunno.blame.decisions.MeleeAttack;
import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.decisions.Open;
import su.msk.dunno.blame.decisions.SelectTarget;
import su.msk.dunno.blame.decisions.Shoot;
import su.msk.dunno.blame.decisions.Take;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.map.path.PathFinder;
import su.msk.dunno.blame.map.path.astar.AStarPathFinder;
import su.msk.dunno.blame.objects.Livings;
import su.msk.dunno.blame.objects.items.PlayerCorpse;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.screens.HelpScreen;
import su.msk.dunno.blame.screens.Inventory;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;


public class Killy extends ALiving implements IScreen
{
	protected EventManager playerEvents;
	protected boolean isNextStep;
	
	//0 - up/2; 1 - left/4; 2 - down/2; 3 - right/6; 4 - 9; 5 - 7; 6 - 1; 7 - 3; 8 - 5
	protected boolean[] keys = new boolean[9];

	protected boolean wantOpen;
	protected boolean wantClose;
	protected boolean wantTake;
	protected boolean wantShoot;
	protected boolean wantEnterStation;
	
	private boolean isCancelMove;
	
	private float infection_level;
	private float infection_expansion_rate_move = 1/3.0f;
	private float infection_expansion_rate_stay = 1/6.0f;
	
	protected PathFinder find;
	protected boolean isFollowPlayer = false;
	protected boolean isAttackEnemies = false;
	
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
		find = new AStarPathFinder(field);
	}

	@Override public ADecision livingAI() 
	{
		if(isAttackEnemies)
		{
			for(AObject ao: getMyNeighbours())
			{
				if(isEnemy(ao))return new Shoot(this, field, ao.cur_pos);
			}
		}
		if(isFollowPlayer)
		{
			Point near = field.getNearestFree(Blame.getCurrentPlayer().cur_pos, 1);
			if(near != null)find.findPath(cur_pos, near);
			// remove first point
			if(!find.path.isEmpty() && cur_pos.equals(find.path.getFirst()))find.path.removeFirst();
			if(find.path.isEmpty())	
			{// couldn't create a path - go to random direction or there is an obstacle on our way
				return new Move(this, (int)(Math.random()*9), field);
			}
			// if everything ok - go to the next point in our path
			else 
			{
				if("Close door".equals(field.getObjectsAtPoint(find.path.getFirst()).getFirst().getName()))
				{	//found a door near us
					return new Open(this);	// open it
				}
				else if(!field.getPassability(find.path.getFirst()))
				{
					return new Move(this, (int)(Math.random()*9), field);
				}
				else return new Move(this, field.getDirection(cur_pos, find.path.getFirst()), field);
			}
		}
		if(keys[0])
		{
			if(isEnemyAtDir(Move.UP))return new MeleeAttack(this, Move.UP);
			else/* if(!isMoving)*/
			{
				return new Move(this, Move.UP, field);
			}
		}
		else if(keys[1])
		{
			if(isEnemyAtDir(Move.LEFT))return new MeleeAttack(this, Move.LEFT);
			else/* if(!isMoving)*/
			{
				return new Move(this, Move.LEFT, field);
			}
		}
		else if(keys[2])
		{
			if(isEnemyAtDir(Move.DOWN))return new MeleeAttack(this, Move.DOWN);
			else/* if(!isMoving)*/
			{
				return new Move(this, Move.DOWN, field);
			}
		}
		else if(keys[3])
		{
			if(isEnemyAtDir(Move.RIGHT))return new MeleeAttack(this, Move.RIGHT);
			else/* if(!isMoving)*/
			{
				return new Move(this, Move.RIGHT, field);
			}
		}
		else if(keys[4])
		{
			if(isEnemyAtDir(Move.UPRIGHT))return new MeleeAttack(this, Move.UPRIGHT);
			else/* if(!isMoving)*/
			{
				return new Move(this, Move.UPRIGHT, field);
			}
		}
		else if(keys[5])
		{
			if(isEnemyAtDir(Move.UPLEFT))return new MeleeAttack(this, Move.UPLEFT);
			else/* if(!isMoving)*/
			{
				return new Move(this, Move.UPLEFT, field);
			}
			
		}
		else if(keys[6])
		{
			if(isEnemyAtDir(Move.DOWNLEFT))return new MeleeAttack(this, Move.DOWNLEFT);
			else/* if(!isMoving)*/
			{
				return new Move(this, Move.DOWNLEFT, field);
			}
		}
		else if(keys[7])
		{
			if(isEnemyAtDir(Move.DOWNRIGHT))return new MeleeAttack(this, Move.DOWNRIGHT);
			else/* if(!isMoving)*/
			{
				return new Move(this, Move.DOWNRIGHT, field);
			}
		}
		else if(keys[8])return new Move(this, Move.STAY, field);
		else if(wantOpen)return new Open(this);
		else if(wantClose)return new Close(this);
		else if(wantTake)return new Take(this, field);
		else if(wantShoot) 
		{
			return new SelectTarget(this, field, new Shoot(this, field));
		}
		else if(wantEnterStation)
		{
			return new EnterStation(this, field);
		}
		return null;
	}
	
	@Override public int getCode()
	{
		return MyFont.PLAYER;
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
		wantEnterStation = false;
	}
	
	public String getInfectionLevel()
	{
		if(infection_level < 35)return "Low";
		else if(infection_level >= 35 && infection_level < 75)return "Medium";
		else if(infection_level >= 75 && infection_level < 100)return "High!";
		else if(infection_level >= 100)return "Mortal";
		return "";
	}
	
	@Override public void changeState(ALiving changer, HashMap<String, String> args)
	{
		if(args.containsKey("Damage"))
		{
			health -= Integer.valueOf(args.get("Damage"));
			if(isNearPlayer())Messages.instance().addMessage(getName()+" receives "+args.get("Damage")+" damage");
		}
		if(args.containsKey("HealthPlus"))
		{
			health += Integer.valueOf(args.get("HealthPlus"));
		}
		if(args.containsKey("InfectionHeal"))
		{
			infection_level -= Integer.valueOf(args.get("InfectionHeal"));
			if(infection_level < 0)infection_level = 0;
		}
		if(args.containsKey("MoveFail"))
		{
			isCancelMove = true;
		}
	}
	
	@Override public HashMap<String, String> getState() 
	{
		HashMap<String, String> state = new HashMap<String, String>();
		if(isCancelMove || inventory.isOpen())
		{
			state.put("CancelMove", "");
			isCancelMove = false;
		}
		return state;
	}
	
	@Override public boolean isLightSource()
	{
		return true;
	}

	@Override public boolean isEnemy(AObject ao) 
	{
		return "Silicon Creature".equals(ao.getName());
	}
	
	@Override public boolean checkStatus(ListIterator<ALiving> li) 
	{
		if(health < 0)
		{
			isDead = true;
			if(isNearPlayer())Messages.instance().addMessage(getName()+" dies");
		}
		if(infection_level >= 100)
		{
			isDead = true;
			if(isNearPlayer())Messages.instance().addMessage(getName()+" dies from the infection");
		}
		if(isDead)
		{
			field.removeObject(this);
			field.addObject(new PlayerCorpse(getName(), cur_pos));
		}
		return isDead;
	}	
	
	public void increaseInfectionLevel()
	{
		if(cur_pos.equals(old_pos))infection_level += infection_expansion_rate_stay;
		else infection_level += infection_expansion_rate_move;
	}
	
	public void process()
	{
		if(inventory.isOpen())inventory.process();
		else if(weapon.isOpen())weapon.process();
		else if(HelpScreen.instance().isOpen())HelpScreen.instance().process();
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
			drawStats();
			Display.sync(Blame.framerate);
			Display.update();
		}
	}
	
	public void drawStats()
	{
		Messages.instance().showMessages();
		// statistics
		int k = Blame.height-25;
		TrueTypeFont.instance().drawString(getName(), Blame.width-200, k, Color.WHITE); k-= 20;
		TrueTypeFont.instance().drawString("HP: "+health, Blame.width-200, k, Color.WHITE); k-= 20;
		TrueTypeFont.instance().drawString("Damage: "+weapon.showDamage(), Blame.width-200, k, Color.WHITE); k-= 20;
		TrueTypeFont.instance().drawString("Energy: "+weapon.showEnergy(), Blame.width-200, k, Color.WHITE); k-= 20;
		if(infection_level < 35)
		{
			TrueTypeFont.instance().drawString("Infection: low", Blame.width-200, k, Color.GREEN); k-= 20;
		}
		else if(infection_level >= 35 && infection_level < 75)
		{
			TrueTypeFont.instance().drawString("Infection: medium", Blame.width-200, k, Color.YELLOW); k-= 20;
		}
		else if(infection_level >= 75 && infection_level < 100)
		{
			TrueTypeFont.instance().drawString("Infection: high!", Blame.width-200, k, Color.RED); k-= 20;
		}
		else if(infection_level >= 100)
		{
			TrueTypeFont.instance().drawString("Infection: mortal", Blame.width-200, k, Color.WHITE); k-= 20;
		}
		TrueTypeFont.instance().drawString("Time: "+Livings.instance().getTime(), Blame.width-200, k, Color.WHITE); k-= 20;
		TrueTypeFont.instance().drawString("FPS: "+Blame.fps, Blame.width-200, k, Color.WHITE); k-= 20;
		TrueTypeFont.instance().drawString("Anima: "+field.animations.size(), Blame.width-200, k, Color.WHITE); k-= 20;
		TrueTypeFont.instance().drawString("PlayerMoves: "+field.playerMoves, Blame.width-200, k, Color.WHITE); k-= 20;
		if(isFollowPlayer)
		{
			TrueTypeFont.instance().drawString("Following "+("Killy".equals(getName())?"Cibo":"Killy"), Blame.width-200, k, Color.WHITE); k-= 20;
		}
		if(isAttackEnemies)
		{
			TrueTypeFont.instance().drawString("Attack on sight", Blame.width-200, k, Color.WHITE); k-= 20;
		}
	}

	@Override public boolean isPlayer() 
	{
		return true;
	}
	
	public void initEvents()
	{
		playerEvents = new EventManager();
		playerEvents.addListener(Keyboard.KEY_UP, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[0] = true;
        		isNextStep = true;
         	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD8, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[0] = true;
        		isNextStep = true;
         	}
        });
		playerEvents.addListener(Keyboard.KEY_LEFT, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[1] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD4, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[1] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_DOWN, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[2] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD2, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[2] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_RIGHT, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[3] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD6, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[3] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD9, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[4] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD7, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[5] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD1, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[6] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD3, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keys[7] = true;
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD5, new KeyListener(500, 100)
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
		playerEvents.addListener(Keyboard.KEY_F1, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		HelpScreen.instance().openHelp();
        	}
        });
		playerEvents.addListener(Keyboard.KEY_F5, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		isFollowPlayer = !isFollowPlayer;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_F6, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		isAttackEnemies = !isAttackEnemies;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_RETURN, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		wantEnterStation = true;
        		isNextStep = true;
        	}
        });		
	}
}
