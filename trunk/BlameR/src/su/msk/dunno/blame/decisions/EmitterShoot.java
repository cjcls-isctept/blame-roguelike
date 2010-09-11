package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import su.msk.dunno.blame.animations.BulletFlight;
import su.msk.dunno.blame.animations.Moving;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.symbols.EmitterBullet;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.support.Point;

public class EmitterShoot extends Shoot 
{
	public EmitterShoot(ALiving al, Field field) 
	{
		super(al, field);
	}

	@Override public void doAction(int actionMoment) 
	{
		if(args != null)
		{
			LinkedList<Point> line = field.getLine(al.curPos, shootTo);
			if(line.size() > 1)
			{
				// animation
				field.playAnimation(new BulletFlight(new EmitterBullet(line.getFirst()), actionMoment, line.get(1), shootTo, field));
				// shooter's kickback
				/*al.setDecision(new Move(al, field.getDirection(line.get(1), al.cur_pos), field));
				al.setDecision(new Move(al, field.getDirection(line.get(1), al.cur_pos), field));*/
				Point old = al.curPos;
				al.curPos = al.curPos.mul(2).minus(line.get(1));
				//al.cur_pos = (al.cur_pos.minus(line.get(1))).plus(al.cur_pos);				
				//al.cur_pos = (al.cur_pos.minus(line.get(1))).mul(2).plus(al.cur_pos);				
				if(field.changeLocation(al) && al.isNearPlayer())
				{
					field.playAnimation(new Moving(actionMoment, field, al, old, al.curPos));
				}
			}
		}
		wasExecuted = true;
	}
}
