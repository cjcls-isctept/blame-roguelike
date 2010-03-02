package su.msk.dunno.blame.objects.livings;

import java.util.HashMap;

import su.msk.dunno.blame.decisions.MeleeAttack;
import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.map.path.PathFinder;
import su.msk.dunno.blame.map.path.astar.AStarPathFinder;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.screens.EnemyInterface;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;


public class SiliconCreature extends ALiving 
{
	PathFinder find;
	int steps;
	IScreen mind = new EnemyInterface(this);
	
	public SiliconCreature(Point p, Field field) 
	{
		super(p, field);
		health = 20;
		dov = 5;
		speed = 3;
		find = new AStarPathFinder(field);
	}

	@Override public ADecision livingAI() 
	{
		int minDist = field.getN_x();
		for(AObject ao: this.getMyNeighbours())
		{
			if(this.isEnemy(ao))	// attack sequence
			{
				minDist = Math.min(this.cur_pos.getDist2(ao.cur_pos), minDist);
				int dir = field.getDirection(cur_pos, ao.cur_pos);
				if(this.isEnemyAtDir(dir))return new MeleeAttack(this, dir)/*null*/;
				else  
				{
					if(getPassabilityAtDir(dir))
					{
						return new Move(this, dir, field);						
					}
					else return new Move(this, (int)(Math.random()*9), field); 
				}
			}
		}
		return new Move(this, (int)(Math.random()*9), field);
		/*steps--;	// explore sequence
		if(!find.path.isEmpty() && cur_pos.equals(find.path.getFirst()))find.path.removeFirst();	// remove the point we reach on the prevoius step
		if(find.path.isEmpty() || steps == 0)	// create a new path to some random point
		{
			Point goTo = field.getRandomPos();
			find.findPath(cur_pos, goTo);
			if(find.path.isEmpty())	// couldn't create a path - go to random direction
			{
				return new Move(this, (int)(Math.random()*9), field);
			}
			steps = find.path.size()*2;
		}
		AObject nextTile = field.getObjectsAtPoint(find.path.getFirst()).getFirst();
		if("Close door".equals(nextTile.getName()))	//found a door near us
		{
			return new Open(this);	// open it
		}
		if(!field.getPassability(find.path.getFirst()))	// there is an obstacle on our way
		{
			return new Move(this, (int)(Math.random()*9), field);	// go to random dir
		}
		// if everything ok - go to the next point in our path
		return new Move(this, field.getDirection(cur_pos, find.path.getFirst()), field);*/
	}

	@Override public String getName() 
	{
		return "Silicon Creature";
	}
	
	@Override public int getCode()
	{
		return MyFont.SILICONCREATURE;
	}
	
	@Override public Color getColor()
	{
		return Color.CYAN;
	}
	
	@Override public void changeState(ALiving changer, HashMap<String, String> args)
	{
		if(args.containsKey("Damage"))
		{
			int d = (int)(Math.random()*Float.valueOf(args.get("Damage")));
			health -= d;
			if(isNearPlayer())Messages.instance().addPropMessage("living.receivedamage", getName(), d+"");
			
		}
		if(args.containsKey("Kick"))
		{
			setDecision(new Move(this, field.getDirection(changer.cur_pos, cur_pos), field));
			if(isNearPlayer())Messages.instance().addPropMessage("living.kickback", getName());
		}
		if(args.containsKey("MindHack"))
		{
			mind.process();
			if(isNearPlayer())Messages.instance().addPropMessage("living.mindhack", getName());
		}
	}

	@Override public boolean isEnemy(AObject ao) 
	{
		return "Killy".equals(ao.getName()) || "Cibo".equals(ao.getName());
	}

	@Override public boolean isPlayer() 
	{
		return false;
	}
}
