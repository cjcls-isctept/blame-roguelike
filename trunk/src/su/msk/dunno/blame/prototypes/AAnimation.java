package su.msk.dunno.blame.prototypes;

import java.util.Calendar;

import su.msk.dunno.blame.containers.Field;

public abstract class AAnimation 
{
	protected Field field;

	protected int time;
	protected int duration;
	
	protected int frames;
	protected int cur_frame;	
	
	protected boolean isRepeatable;
	public boolean isEnded;
	
	public AAnimation(Field field, boolean isRepeatable)
	{
		this.field = field;
		this.isRepeatable = isRepeatable;
	}
	
	public abstract void nextFrame();
	
	public void play()
	{
		time++;
		if(time > duration)
		{
			if(isRepeatable)restart();
			else 
			{
				stop();
				isEnded = true;
			}
		}
		else
		{
			int i = time*frames/duration;
			if(i > cur_frame)
			{
				nextFrame();
				cur_frame = i;
			}
		}
	}
	
	public void stop()
	{

	}
	
	public void restart()
	{
		
	}
	
	public long getDuration()
	{
		return duration;
	}
}
