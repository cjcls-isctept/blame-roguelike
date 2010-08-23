package su.msk.dunno.blame.screens;

import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.IScreen;

public class FieldScreen implements IScreen 
{
	private Field field;
	public FieldScreen(Field f)
	{
		field = f;
	}
	
	private boolean isRunning;
	public void process() 
	{
		isRunning = true;
		while(isRunning)
		{
			
		}
	}
}
