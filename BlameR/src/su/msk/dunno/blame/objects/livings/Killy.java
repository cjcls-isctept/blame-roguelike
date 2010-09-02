package su.msk.dunno.blame.objects.livings;

import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.decisions.CloseDoor;
import su.msk.dunno.blame.decisions.EnterStation;
import su.msk.dunno.blame.decisions.GiveOrder;
import su.msk.dunno.blame.decisions.MeleeAttack;
import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.decisions.OpenDoor;
import su.msk.dunno.blame.decisions.OpenHelpScreen;
import su.msk.dunno.blame.decisions.OpenInventory;
import su.msk.dunno.blame.decisions.OpenWeapon;
import su.msk.dunno.blame.decisions.SelectEmitter;
import su.msk.dunno.blame.decisions.SelectLook;
import su.msk.dunno.blame.decisions.SelectTarget;
import su.msk.dunno.blame.decisions.Shoot;
import su.msk.dunno.blame.decisions.Take;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.map.path.PathFinder;
import su.msk.dunno.blame.map.path.astar.AStarPathFinder;
import su.msk.dunno.blame.objects.Livings;
import su.msk.dunno.blame.objects.items.ImpEmitter;
import su.msk.dunno.blame.objects.items.PlayerCorpse;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.screens.InventoryScreen;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;
import su.msk.dunno.blame.support.TrueTypeFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;


public class Killy extends ALiving implements IScreen
{
	protected EventManager playerEvents = new EventManager();
	protected boolean isNextStep;
	
	private boolean isCancelMove;
	
	/*private float infection_level;
	private float infection_expansion_rate_move = 1/3.0f;
	private float infection_expansion_rate_stay = 1/6.0f;*/
	
	protected PathFinder find;
	protected boolean isFollowPlayer = false;
	protected boolean isAttackEnemies = false;
	
	protected ADecision keyboardDecision;
	protected Killy k = this;
	
	public Killy(Point p, Field field) 
	{
		super(p.x, p.y, field);
		initEvents();
		find = new AStarPathFinder(field);
		inventory.addItem(new ImpEmitter(new Point()));
	}
	
	@Override protected void initStats() 
	{
		setStat("Dov", 5);
		setStat("Health", 100);
		setStat("Speed", 4);
	}

	@Override public ADecision livingAI() 
	{
		if(isAttackEnemies)
		{
			for(AObject ao: getMyNeighbours())
			{
				if(isEnemy(ao) || ao.isEnemy(this))return new Shoot(this, field, ao.cur_pos);
			}
		}
		if(isFollowPlayer)
		{
			Point near = field.getNearestFree(Blame.getCurrentPlayer().cur_pos, 1);
			if(near != null)find.findPath(cur_pos, near);
			// remove first point
			if(!find.path.isEmpty() && cur_pos.equals(find.path.getFirst()))find.path.removeFirst();
			if(find.path.isEmpty() || find.path.size() == 1)	
			{// couldn't create a path - go to random direction or there is an obstacle on our way
				return new Move(this, (int)(Math.random()*9), field);
			}
			else 			// if everything ok - go to the next point in our path 
			{
				if("Close door".equals(field.getObjectsAtPoint(find.path.getFirst()).getFirst().getName()))
				{	//found a door near us
					return new OpenDoor(this);	// open it
				}
				else if(!field.getPassability(find.path.getFirst()))
				{
					return new Move(this, (int)(Math.random()*9), field);
				}
				else return new Move(this, field.getDirection(cur_pos, find.path.getFirst()), field);
			}
		}
		return keyboardDecision;		
	}
	
	@Override public int getSymbol()
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
	
	/*public String getInfectionLevel()
	{
		if(infection_level < 35)return "Low";
		else if(infection_level >= 35 && infection_level < 75)return "Medium";
		else if(infection_level >= 75 && infection_level < 100)return "High!";
		else if(infection_level >= 100)return "Mortal";
		return "";
	}*/
	
