package su.msk.dunno.blame.decisions;

import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.StateMap;

public class MeleeAttack extends ADecision
{
	int dir;
	
	public MeleeAttack(ALiving al, int dir) 
	{
		super(al);
		this.dir = dir;
	}

	@Override public void doAction(int actionMoment) 
	{
		for(AObject ao: al.getObjectsAtDir(dir))
		{
			/*if(al.isEnemy(ao) || ao.isEnemy(al))	// enemy status must be checked in livingAI() section, not here!
			{*/
				if(al.isNearPlayer())if(al.isNearPlayer())Messages.instance().addPropMessage("decision.melee", al.getName(), ao.getName());
				ao.changeState(al, new StateMap("Damage", (int)(Math.random()*20)));
			/*}*/
		}
		wasExecuted = true;
	}
}
