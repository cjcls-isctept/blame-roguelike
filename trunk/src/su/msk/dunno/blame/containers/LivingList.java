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
	
	public int time;	// must be private in the release!
	
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
		if(!(Blame.playCibo?cibo:killy).isDead)
		{
			(Blame.playCibo?cibo:killy).checkPlayerStatus();
			(Blame.playCibo?cibo:killy).updateOldPos();
			(Blame.playCibo?cibo:killy).nextStep(time);
			if((Blame.playCibo?cibo:killy).getState().containsKey("CancelMove")) return;
		}
		
		// update state of the second player (if not CancelMove)
		if(!(Blame.playCibo?killy:cibo).isDead)
		{
			(Blame.playCibo?killy:cibo).checkPlayerStatus();
			(Blame.playCibo?killy:cibo).updateOldPos();
			(Blame.playCibo?killy:cibo).nextStep(time);
		}
		
		// update monsters
		while(time - (Blame.playCibo?cibo:killy).getLastActionTime() < (Blame.playCibo?cibo:killy).getActionPeriod())
		{
			for(ListIterator<ALiving> li = livings.listIterator(); li.hasNext();)
			{
				ALiving al = li.next();
				al.checkStatus(li);
				al.updateOldPos();
				al.nextStep(time);
			}
			time++;
		}
	}
}
