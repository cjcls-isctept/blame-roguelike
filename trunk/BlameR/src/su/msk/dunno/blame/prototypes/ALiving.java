package su.msk.dunno.blame.prototypes;

import java.util.LinkedList;
import java.util.ListIterator;

import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.Livings;
import su.msk.dunno.blame.objects.items.ColdPart;
import su.msk.dunno.blame.objects.items.FirePart;
import su.msk.dunno.blame.objects.items.LightningPart;
import su.msk.dunno.blame.objects.items.PoisonPart;
import su.msk.dunno.blame.objects.items.SocketExtender;
import su.msk.dunno.blame.screens.Inventory;
import su.msk.dunno.blame.screens.Weapon;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.Point;


public abstract class ALiving extends AObject 
{
	// stats
	protected int health;
	protected int speed;	
	
	// effects
	public boolean isDead;
	
	public Inventory inventory;
	public Weapon weapon;
	
	private Point old_pos = cur_pos;	// previous position: set private to prevent some possibilities "to hack" the system :)
	private int lastActionTime;
	private int actionPeriod;
	private ADecision decision;
	
	protected Field field;	

	public ALiving(int i, int j, Field field) 
	{
		super(i, j);
		inventory = new Inventory(this, field);
		weapon = new Weapon(this, inventory);
		this.field = field;
		field.addObject(this);
	}
	
	public ALiving(Point p, Field field) 
	{
		this(p.x, p.y, field);
	}

	public abstract ADecision livingAI();
	
	public void updateOldPos()
	{//	trying to predict some possible bugs... :3
		if(!field.getObjectsAtPoint(cur_pos).contains(this))
		{
			field.findObject(this);
		}
		old_pos = cur_pos;
	}
	
	public void nextStep()
	{
		int cur_time = Livings.instance().getTime();
		weapon.energyRefill();
		if(cur_time - lastActionTime >= actionPeriod)
		{
			/*if(decision == null)
			{*/
				decision = livingAI();
			/*}*/
			if(decision != null)
			{
				actionPeriod = decision.getActionPeriod();	// doAction AFTER getActionPeriod is necessary...
				decision.doAction(cur_time);		// At least until I'd need getActionPeriod to obtain its result depends on doAction results
			}
			//decision = null;
			if(!this.getState().containsKey("CancelMove"))lastActionTime = cur_time;
		}
	}
	
	public boolean isEnemyAtDir(int dir)
	{
		for(AObject ao: getObjectsAtDir(dir))
		{
			if(isEnemy(ao))return true;
		}
		return false;
	}
	
	public LinkedList<AObject> getObjectsAtDir(int dir)
	{
		switch(dir)
		{
		case Move.UP:
			return field.getObjectsAtPoint(new Point(cur_pos.x, cur_pos.y+1));
		case Move.LEFT: 
			return field.getObjectsAtPoint(new Point(cur_pos.x-1, cur_pos.y));
		case Move.DOWN: 
			return field.getObjectsAtPoint(new Point(cur_pos.x, cur_pos.y-1));
		case Move.RIGHT: 
			return field.getObjectsAtPoint(new Point(cur_pos.x+1, cur_pos.y));
		case Move.UPRIGHT: 
			return field.getObjectsAtPoint(new Point(cur_pos.x+1, cur_pos.y+1));
		case Move.UPLEFT: 
			return field.getObjectsAtPoint(new Point(cur_pos.x-1, cur_pos.y+1));
		case Move.DOWNLEFT: 
			return field.getObjectsAtPoint(new Point(cur_pos.x-1, cur_pos.y-1));
		case Move.DOWNRIGHT: 
			return field.getObjectsAtPoint(new Point(cur_pos.x+1, cur_pos.y-1));
		case Move.STAY:	//will not return this object, only other objects on the point
			LinkedList<AObject> atPoint = new LinkedList<AObject>();
			for(AObject ao: field.getObjectsAtPoint(new Point(cur_pos.x, cur_pos.y)))
			{
				if(!ao.equals(this))atPoint.add(ao);
			}
			return atPoint;
		}
		return null;
	}
	
	public boolean getPassabilityAtDir(int dir)
	{
		for(AObject ao: this.getObjectsAtDir(dir))
		{
			if(!ao.getPassability())return false;
		}
		return true;
	}
	
	public LinkedList<AObject> getMyNearestNeighbours() // dov = depth of vision
	{
		return field.getNeighbours(this, 1);
	}
	
	public LinkedList<AObject> getMyNeighbours() // dov = depth of vision
	{
		return field.getNeighbours(this, getDov()+1);	// +1 because of the rlforj fov algorithm
	}
	
	public LinkedList<AObject> getMyEnemies()
	{
		LinkedList<AObject> enemies = new LinkedList<AObject>();
		for(AObject o: getMyNeighbours())
		{
			if(isEnemy(o))enemies.add(o);
		}
		return enemies;
	}

	public void checkStatus(ListIterator<ALiving> li) 
	{
		if(health < 0)isDead = true;
		if(isDead)
		{
			if(isNearPlayer())Messages.instance().addMessage(getName()+" is dead");
			li.remove();
			field.removeObject(this);
			int rand = (int)(Math.random()*5);
			switch(rand)
			{
			case 0: field.addObject(new ColdPart(cur_pos)); return;
			case 1: field.addObject(new FirePart(cur_pos)); return;
			case 2: field.addObject(new LightningPart(cur_pos)); return;
			case 3: field.addObject(new PoisonPart(cur_pos)); return;
			case 4: field.addObject(new SocketExtender(cur_pos)); return;			
			}
		}
	}
	
	@Override public abstract boolean isEnemy(AObject ao);
	
	public abstract boolean isPlayer();	
	
	@Override public boolean getPassability() // all livings are impossible to pass through
	{
		return false;
	}
	
	@Override public boolean getTransparency() // and to look through
	{
		return true;
	}

	public Point getOld_pos() 
	{
		return old_pos;
	}

	public boolean isNearPlayer()
	{
		if((Blame.playCibo?"Cibo":"Killy").equals(getName()))return true;
		for(AObject ao: getMyNeighbours())
		{
			if((Blame.playCibo?"Cibo":"Killy").equals(ao.getName()))return true;
		}
		return false;
	}
	
	@Override public abstract  Color getColor();

	public int getLastActionTime() {
		return lastActionTime;
	}

	public int getActionPeriod() {
		return actionPeriod;
	}
	
	public void setDecision(ADecision d)
	{
		int cur_time = Livings.instance().getTime();
		actionPeriod = d.getActionPeriod();
		d.doAction(cur_time);
		if(!this.getState().containsKey("CancelMove"))lastActionTime = cur_time;
	}
	
	public int getHealth() 
	{
		return health;
	}	
	
	public int getStat(String s)
	{
		if("Health".equals(s))return health;
		if("Speed".equals(s))return speed;
		return 0;
	}
}
