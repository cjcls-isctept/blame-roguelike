package su.msk.dunno.blame.prototypes;

import java.util.Calendar;

import su.msk.dunno.blame.containers.Field;

public abstract class AAnimation 
{
	protected Field field;
	
	protected long start_time;	// in msecs
	protected long time;
	protected long duration;
	
	protected int frames;
	protected long cur_frame;	
	
	protected boolean isRepeatable;
	public boolean isEnded;
	
	public AAnimation(Field field, boolean isRepeatable)
	{
		this.field = field;
		this.isRepeatable = isRepeatable;
		start_time = Calendar.getInstance().getTimeInMillis();
	}
	
	public abstract void nextFrame();
	
	public void play()
	{
		time = Calendar.getInstance().getTimeInMillis(); 
		if(time - start_time > duration)
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
			long i = (time - start_time)*frames/duration;
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
