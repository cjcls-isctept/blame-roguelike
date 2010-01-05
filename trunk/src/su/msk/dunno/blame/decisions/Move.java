package su.msk.dunno.blame.decisions;

import java.util.HashMap;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;


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

	@Override public void doAction() 
	{
		switch(dir)
		{
		case 0: 
			al.cur_pos = new Point(al.cur_pos.x, al.cur_pos.y+1); 
			break;	// up
		case 1: 
			al.cur_pos = new Point(al.cur_pos.x-1, al.cur_pos.y); 
			break;	// left
		case 2: 
			al.cur_pos = new Point(al.cur_pos.x, al.cur_pos.y-1); 
			break;	// down
		case 3: 
			al.cur_pos = new Point(al.cur_pos.x+1, al.cur_pos.y); 
			break;	// right
		case 4:
			al.cur_pos = new Point(al.cur_pos.x+1, al.cur_pos.y+1); 
			break;	// up/right
		case 5:
			al.cur_pos = new Point(al.cur_pos.x-1, al.cur_pos.y+1); 
			break;	// up/left
		case 6:
			al.cur_pos = new Point(al.cur_pos.x-1, al.cur_pos.y-1); 
			break;	// down/left
		case 7:
			al.cur_pos = new Point(al.cur_pos.x+1, al.cur_pos.y-1); 
			break;	// down/right
		case 8:
			//al.cur_pos = new Point(al.cur_pos.x, al.cur_pos.y); 
			break;	// down/right
		}
		if(!field.changeLocation(al))
		{
			HashMap<String, Integer> args = new HashMap<String, Integer>();
			args.put("MoveFail", 1);
			al.changeState(args);
		}
		wasExecuted = true;
	}
}
