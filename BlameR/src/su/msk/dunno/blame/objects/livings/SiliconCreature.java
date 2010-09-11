package su.msk.dunno.blame.objects.livings;

import su.msk.dunno.blame.decisions.MeleeAttack;
import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.map.path.PathFinder;
import su.msk.dunno.blame.map.path.astar.AStarPathFinder;
import su.msk.dunno.blame.objects.items.ImpAcid;
import su.msk.dunno.blame.objects.items.ImpAcidRes;
import su.msk.dunno.blame.objects.items.SiliconCorpse;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.IScreen;
import su.msk.dunno.blame.screens.EnemyInterface;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;


public class SiliconCreature extends ALiving 
{
	PathFinder find;
	int steps;
	IScreen mind = new EnemyInterface(this);
	
	public SiliconCreature(Point p, Field field) 
	{
		super(p, field);	
		find = new AStarPathFinder(field);
	}
	
	@Override protected void initStats() 
	{
		setStat("Dov", 6);		// higher than player's - they' sense player earlier than he do
		setStat("Health", 20);
		setStat("Speed", 3);	// lower the parameter - faster mob
	}
	
	@Override protected void initItemDrop() 
	{
		mustBeDropped.add(new SiliconCorpse(new Point(0,0)));
		
		itemProbabilities = new StateMap[2];
		itemProbabilities[0] = new StateMap("Probability", 50).putObject("Item", new ImpAcid(new Point(0,0)));
		itemProbabilities[1] = new StateMap("Probability", 10).putObject("Item", new ImpAcidRes(new Point(0,0)));
	}

	@Override public ADecision livingAI() 
	{
		int minDist = field.getN_x();
		for(AObject ao: getMyNeighbours())
		{
			if(isEnemy(ao) || ao.isEnemy(this))	// attack sequence
			{
				minDist = Math.min(this.curPos.getDist2(ao.curPos), minDist);
				int dir = field.getDirection(curPos, ao.curPos);
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
	
	@Override public int getSymbol()
	{
		return MyFont.SILICONCREATURE;
	}
	
	@Override public Color getColor()
	{
		return Color.CYAN;
	}
	
	@Override public void changeState(ALiving changer, StateMap effects)
	{
		super.changeState(changer, effects);
		if(effects.containsKey("MindHack"))
		{
			mind.process();
			if(isNearPlayer())Messages.instance().addPropMessage("living.mindhack", getName());
		}
	}
	
	@Override public StateMap getState()
	{
		return new StateMap("SiliconCreature");
	}

	@Override public boolean isEnemy(AObject ao) 
	{
		StateMap sm = ao.getState();
		return sm.containsKey("Player") || (sm.containsKey("Station") && !sm.containsKey("Corrupted"));
	}
}
