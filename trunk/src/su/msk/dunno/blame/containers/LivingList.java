package su.msk.dunno.blame.containers;

import java.util.LinkedList;
import java.util.ListIterator;

import su.msk.dunno.blame.livings.Cibo;
import su.msk.dunno.blame.livings.Killy;
import su.msk.dunno.blame.livings.SiliconCreature;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.prototypes.ALiving;


public class LivingList
{
	private static final long serialVersionUID = 7325672295995481834L;
	private Field field;
	
	public int time;
	
	private LinkedList<ALiving> livings;
	private Killy killy;
	private Cibo cibo;
	
	public LivingList(Field field, Killy killy, Cibo cibo)
	{
		this.field = field;
		this.killy = killy;
		field.addObject(killy);
		this.cibo = cibo;
		field.addObject(cibo);
		livings = new LinkedList<ALiving>();
	}
	
	public void addCreatures(int num)
	{
		for(int i = 0; i < num; i++)addObject(new SiliconCreature(field.getRandomPos(), field));
	}
	
	public void addObject(ALiving ao)
	{
		livings.add(ao);
		field.addObject(ao);
	}
	
	public void removeObject(ALiving ao)
	{
		livings.remove(ao);
		field.removeObject(ao);
	}
	
	public void nextStep()
	{
		// update state of the current player
		(Blame.playCibo?cibo:killy).updateOldPos();
		(Blame.playCibo?cibo:killy).nextStep(time);
		if((Blame.playCibo?cibo:killy).getState().containsKey("CancelMove")) return;
		
		// update state of the second player (if not CancelMove)
		(Blame.playCibo?killy:cibo).updateOldPos();
		(Blame.playCibo?killy:cibo).nextStep(time);		
		
		// update monsters
		while(time - (Blame.playCibo?cibo:killy).lastAction_time < (Blame.playCibo?cibo:killy).actionPeriod)
		{
			for(ListIterator<ALiving> li = livings.listIterator(); li.hasNext();)
			{
				ALiving al = li.next();
				al.updateOldPos();
				al.nextStep(time);
				al.checkStatus(li);
			}
			time++;
		}
	}
}
