package su.msk.dunno.blame.prototypes;

import java.util.LinkedList;
import java.util.ListIterator;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.items.Corpse;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Point;


public abstract class ALiving extends AObject 
{
	protected int health;
	public boolean isDead;
	
	private Point old_pos;	// previous position: set private to prevent some possibilities "to hack" system :)
	
	protected Field field;
	
	private int lastAction_time;
	protected int actionPeriod;
	
	private ADecision decision;

	public ALiving(int i, int j, Field field) 
	{
		super(i, j);
		old_pos = cur_pos;
		this.field = field;
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
	
	public void nextStep(int time)
	{
		if(time - getLastActionTime() >= getActionPeriod())
		{
			if(decision == null)
			{
				decision = livingAI();
			}
			if(decision != null)decision.doAction();
			decision = null;
			if(!this.getState().containsKey("CancelMove"))lastAction_time = time;
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

	public void checkStatus(ListIterator<ALiving> li) 
	{
		if(health < 0)isDead = true;
		if(isDead)
		{
			li.remove();
			field.removeObject(this);
			field.addObject(new Corpse(cur_pos));
		}
	}
	
	@Override public abstract boolean isEnemy(AObject ao);
	
	
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
	
	public void removeObject(AObject ao)
	{
		if(cur_pos.equals(ao.cur_pos))field.removeObject(ao);
	}

	public int getHealth() 
	{
		return health;
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
		return lastAction_time;
	}

	public int getActionPeriod() {
		return actionPeriod;
	}
}
