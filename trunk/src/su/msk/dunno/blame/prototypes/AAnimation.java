package su.msk.dunno.blame.prototypes;

import su.msk.dunno.blame.field.Field;

public abstract class AAnimation 
{
	protected Field field;
	private int start_time;	// in msecs
	private int duration;
	private boolean isRepeatable;
	
	public boolean isEnded;
	
	public AAnimation(Field field, boolean isRepeatable)
	{
		this.field = field;
		this.isRepeatable = isRepeatable;
	}
	
	public abstract void play();
	
	public void stop()
	{
		isEnded = true;
	}
}
