package su.msk.dunno.blame.prototypes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.Livings;
import su.msk.dunno.blame.screens.InventoryScreen;
import su.msk.dunno.blame.screens.WeaponScreen;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;


public abstract class ALiving extends AObject 
{	
	// effects
	public boolean isDead;
	
	protected InventoryScreen inventory;
	protected WeaponScreen weapon;
	
	// previous position: set private to prevent some possibilities "to hack" the system :)
	protected Point old_pos = curPos;	// not anymore :(	
	
	private int lastActionTime;
	private int actionPeriod;
	private ADecision decision;
	
	protected Field field;	

	public ALiving(int i, int j, Field field) 
	{
		super(i, j);
		initStats();
		initItemDrop();
		inventory = new InventoryScreen(this, field);
		weapon = new WeaponScreen(this);
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
		if(!field.getObjectsAtPoint(curPos).contains(this))
		{
			field.findObject(this);	// fail. what a dumbass :)
		}
		old_pos = curPos;
	}
	
	public void nextStep()
	{
		int cur_time = Livings.instance().getTime();
		weapon.energyRefill();
		if(cur_time - lastActionTime >= actionPeriod)
		{
			if(decision == null || decision.wasExecuted)
			{
				/*if(decision == null)
				{*/
					decision = livingAI();
				/*}*/
				if(decision != null && !decision.wasExecuted)
				{
					actionPeriod = decision.getActionPeriod();	// doAction AFTER getActionPeriod is necessary...
					decision.doAction(cur_time);		// At least until I'd need getActionPeriod to obtain its result depends on doAction results
				}
				//decision = null;
			}
			if(!this.getState().containsKey("CancelMove"))lastActionTime = cur_time;
		}
	}
	
	public boolean isEnemyAtDir(int dir)
	{
		for(AObject ao: getObjectsAtDir(dir))
		{
			if(isEnemy(ao) || ao.isEnemy(this))return true;
		}
		return false;
	}
	
	public LinkedList<AObject> getObjectsAtDir(int dir)
	{
		switch(dir)
		{
		case Move.UP:
			return field.getObjectsAtPoint(new Point(curPos.x, curPos.y+1));
		case Move.LEFT: 
			return field.getObjectsAtPoint(new Point(curPos.x-1, curPos.y));
		case Move.DOWN: 
			return field.getObjectsAtPoint(new Point(curPos.x, curPos.y-1));
		case Move.RIGHT: 
			return field.getObjectsAtPoint(new Point(curPos.x+1, curPos.y));
		case Move.UPRIGHT: 
			return field.getObjectsAtPoint(new Point(curPos.x+1, curPos.y+1));
		case Move.UPLEFT: 
			return field.getObjectsAtPoint(new Point(curPos.x-1, curPos.y+1));
		case Move.DOWNLEFT: 
			return field.getObjectsAtPoint(new Point(curPos.x-1, curPos.y-1));
		case Move.DOWNRIGHT: 
			return field.getObjectsAtPoint(new Point(curPos.x+1, curPos.y-1));
		case Move.STAY:	//will not return this object, only other objects on the point
			LinkedList<AObject> atPoint = new LinkedList<AObject>();
			for(AObject ao: field.getObjectsAtPoint(new Point(curPos.x, curPos.y)))
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
			if(isEnemy(o) || o.isEnemy(this))enemies.add(o);
		}
		return enemies;
	}

	public boolean checkStatus(ListIterator<ALiving> li) 
	{
		if(getStat("Health") <= 0)isDead = true;
		if(isDead)
		{
			if(isNearPlayer())Messages.instance().addPropMessage("living.dead", getName());
			li.remove();
			field.removeObject(this);
			dropItem();
		}
		return isDead;
	}
	
