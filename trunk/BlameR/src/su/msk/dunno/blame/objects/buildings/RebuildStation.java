package su.msk.dunno.blame.objects.buildings;

import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;

public class RebuildStation extends ALiving 
{
	private boolean isGreetingsMessageDone;
	
	public RebuildStation(int i, int j, Field field) 
	{
		super(i, j, field);
		dov = 2;
	}

	@Override public Color getColor() 
	{
		return Color.RED;
	}

	@Override public boolean isEnemy(AObject ao) 
	{
		return false;
	}

	@Override public boolean isPlayer() 
	{
		return false;
	}

	@Override public ADecision livingAI() 
	{
		if(isNearPlayer())
		{
			if(!isGreetingsMessageDone)
			{
				Messages.instance().addMessage("Entering "+getName()+"!");
				isGreetingsMessageDone = true;
			}
		}
		else if(isGreetingsMessageDone)
		{
			isGreetingsMessageDone = false;
		}
		return null;
	}

	@Override public String getName() 
	{
		return "Rebuild Station";
	}

	@Override public int getCode()
	{
		return MyFont.STATION;
	}
	
	@Override public boolean getPassability()
	{
		return true;
	}
}
