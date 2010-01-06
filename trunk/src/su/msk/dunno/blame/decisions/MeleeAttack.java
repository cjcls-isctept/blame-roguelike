package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.main.support.Messages;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;

public class MeleeAttack extends ADecision
{
	int dir;
	
	public MeleeAttack(ALiving al, int dir) 
	{
		super(al);
		this.dir = dir;
		args.put("Damage", (int)(Math.random()*20));
	}

	@Override public void doAction() 
	{
		for(AObject ao: al.getObjectsAtDir(dir))
		{
			if(al.isEnemy(ao))
			{
				ao.changeState(args);
				if(al.isNearPlayer())
				{
					if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" deals "+args.get("Damage")+" damage to "+ao.getName());
				}
			}
		}
		wasExecuted = true;
	}
}
