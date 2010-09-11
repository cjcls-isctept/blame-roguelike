package su.msk.dunno.blame.objects;

import java.util.LinkedList;
import java.util.ListIterator;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.livings.Cibo;
import su.msk.dunno.blame.objects.livings.Killy;
import su.msk.dunno.blame.objects.livings.SiliconCreature;
import su.msk.dunno.blame.prototypes.ALiving;


public class Livings
{
	private static final long serialVersionUID = 7325672295995481834L;
	private static Livings instance;
	
	public static Livings instance()
	{
		if(instance == null)
		{
			instance = new Livings();
		}
		return instance;
	}
	
	private Livings() {}
	
	private Field field;
	
	private int time;	// must be private in the release!
	
	private LinkedList<ALiving> livings = new LinkedList<ALiving>();
	private LinkedList<ALiving> stations = new LinkedList<ALiving>();
	private Killy killy;
	private Cibo cibo;
	
	// mob statistics
	private int numSiliconCreatures = 0;
	private int numCorruptedStations = 0;
	
	public void addField(Field field)
	{
		this.field = field;		
	}
	
	public void addKilly(Killy killy)
	{
		this.killy = killy;
	}
	
	public void addCibo(Cibo cibo)
	{
		this.cibo = cibo;
	}
	
	public void addCreatures(int num)
	{
		for(int i = 0; i < num; i++)addObject(new SiliconCreature(field.getRandomPos(), field));
	}
	
	public void addObject(ALiving l)
	{
		livings.add(l);
		if(l.getState().containsKey("Station"))stations.add(l);
	}
	
	/*public void removeObject(ALiving ao)
	{
		livings.remove(ao);
		field.removeObject(ao);
	}*/
	
	public void nextStep()
	{
		// update state of the current player
		if(!(Blame.playCibo?cibo:killy).isDead)
		{
			/*(Blame.playCibo?cibo:killy).increaseInfectionLevel();*/
			if(!(Blame.playCibo?cibo:killy).checkStatus(null))
			{
				(Blame.playCibo?cibo:killy).nextStep();
				(Blame.playCibo?cibo:killy).updateOldPos();				
				if((Blame.playCibo?cibo:killy).getState().containsKey("CancelMove"))return;
			}
		}
		
		// update other livings
		while(time - (Blame.playCibo?cibo:killy).getLastActionTime() < (Blame.playCibo?cibo:killy).getActionPeriod())
		{
			// update state of the second player (if not CancelMove)
			if(!(Blame.playCibo?killy:cibo).isDead)
			{
				/*(Blame.playCibo?killy:cibo).increaseInfectionLevel();*/
				if(!(Blame.playCibo?killy:cibo).checkStatus(null))
				{
					(Blame.playCibo?killy:cibo).nextStep();
					(Blame.playCibo?killy:cibo).updateOldPos();
				}
			}
			
			// update monsters
			int numSiliconCreatures_new = 0;
			int numCorruptedStations_new = 0;
			for(ListIterator<ALiving> li = livings.listIterator(); li.hasNext();)
			{
				ALiving al = li.next();
				if(!al.checkStatus(li))
				{
					if(al.getState().containsKey("SiliconCreature")) numSiliconCreatures_new++;
					if(al.getState().containsKey("Corrupted")) numCorruptedStations_new++;
					
					al.nextStep();
					al.updateOldPos();
				}
			}
			
			numSiliconCreatures = numSiliconCreatures_new;
			numCorruptedStations = numCorruptedStations_new;
			time++;
		}
	}

	public int getTime() {
		return time;
	}

	public void clear() 
	{
		livings.clear();
	}
}
