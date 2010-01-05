package su.msk.dunno.blame.containers;

import java.util.LinkedList;
import java.util.ListIterator;

import su.msk.dunno.blame.livings.SiliconCreature;
import su.msk.dunno.blame.prototypes.ALiving;


public class LivingList extends LinkedList<ALiving>
{
	private static final long serialVersionUID = 7325672295995481834L;
	private Field field;
	private int time;
	
	public LivingList(Field field)
	{
		this.field = field;
	}
	
	public void addCreatures(int num)
	{
		for(int i = 0; i < num; i++)addObject(new SiliconCreature(field.getRandomPos(), field));
	}
	
	public void addObject(ALiving ao)
	{
		add(ao);
		field.addObject(ao);
	}
	
	public void removeObject(ALiving ao)
	{
		remove(ao);
		field.removeObject(ao);
	}
	
	public void nextStep()
	{
		for(ListIterator<ALiving> li = this.listIterator(); li.hasNext();)
		{
			ALiving al = li.next();
			al.updateOldPos();
			al.nextStep();
			if(al.getState().containsKey("CancelMove")) return;
			al.checkStatus(li);
		}
	}
}
