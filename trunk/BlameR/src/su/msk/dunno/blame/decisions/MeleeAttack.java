package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Messages;

public class MeleeAttack extends ADecision
{
	int dir;
	
	public MeleeAttack(ALiving al, int dir) 
	{
		super(al);
		this.dir = dir;
		args.put("Damage", (int)(Math.random()*20)+"");
	}

	@Override public void doAction(int actionMoment) 
	{
		for(AObject ao: al.getObjectsAtDir(dir))
		{
			if(al.isEnemy(ao))
			{
				if(al.isNearPlayer())if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" strikes at "+ao.getName());
				ao.changeState(al, args);
			}
		}
		wasExecuted = true;
	}
}