	protected ArrayList<AObject> mustBeDropped = new ArrayList<AObject>();
	protected StateMap[] itemProbabilities = null;
	protected abstract void initItemDrop();
	private int OverallfProbabilitity()
	{
		int sum = 0;
		if(itemProbabilities != null)
		{
			for(int i = 0; i < itemProbabilities.length; i++)
			{
				sum += itemProbabilities[i].getInt("Probability");
			}
		}
		return sum;
	}
	protected void dropItem()
	{
		for(AObject item: mustBeDropped)
		{
			item.curPos = curPos;
			field.addObject(item);
		}
		if(itemProbabilities != null)
		{
			int len = itemProbabilities.length;
			int attempt = (int)(Math.random()*100);
			int currentStep = 0;
			for(int i = 0; i < len-1; i++)
			{
				if(attempt >= currentStep && 
				   attempt < itemProbabilities[i].getInt("Probability")+currentStep)
				{
					AObject item = itemProbabilities[i].getObject("Item");
					item.curPos = curPos;
					field.addObject(item);
					return;
				}
				currentStep += itemProbabilities[i].getInt("Probability");
			}
			if(attempt >= currentStep && attempt < itemProbabilities[len-1].getInt("Probability")+currentStep)
			{
				AObject item = itemProbabilities[len-1].getObject("Item");
				item.curPos = curPos;
				field.addObject(item);
				return;
			}
		}
	}
	
	@Override public abstract boolean isEnemy(AObject ao);
	
	public boolean isPlayer()
	{
		return false;
	}
	
	@Override public boolean getPassability() // all livings are impossible to pass through
	{
		return false;
	}
	
	@Override public boolean getTransparency() // and to look through
	{
		return true;
	}

	public Point getOldPos() 
	{
		return old_pos;
	}

	public boolean isNearPlayer()
	{
		if(isPlayer())return true;
		for(AObject ao: Blame.getCurrentPlayer().getMyNeighbours())
		{
			if(this.equals(ao))return true;
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
	
	protected abstract void initStats();
	@Override public float getStat(String key)
	{
		return super.getStat(key) + weapon.getModifier(key);
	}
	
	public InventoryScreen getInventory()
	{
		return inventory;
	}
	
	public WeaponScreen getWeapon()
	{
		return weapon;
	}
	
	@Override public void changeState(ALiving changer, StateMap effects)
	{
		float acidDamage = effects.getFloat("AcidDamage");
		float acidResist = Math.min(getStat("AcidResist"), 75)*0.01f;
		
		float bioDamage = effects.getFloat("BioDamage");
		float bioResist = Math.min(getStat("BioResist"), 75)*0.01f;
		
		float electroDamage = effects.getFloat("ElectroDamage");
		float electroResist = Math.min(getStat("ElectroResist"), 75)*0.01f;
		
		float laserDamage = effects.getFloat("LaserDamage");
		float laserResist = Math.min(getStat("LaserResist"), 75)*0.01f;
		
		boolean isCritical = Math.random() <= Math.min(effects.getFloat("Critical"), 50)*0.01f;
		
		int damage = (int) ((acidDamage - acidDamage*acidResist) + 	// need to add accuracy modifier
					   		(bioDamage - bioDamage*bioResist) + 
					   		(electroDamage - electroDamage*electroResist) + 
					   		(laserDamage - laserDamage*laserResist)*Math.random());
		if(damage > 0 && isNearPlayer())
		{
			decreaseStat("Health", isCritical?damage*2:damage);
			if(isNearPlayer())
			{
				if(!isCritical) Messages.instance().addPropMessage("living.receivedamage", getName(), damage);
				else Messages.instance().addPropMessage("living.receivedamage", getName(), damage); // add different message!!
			}
		}
		
		boolean isKicked = Math.random() <= Math.min(effects.getFloat("Kick"), 33)*0.01f;
		if(isKicked && isMovable())
		{
			setDecision(new Move(this, field.getDirection(changer.curPos, curPos), field));
			if(isNearPlayer()) Messages.instance().addPropMessage("living.kickback", getName());
		}
	}

	public boolean isMovable() // stations cannot move
	{
		return true;
	}

	public Color getDamageColor() 
	{
		return weapon.getDamageColor();
	}
}
