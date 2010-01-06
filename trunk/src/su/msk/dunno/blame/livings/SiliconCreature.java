package su.msk.dunno.blame.livings;

import java.util.HashMap;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;


public class SiliconCreature extends ALiving 
{
	public SiliconCreature(Point p, Field field) 
	{
		super(p, field);
		health = 20;
		dov = 5;
		actionPeriod = 3;
	}

	@Override public ADecision livingAI() 
	{
		return new Move(this, (int)(Math.random()*9), field);
	}

	@Override public String getName() 
	{
		return "Silicon Creature";
	}

	@Override public char getSymbol() 
	{
		return 'S';
	}
	
	@Override public Color getColor()
	{
		return Color.CYAN;
	}
	
	@Override public void changeState(HashMap<String, Integer> args)
	{
		if(args.containsKey("Damage"))
		{
			health -= args.get("Damage");
		}
	}

	@Override public boolean isEnemy() 
	{
		return true;
	}
}
