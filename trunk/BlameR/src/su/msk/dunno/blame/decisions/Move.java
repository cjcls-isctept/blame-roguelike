package su.msk.dunno.blame.decisions;

import java.util.LinkedList;

import su.msk.dunno.blame.animations.Moving;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.StateMap;


public class Move extends ADecision 
{
	public final static int UP = 0;
	public final static int LEFT = 1;
	public final static int DOWN = 2;
	public final static int RIGHT = 3;
	public final static int UPRIGHT = 4;
	public final static int UPLEFT = 5;
	public final static int DOWNLEFT = 6;
	public final static int DOWNRIGHT = 7;
	public final static int STAY = 8;
	// dir - direction to move: 0 = up, 1 = left, 2 = down, 3 = right, 4 - up/right, 5 - up/left, 
	// 6 - down/left, 7 - down/right, 8 - stay
	int dir;
	Field field;
	
	public Move(ALiving al, int dir, Field field)
	{
		super(al);
		this.dir = dir;
		this.field = field;
	}

	@Override public void doAction(int actionMoment) 
	{
		Point old = al.curPos;
		switch(dir)
		{
		case 0: // rewrite this with plus(int i, int j) from Point class
			al.curPos = new Point(al.curPos.x, al.curPos.y+1); 
			break;	// up
		case 1: 
			al.curPos = new Point(al.curPos.x-1, al.curPos.y); 
			break;	// left
		case 2: 
			al.curPos = new Point(al.curPos.x, al.curPos.y-1); 
			break;	// down
		case 3: 
			al.curPos = new Point(al.curPos.x+1, al.curPos.y); 
			break;	// right
		case 4:
			al.curPos = new Point(al.curPos.x+1, al.curPos.y+1); 
			break;	// up/right
		case 5:
			al.curPos = new Point(al.curPos.x-1, al.curPos.y+1); 
			break;	// up/left
		case 6:
			al.curPos = new Point(al.curPos.x-1, al.curPos.y-1); 
			break;	// down/left
		case 7:
			al.curPos = new Point(al.curPos.x+1, al.curPos.y-1); 
			break;	// down/right
		case 8:
			//al.cur_pos = new Point(al.cur_pos.x, al.cur_pos.y); 
			break;	// down/right
		}
		if(!field.changeLocation(al))
		{
			al.changeState(al, new StateMap("MoveFail"));
		}
		else if(al.isNearPlayer() && dir != STAY)
		{
			LinkedList<AObject> items = al.getObjectsAtDir(STAY);
			if(items.size() == 2)
			{
				AObject item = al.getObjectsAtDir(STAY).getLast();
				if(item.getState().containsKey("Item") || item.getState().containsKey("OnFloor")) 
					Messages.instance().addMessage(al.getName()+" found "+item.getName()+" on the floor");
			}
			else if(items.size() > 2) Messages.instance().addMessage(al.getName()+" found several items on the floor");
			
			if(!al.isPlayer()) field.playAnimation(new Moving(actionMoment, field, al, old, al.curPos));
		}
		wasExecuted = true;				
	}
}
