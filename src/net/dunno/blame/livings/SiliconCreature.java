package net.dunno.blame.livings;

import java.util.HashMap;

import net.dunno.blame.decisions.Move;
import net.dunno.blame.field.Field;
import net.dunno.blame.main.support.Color;
import net.dunno.blame.main.support.Point;
import net.dunno.blame.prototypes.ADecision;
import net.dunno.blame.prototypes.ALiving;

public class SiliconCreature extends ALiving 
{
	public SiliconCreature(Point p, Field field) 
	{
		super(p, field);
		health = 20;
		dov = 5;
		action_period = 0;
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
		return 's';
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
