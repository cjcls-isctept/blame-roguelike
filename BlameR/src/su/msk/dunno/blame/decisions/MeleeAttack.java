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
			if(al.isEnemy(ao) || ao.isEnemy(al))	// needs to avoid messages about shooting to floor, door, etc ("Killy shoots to floor")
			{
				if(al.isNearPlayer())if(al.isNearPlayer())Messages.instance().addPropMessage("decision.melee", al.getName(), ao.getName());
				args = al.getWeapon().showEffects();
				if(args != null) ao.changeState(al, args);
			}
		}
		wasExecuted = true;
	}
}
