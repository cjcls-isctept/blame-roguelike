package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import su.msk.dunno.blame.animations.BulletFlight;
import su.msk.dunno.blame.animations.Moving;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.symbols.Bullet;
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
		args = al.getWeapon().applyEffects();
		if(args != null)
		{
			LinkedList<Point> line = field.getLine(al.cur_pos, shootTo);
			if(line.size() > 1)
			{
				// animation
				field.playAnimation(new BulletFlight(new Bullet(line.getFirst()), actionMoment, line.get(1), shootTo, field));
				// shooter's kickback
				Point old = al.cur_pos;
				al.cur_pos = al.cur_pos.mul(2).minus(line.get(1));
				if(field.changeLocation(al) && al.isNearPlayer())
				{
					field.playAnimation(new Moving(actionMoment, field, al, old, al.cur_pos));
				}			
			}
			LinkedList<AObject> lao = field.getObjectsAtPoint(shootTo).clone();		// clone() due to some bugs if not...
			for(AObject ao: lao)
			{
				if(al.isEnemy(ao) || ao.isEnemy(al))	// needs to avoid messages about shooting to floor, door, etc ("Killy shoots to floor")
				{
					if(al.isNearPlayer()) Messages.instance().addPropMessage("decision.shoot", al.getName(), ao.getName());
					ao.changeState(al, args);
				}
			}
		}
		else if(al.isNearPlayer())Messages.instance().addPropMessage("decision.shoot.noenergy", al.getName());
		wasExecuted = true;
	}
}
