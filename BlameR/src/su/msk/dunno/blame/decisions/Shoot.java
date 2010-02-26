package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.animations.BulletFlight;
import su.msk.dunno.blame.animations.Moving;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.livings.Killy;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.prototypes.ISelector;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.Point;

public class Shoot extends ADecision implements ISelector 
{
	Point shootTo;
	Field field;
	
	public Shoot(ALiving al, Field field)
	{
		super(al);
		this.field = field;
	}
	
	public Shoot(ALiving al, Field field, Point selectPoint) 
	{
		this(al, field);
		shootTo = selectPoint;
	}

	@Override public void setSelectPoint(Point selectPoint) 
	{
		shootTo = selectPoint;
	}

	@Override public void doAction(int actionMoment) 
	{
		args = al.weapon.applyEffects();
		if(args != null)
		{
			LinkedList<Point> line = field.getLine(al.cur_pos, shootTo);
			if(line.size() > 1)
			{
				// animation
				BulletFlight bf = new BulletFlight(actionMoment, line.get(1), shootTo, field);
				field.addAnimation(bf);
				/*while(!bf.isEnded)
				{
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);		
					GL11.glLoadIdentity();
					
					field.draw(Blame.getCurrentPlayer().cur_pos);		
					Blame.getCurrentPlayer().drawStats();
					Display.sync(Blame.framerate);
					Display.update();
				}*/
				// shooter's kickback
				Point old = al.cur_pos;
				al.cur_pos = al.cur_pos.mul(2).minus(line.get(1));
				if(field.changeLocation(al) && al.isNearPlayer())
				{
					Moving mv = new Moving(actionMoment, field, al, old, al.cur_pos);
					field.addAnimation(mv);
					/*while(!mv.isEnded)
					{
						GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);		
						GL11.glLoadIdentity();
						
						field.draw(Blame.getCurrentPlayer().cur_pos);		
						Blame.getCurrentPlayer().drawStats();
						Display.sync(Blame.framerate);
						Display.update();
					}*/
				}			
			}
			LinkedList<AObject> lao = field.getObjectsAtPoint(shootTo).clone();		// clone() due to some bugs if not...
			for(AObject ao: lao)
			{
				if(al.isEnemy(ao))
				{
					if(al.isNearPlayer()) Messages.instance().addMessage(al.getName()+" shoots to "+ao.getName());
					ao.changeState(args);
				}
			}
		}
		else if(al.isNearPlayer())Messages.instance().addMessage("Not enough energy!");
		wasExecuted = true;
	}
}
