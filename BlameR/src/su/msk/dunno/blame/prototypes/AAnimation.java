package su.msk.dunno.blame.prototypes;

import su.msk.dunno.blame.map.Field;

public abstract class AAnimation 
{
	protected Field field;

	protected int count;
	protected int duration;
	
	protected int frames;
	protected int cur_frame;	
	
	protected boolean isRepeatable;
	public boolean isEnded;
	
	public int cur_time;
	
	public AAnimation(int cur_time, Field field, boolean isRepeatable)
	{
		this.cur_time = cur_time;
		this.field = field;
		this.isRepeatable = isRepeatable;
	}
	
	public abstract void nextFrame();
	
	public void play()
	{
		count++;
		if(count > duration)
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
			int i = count*frames/duration;
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