	@Override public void changeState(ALiving changer, StateMap args)
	{
		/*if(args.containsKey("InfectionHeal"))
		{
			infection_level -= args.getInt("InfectionHeal");
			if(infection_level < 0)infection_level = 0;
		}*/
		if(args.containsKey("MoveFail"))
		{
			isCancelMove = true;
		}
		if(args.containsKey("FollowMe"))
		{
			if(changer.getState().containsKey("Player"))
			{
				isFollowPlayer = true;
				if(isNearPlayer())Messages.instance().addMessage(getName()+" будет следовать за "+changer.getName());
			}
		}
		if(args.containsKey("Wait"))
		{
			if(changer.getState().containsKey("Player"))
			{
				isFollowPlayer = false;
				if(isNearPlayer())Messages.instance().addMessage(getName()+" останавливается");
			}
		}
		if(args.containsKey("ShootEnemies"))
		{
			if(changer.getState().containsKey("Player"))
			{
				isAttackEnemies = true;
				if(isNearPlayer())Messages.instance().addMessage(getName()+" будет атаковать врагов в поле зрения ");
			}
		}
		if(args.containsKey("StopShoot"))
		{
			if(changer.getState().containsKey("Player"))
			{
				isAttackEnemies = false;
				if(isNearPlayer())Messages.instance().addMessage(getName()+" прекращает атаковать цели");
			}
		}
	}
	
