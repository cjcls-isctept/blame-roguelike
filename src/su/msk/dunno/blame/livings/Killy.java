package su.msk.dunno.blame.livings;

import java.util.HashMap;
import java.util.LinkedList;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.decisions.Close;
import su.msk.dunno.blame.decisions.MeleeAttack;
import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.decisions.Open;
import su.msk.dunno.blame.decisions.Shoot;
import su.msk.dunno.blame.decisions.Take;
import su.msk.dunno.blame.items.PlayerCorpse;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.ADecision;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.symbols.MainSelector;
import su.msk.dunno.blame.symbols.MinorSelector;


public class Killy extends ALiving 
{
	//0 - up/2; 1 - left/4; 2 - down/2; 3 - right/6; 4 - 9; 5 - 7; 6 - 1; 7 - 3; 8 - 5
	public boolean[] keys = new boolean[9];

	public boolean wantOpen;
	public boolean wantClose;
	public boolean wantTake;
	public boolean wantShoot;
	
	public boolean isSelectTarget;
	public Point selectPoint;
	protected LinkedList<MinorSelector> selectLine;
	
	boolean isCancelMove;
	boolean isMoving;
	
	public Killy(Point p, Field field) 
	{
		this(p.x, p.y, field);
	}
	
	public Killy(int i, int j, Field field) 
	{
		super(i, j, field);
		health = 100;
		actionPeriod = 3;
		dov = 5;
		
		selectLine = new LinkedList<MinorSelector>();
	}

	@Override public ADecision livingAI() 
	{
		
		if(keys[0])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y+1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.UP))return new MeleeAttack(this, Move.UP);
				else if(!isMoving)
				{
					return new Move(this, Move.UP, field);
				}
			}
		}
		else if(keys[1])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.LEFT))return new MeleeAttack(this, Move.LEFT);
				else if(!isMoving)
				{
					return new Move(this, Move.LEFT, field);
				}
			}
		}
		else if(keys[2])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x, selectPoint.y-1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.DOWN))return new MeleeAttack(this, Move.DOWN);
				else if(!isMoving)
				{
					return new Move(this, Move.DOWN, field);
				}
			}
		}
		else if(keys[3])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.RIGHT))return new MeleeAttack(this, Move.RIGHT);
				else if(!isMoving)
				{
					return new Move(this, Move.RIGHT, field);
				}
			}
		}
		else if(keys[4])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y+1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.UPRIGHT))return new MeleeAttack(this, Move.UPRIGHT);
				else if(!isMoving)
				{
					return new Move(this, Move.UPRIGHT, field);
				}
			}
		}
		else if(keys[5])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y+1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.UPLEFT))return new MeleeAttack(this, Move.UPLEFT);
				else if(!isMoving)
				{
					return new Move(this, Move.UPLEFT, field);
				}
			}
		}
		else if(keys[6])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x-1, selectPoint.y-1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.DOWNLEFT))return new MeleeAttack(this, Move.DOWNLEFT);
				else if(!isMoving)
				{
					return new Move(this, Move.DOWNLEFT, field);
				}
			}
		}
		else if(keys[7])
		{
			if(selectLine.size() > 0)
			{
				selectPoint = new Point(selectPoint.x+1, selectPoint.y-1);
				buildLine();
				isSelectTarget = true;
			}
			else
			{
				if(isEnemyAtDir(Move.DOWNRIGHT))return new MeleeAttack(this, Move.DOWNRIGHT);
				else if(!isMoving)
				{
					return new Move(this, Move.DOWNRIGHT, field);
				}
			}
		}
		else if(keys[8])return new Move(this, Move.STAY, field);
		else if(wantOpen)return new Open(this);
		else if(wantClose)return new Close(this);
		else if(wantTake)return new Take(this);
		else if(wantShoot) 
		{
			if(selectLine.size() > 0)
			{
				clearLine();
				Shoot sh = new Shoot(this, selectPoint, field);
				selectPoint = null;
				return sh;
			}
			else 
			{
				selectPoint = cur_pos;
				LinkedList<AObject> neighbours = getMyNeighbours();
				for(AObject ao: neighbours)
				{
					if(isEnemy(ao))
					{
						selectPoint = ao.cur_pos;
						break;
					}
				}
				buildLine();
				isSelectTarget = true;
			}
		}
		return null;
	}

	@Override public char getSymbol() 
	{
		return '@';
	}

	@Override public String getName() 
	{
		return "Killy";
	}
	
	@Override public Color getColor()
	{
		/*if(isDead) return Color.WHITE;
		else */return Color.RED;
	}
	
	public void reset_keys()
	{
		for(int i = 0; i < keys.length; i++)keys[i] = false;
		wantOpen = false;
		wantClose = false;
		wantTake = false;
		wantShoot = false;
		isSelectTarget = false;
	}
	
	@Override public void changeState(HashMap<String, Integer> args)
	{
		if(args.containsKey("Damage"))
		{
			health -= args.get("Damage");
		}
		if(args.containsKey("HealthPlus"))
		{
			health += args.get("HealthPlus");
		}
		if(args.containsKey("MoveFail"))
		{
			isCancelMove = true;
		}
		if(args.containsKey("MovingStarted"))
		{
			isMoving = true;
		}
		if(args.containsKey("MovingEnded"))
		{
			isMoving = false;
		}
	}
	
	@Override public HashMap<String, Integer> getState() 
	{
		HashMap<String, Integer> state = new HashMap<String, Integer>();
		if(isSelectTarget || isCancelMove)
		{
			state.put("CancelMove", 1);
			isCancelMove = false;
		}
		return state;
	}
	
	protected void buildLine()
	{
		clearLine();
		LinkedList<Point> line = field.getLine(cur_pos, selectPoint);
		int i = 0;
		for(Point p: line)
		{
			i++;
			if(line.size() > 1 && i == 1)continue;	//	skip the first element if amount of elements is more than 1
			if(field.onArea(p) && field.isMapVisible(p, cur_pos, dov))
			{
				MinorSelector s = new MinorSelector(p);
				selectLine.add(s);
			}
			else break;
		}
		if(selectLine.size() > 0)
		{
			selectLine.set(selectLine.size()-1, new MainSelector(selectLine.getLast().cur_pos));
			selectPoint = selectLine.getLast().cur_pos;
		}
		for(MinorSelector s: selectLine)field.addObject(s);
	}
	
	public void clearLine()
	{
		for(MinorSelector s: selectLine)
		{
			field.removeObject(s);
		}
		selectLine.clear();
	}
	
	@Override public boolean isLightSource()
	{
		return true;
	}

	@Override public boolean isEnemy(AObject ao) 
	{
		return "Silicon Creature".equals(ao.getName());
	}
	
	public void checkPlayerStatus()
	{
		if(health < 0)isDead = true;
		if(isDead)
		{
			field.removeObject(this);
			field.addObject(new PlayerCorpse(cur_pos));
		}
	}
}
