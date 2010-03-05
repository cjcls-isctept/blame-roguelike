package su.msk.dunno.blame.decisions;

import java.util.HashMap;
import java.util.LinkedList;

import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.StateMap;

public class EnterStation extends ADecision 
{
	Field field;
	
	public EnterStation(ALiving al, Field field) 
	{
		super(al);
		this.field = field;
	}

	@Override public void doAction(int actionMoment) 
	{
		LinkedList<AObject> lao = field.getObjectsAtPoint(al.cur_pos); 
		for(AObject ao: lao)
		{
			if(ao.getState().containsKey("Station"))
			{
				/*HashMap<String, String> args = new HashMap<String, String>();
				args.put("Enter", al.getName());*/
				StateMap args = new StateMap("Enter", al.getName());
				ao.changeState(al, args);
			}
		}
	}
}
