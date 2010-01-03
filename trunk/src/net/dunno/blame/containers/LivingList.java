package net.dunno.blame.containers;

import java.util.LinkedList;
import java.util.ListIterator;

import net.dunno.blame.field.Field;
import net.dunno.blame.livings.SiliconCreature;
import net.dunno.blame.main.Blame;
import net.dunno.blame.prototypes.ALiving;

public class LivingList extends LinkedList<ALiving>
{
	private static final long serialVersionUID = 7325672295995481834L;
	private Field field;
	
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
		//Messages.instance().addMessage("----------------------");
	}
}