	@Override public StateMap getState() 
	{
		StateMap state = new StateMap("Player");
		if(isCancelMove || inventory.isOpen())
		{
			state.put("CancelMove");
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
		return ao.getState().containsKey("Corrupted");
	}
	
	@Override public boolean checkStatus(ListIterator<ALiving> li) 
	{
		if(getStat("Health") < 0)
		{
			isDead = true;
			if(isNearPlayer())Messages.instance().addPropMessage("living.dies", getName());
		}
		/*if(infection_level >= 100)
		{
			isDead = true;
			if(isNearPlayer())Messages.instance().addPropMessage("player.infectiondeath", getName());
		}*/
		if(isDead)
		{
			field.removeObject(this);
			field.addObject(new PlayerCorpse(getName(), cur_pos));
		}
		return isDead;
	}	
	
	/*public void increaseInfectionLevel()
	{
		if(cur_pos.equals(old_pos))infection_level += infection_expansion_rate_stay;
		else infection_level += infection_expansion_rate_move;
	}*/
	
	public void cancelMove()
	{
		isCancelMove = true;
	}
	
	public void process()
	{
		playerEvents.checkEvents();
		if(isNextStep)
		{
			Livings.instance().nextStep();
			isNextStep = false;
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
		GL11.glLoadIdentity();
		
		field.draw(cur_pos/*Blame.scale*/);
		drawStats();
		Messages.instance().showMessages();
		Display.sync(Blame.framerate);
		Display.update();
	}
	
	public void drawStats()
	{
		// statistics
		int k = Blame.height-25;
		TrueTypeFont.instance().drawString(getName(), Blame.width-200, k, Color.WHITE); k-= 20;
		TrueTypeFont.instance().drawString(Messages.instance().getPropMessage("interface.hp", getStat("Health")+""), Blame.width-200, k, Color.WHITE); k-= 20;
		TrueTypeFont.instance().drawString(Messages.instance().getPropMessage("interface.energy", weapon.showEnergy()+""), Blame.width-200, k, Color.WHITE); k-= 20;
		/*if(infection_level < 35)
		{
			TrueTypeFont.instance().drawString(Messages.instance().getPropMessage("interface.infection.low"), Blame.width-200, k, Color.GREEN); k-= 20;
		}
		else if(infection_level >= 35 && infection_level < 75)
		{
			TrueTypeFont.instance().drawString(Messages.instance().getPropMessage("interface.infection.medium"), Blame.width-200, k, Color.YELLOW); k-= 20;
		}
		else if(infection_level >= 75 && infection_level < 100)
		{
			TrueTypeFont.instance().drawString(Messages.instance().getPropMessage("interface.infection.high"), Blame.width-200, k, Color.RED); k-= 20;
		}
		else if(infection_level >= 100)
		{
			TrueTypeFont.instance().drawString(Messages.instance().getPropMessage("interface.infection.mortal"), Blame.width-200, k, Color.WHITE); k-= 20;
		}*/
		TrueTypeFont.instance().drawString("Time: "+Livings.instance().getTime(), Blame.width-200, k, Color.WHITE); k-= 20;
		TrueTypeFont.instance().drawString("FPS: "+Blame.fps, Blame.width-200, k, Color.WHITE); k-= 20;
		TrueTypeFont.instance().drawString("Anima: "+field.animations.size(), Blame.width-200, k, Color.WHITE); k-= 20;
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
		return this.equals(Blame.getCurrentPlayer());
	}
	
	public void initEvents()
	{
		playerEvents.addListener(Keyboard.KEY_UP, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.UP))keyboardDecision = new MeleeAttack(k, Move.UP);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.UP, field);
    			}
        		isNextStep = true;
         	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD8, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.UP))keyboardDecision = new MeleeAttack(k, Move.UP);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.UP, field);
    			}
        		isNextStep = true;
         	}
        });
		playerEvents.addListener(Keyboard.KEY_LEFT, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.LEFT))keyboardDecision = new MeleeAttack(k, Move.LEFT);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.LEFT, field);
    			}
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD4, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.LEFT))keyboardDecision = new MeleeAttack(k, Move.LEFT);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.LEFT, field);
    			}
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_DOWN, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.DOWN))keyboardDecision = new MeleeAttack(k, Move.DOWN);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.DOWN, field);
    			}
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD2, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.DOWN))keyboardDecision = new MeleeAttack(k, Move.DOWN);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.DOWN, field);
    			}
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_RIGHT, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.RIGHT))keyboardDecision = new MeleeAttack(k, Move.RIGHT);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.RIGHT, field);
    			}
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD6, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.RIGHT))keyboardDecision = new MeleeAttack(k, Move.RIGHT);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.RIGHT, field);
    			}
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD9, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.UPRIGHT))keyboardDecision = new MeleeAttack(k, Move.UPRIGHT);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.UPRIGHT, field);
    			}
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD7, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.UPLEFT))keyboardDecision = new MeleeAttack(k, Move.UPLEFT);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.UPLEFT, field);
    			}
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD1, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
    			if(isEnemyAtDir(Move.DOWNLEFT))keyboardDecision = new MeleeAttack(k, Move.DOWNLEFT);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.DOWNLEFT, field);
    			}
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD3, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		if(isEnemyAtDir(Move.DOWNRIGHT))keyboardDecision = new MeleeAttack(k, Move.DOWNRIGHT);
    			else/* if(!isMoving)*/
    			{
    				keyboardDecision = new Move(k, Move.DOWNRIGHT, field);
    			}
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_NUMPAD5, new KeyListener(500, 100)
        {
        	public void onKeyDown()
        	{
        		keyboardDecision = new Move(k, Move.STAY, field);
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_O, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		keyboardDecision = new OpenDoor(k);
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_C, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		keyboardDecision = new CloseDoor(k);
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_F, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		if(weapon.showEffects().containsKey("Level2"))keyboardDecision = new SelectEmitter(k, field);
    			else keyboardDecision = new SelectTarget(k, field, new Shoot(k, field));
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_L, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		keyboardDecision = new SelectLook(k, field, null);
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
        		keyboardDecision = new Take(k, field);
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_I, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		inventory.setMode(InventoryScreen.TO_CHECK);
        		keyboardDecision = new OpenInventory(k);
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_D, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		inventory.setMode(InventoryScreen.TO_DROP);
        		keyboardDecision = new OpenInventory(k);
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_W, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		keyboardDecision = new OpenWeapon(k);
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_F1, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		keyboardDecision = new OpenHelpScreen(k);
        		isNextStep = true;
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
        		keyboardDecision = new EnterStation(k, field);
        		isNextStep = true;
        	}
        });
		playerEvents.addListener(Keyboard.KEY_T, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		keyboardDecision = new GiveOrder(k, field);
        		isNextStep = true;
        	}
        });
	}
}
