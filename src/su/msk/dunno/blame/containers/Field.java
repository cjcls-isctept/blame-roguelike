package su.msk.dunno.blame.containers;

import java.util.LinkedList;
import java.util.ListIterator;

import org.lwjgl.opengl.GL11;

import rlforj.los.BresLos;
import rlforj.los.ILosAlgorithm;
import rlforj.los.ILosBoard;
import rlforj.los.PrecisePermissive;
import rlforj.math.Point2I;
import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.gen.RecursiveDivisionMethod;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.LinkObject;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.main.support.Vector2D;
import su.msk.dunno.blame.prototypes.AAnimation;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.tiles.Door;
import su.msk.dunno.blame.tiles.Floor;
import su.msk.dunno.blame.tiles.Wall;

public class Field 
{
	public LinkObject[][] objects;	// must be private in the release!!!
	public LinkedList<AAnimation> animations;	// must be private!!!
	private LinkedList<AObject> lightSources;
	private int N_x, N_y;
	
	private RL4JMapView drawView;
	private RL4JMapView lineView;
	
	public boolean isPlayerMoving;
	public Vector2D playerMovingCoord;
	
	public Field(int N_x, int N_y, String mapname)	// constructor for map generation or map loading
	{
		this(N_x, N_y);
		if("random".equals(mapname))
		{
			int[][] map = RecursiveDivisionMethod.generate(this);
			for(int i = 0; i < N_x; i++)
			{
				for(int j = 0; j < N_y; j++)
				{
					switch(map[i][j])
					{
					case 0: objects[i][j].set(0, new Floor(i, j)); break;
					case 1: objects[i][j].set(0, new Wall(i, j)); break;
					case 2: objects[i][j].set(0, new Door(i, j)); break;
					}
				}
			}
		}
	}
	
	public Field(int N_x, int N_y)	// std constructor with only floor as map
	{
		this.N_x = N_x;
		this.N_y = N_y;
		objects = new LinkObject[N_x][N_y];
		for(int i = 0; i < N_x; i++)
		{
			for(int j = 0; j < N_y; j++)
			{
				objects[i][j] = new LinkObject();
				objects[i][j].add(new Floor(i, j));
			}
		}
		lightSources = new LinkedList<AObject>();
		animations = new LinkedList<AAnimation>();
		drawView = new RL4JMapView()
		{
			public boolean isObstacle(int x, int y) 
			{
				return !getTransparency(x, y);
			}

			public void visit(int x, int y) 
			{
				if(!objects[x][y].getLast().preventDraw)
				{
					MyFont.instance().drawChar(objects[x][y].getLast().getSymbol(), x*Blame.scale, y*Blame.scale, Blame.scale*0.01f, objects[x][y].getLast().getColor());
					objects[x][y].getFirst().wasDrawed = true;
				}
			}			
		};
		lineView = new RL4JMapView()
		{
			public boolean isObstacle(int x, int y) 
			{
				return !getTransparency(x, y);
			}

			public void visit(int x, int y){}			
		};
	}
	
	public void addObject(AObject ao)
	{
		objects[ao.cur_pos.x][ao.cur_pos.y].add(ao);
		if(ao.isLightSource())lightSources.add(ao);
	}
	
	public boolean removeObject(AObject ao)
	{
		if(ao.isLightSource())lightSources.remove(ao);
		if(!objects[ao.cur_pos.x][ao.cur_pos.y].contains(ao))return false;
		else
		{
			objects[ao.cur_pos.x][ao.cur_pos.y].remove(ao);
			return true;
		}
	}
	
	public void addAnimation(AAnimation a)
	{
		animations.add(a);
	}
	
	public void setMapTile(Point p, AObject ao)
	{
		objects[p.x][p.y].set(0, ao);
	}
	
	public Vector2D getCoordForPoint(Point player_point, Point p)
	{
		return new Vector2D(220-player_point.x*Blame.scale, 240-player_point.y*Blame.scale).plus
		      (new Vector2D(p.x*Blame.scale, p.y*Blame.scale));
	}
	
	public void draw()
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(20, 
				  		  80, 
				  		  0.0f);
		for(int i = 0; i < N_x; i++)
		{
			for(int j = 0; j < N_y; j++)
			{
				if(!objects[i][j].getLast().preventDraw)
				{
					MyFont.instance().drawChar(objects[i][j].getLast().getSymbol(), i*20, j*20, 0.2f, objects[i][j].getLast().getColor());
				}
			}
		}
		playAnimations();
		GL11.glPopMatrix();
	}
	
	public void draw(Point player_point)
	{
		GL11.glPushMatrix();
		if(!isPlayerMoving)
		{
			GL11.glTranslatef(220-player_point.x*Blame.scale, 
					  		  240-player_point.y*Blame.scale, 
					  		  0.0f);
		}
		else
		{
			GL11.glTranslatef(220-playerMovingCoord.x, 
			  		  		  240-playerMovingCoord.y, 
			  		  		  0.0f);
		}
		for(int i = /*Math.max(0, player_point.x-20*7/Blame.scale)*/0; i < Math.min(player_point.x+20*10/Blame.scale, N_x); i++)
		{
			for(int j = Math.max(0, player_point.y-20*7/Blame.scale); j < /*Math.min(player_point.y+20*7/Blame.scale, N_y)*/N_y; j++)
			{
				if(objects[i][j].getLast().isAlwaysDraw())
				{
					MyFont.instance().drawChar(objects[i][j].getLast().getSymbol(), i*Blame.scale, j*Blame.scale, Blame.scale*0.01f, objects[i][j].getLast().getColor());
				}
				else if(objects[i][j].getFirst().wasDrawed)MyFont.instance().drawChar(objects[i][j].getFirst().getSymbol(), i*Blame.scale, j*Blame.scale, Blame.scale*0.01f, Color.GRAY);
			}
		}
		for(AObject source: lightSources)
		{
			new PrecisePermissive().visitFieldOfView(drawView, source.cur_pos.x, source.cur_pos.y, source.getDov());
		}
		playAnimations();
		GL11.glPopMatrix();
	}
	
	/*public void playAnimation(AAnimation a)
	{
		while(!a.isEnded)
		{
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);		
			GL11.glLoadIdentity();
			draw((Blame.playCibo?Blame.cibo.cur_pos:Blame.killy.cur_pos));
			a.play();
			MyFont.instance().drawString("FPS: "+Blame.fps, 450, 415, 0.2f, Color.WHITE);
			Display.sync(Blame.framerate);
			Display.update();
		}		
	}*/
	
	public void playAnimations()
	{
		for(ListIterator<AAnimation> li = animations.listIterator(); li.hasNext();)
		{
			AAnimation a = li.next();
			a.play();
			if(a.isEnded)li.remove();
		}
		/*if(animations.size() > 0)
		{
			AAnimation a = animations.getFirst();
			a.play();
			if(a.isEnded)animations.remove(a);
		}*/
	}
	
	public boolean changeLocation(ALiving al)	// add checks for edges and passability!
	{
		if(!al.getOld_pos().equals(al.cur_pos))
		{
			if(!al.isDead && getPassability(al.cur_pos))
			{
				objects[al.getOld_pos().x][al.getOld_pos().y].remove(al);
				objects[al.cur_pos.x][al.cur_pos.y].add(al);
				//if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" moves "+getDirection(al.getOld_pos(), al.cur_pos));
			}
			else
			{
				//if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" fails to move "+getDirection(al.getOld_pos(), al.cur_pos));
				al.cur_pos = al.getOld_pos();
				return false;	// if Player fails to go - there is no move (see nextStep@LivingLinkedList)!
			}
		}
		return true;
	}
	
	/*public String getDirection(Point from, Point to)
	{
		if(from.equals(to))return "stays still";
		int x = to.x - from.x;
		int y = to.y - from.y;
		if(y > 0)
		{
			if(x > 0)return "up/right";
			else if(x == 0)return "up";
			else if(x < 0)return "up/left";
		}
		else if(y == 0)
		{
			if(x > 0)return "right";
			else if(x < 0)return "left";
		}
		else if(y < 0)
		{
			if(x > 0)return "down/right";
			else if(x == 0)return "down";
			else if(x < 0)return "down/left";
		}
	}*/
	public int getDirection(Point from, Point to)
	{
		if(from.equals(to))return Move.STAY;
		int x = to.x - from.x;
		int y = to.y - from.y;
		if(y > 0)
		{
			if(x > 0)return Move.UPRIGHT;
			else if(x == 0)return Move.UP;
			else/* if(x < 0)*/return Move.UPLEFT;
		}
		else if(y == 0)
		{
			if(x > 0)return Move.RIGHT;
			else/* if(x < 0)*/return Move.LEFT;
		}
		else/* if(y < 0)*/
		{
			if(x > 0)return Move.DOWNRIGHT;
			else if(x == 0)return Move.DOWN;
			else/* if(x < 0)*/return Move.DOWNLEFT;
		}
	}
	
	public LinkedList<AObject> getNeighbours(AObject player, int dov) // dov = depth of vision
	{
		LinkedList<AObject> neighbours = new LinkedList<AObject>();
		for(int i = Math.max(0, player.cur_pos.x-dov); i <= Math.min(player.cur_pos.x+dov, N_x-1); i++)
		{
			for(int j = Math.max(0, player.cur_pos.y-dov); j <= Math.min(player.cur_pos.y+dov, N_y-1); j++)
			{
				for(AObject next: objects[i][j])
				{
					if(isVisible(player.cur_pos, next.cur_pos, dov) && !next.equals(player))neighbours.add(next);
				}
			}
		}
		return neighbours;
	}
	
	public LinkedList<AObject> getObjectsAtPoint(Point p)
	{
		if(p.x >= 0 && p.x < N_x && p.y >= 0 && p.y < N_y)return objects[p.x][p.y];
		else return new LinkedList<AObject>();
	}
	
	public Point findObject(AObject ao)
	{
		Point p = null;
		for(int i = 0; i < N_x; i++)
		{
			for(int j = 0; j < N_y; j++)
			{
				if(objects[i][j].contains(ao))
				{
					p = new Point(i, j);
					ao.cur_pos = p;
				}
			}
		}
		return p;
	}
	
	public boolean getTransparency(Point p)
	{
		for(AObject ao: objects[p.x][p.y])
		{
			if(!ao.getTransparency())return false;
		}
		return true;
	}
	
	public boolean getTransparency(int i, int j)
	{
		for(AObject ao: objects[i][j])
		{
			if(!ao.getTransparency())return false;
		}
		return true;
	}
	
	public boolean getMapPassability(Point p)
	{
		if(!onArea(p))return false;
		return objects[p.x][p.y].getFirst().getPassability();
	}
	
	public boolean getMapPassability(int i, int j)
	{
		if(!onArea(i, j))return false;
		return objects[i][j].getFirst().getPassability();
	}
	
	public boolean getPassability(Point p)
	{
		if(!onArea(p))return false;
		for(AObject ao: objects[p.x][p.y])
		{
			if(!ao.getPassability())return false;
		}
		return true;
	}
	
	public boolean getPassability(int i, int j)
	{
		if(!onArea(new Point(i, j)))return false;
		for(AObject ao: objects[i][j])
		{
			if(!ao.getPassability())return false;
		}
		return true;
	}
	
	public boolean onArea(Point p)
	{
		return p.x >= 0 && p.x < N_x && p.y >= 0 && p.y < N_y;
	}
	
	public boolean onArea(int i, int j)
	{
		return i >= 0 && i < N_x && j >= 0 && j < N_y;
	}
	
	public Point getRandomPos()
	{
		int i, j;
		i = (int)(Math.random()*N_x);
		j = (int)(Math.random()*N_y);
		while(!this.getPassability(i, j))
		{
			i = (int)(Math.random()*N_x);
			j = (int)(Math.random()*N_y);
		}
		return new Point(i, j);
	}
	
	public Point getRandomPos(int startx, int starty, int endx, int endy)
	{
		int i, j;
		i = startx + (int)(Math.random()*(endx+1 - startx));
		j = starty + (int)(Math.random()*(endy+1 - starty));
		while(!this.getPassability(i, j))
		{
			i = startx + (int)(Math.random()*(endx+1 - startx));
			j = starty + (int)(Math.random()*(endy+1 - starty));
		}
		return new Point(i, j);
	}
	
	public Point getRandomPos(Point leftup, Point rightdown)
	{
		return this.getRandomPos(leftup.x, leftup.y, rightdown.x, rightdown.y);
	}

	public int getN_x() {
		return N_x;
	}

	public int getN_y() {
		return N_y;
	}
	
	public String getTileType(Point p)
	{
		return this.getTileType(p.x, p.y);
	}
	
	public String getTileType(int i, int j)
	{
		return objects[i][j].getFirst().getName();
	}
	
	public boolean isVisible(Point p1, Point p2, int dov)
	{
    	if(p2.getDist2(p1) > dov*dov)return false;
    	if(p2.equals(p1))return true;
    	return new BresLos(false).existsLineOfSight(
    			new RL4JMapView()
    			{
					public boolean isObstacle(int x, int y) 
					{
						return !getTransparency(x, y);
					}

					public void visit(int x, int y){}    				
    			}, 
    			p1.x, p1.y, p2.x, p2.y, false);
	}
	
	public boolean isMapVisible(Point p1, Point p2, int dov)
	{
		//if(p2.getDist2(p1) > dov*dov)return false;
    	if(p2.equals(p1))return true;
    	return new BresLos(false).existsLineOfSight(
    			new RL4JMapView()
    			{
					public boolean isObstacle(int x, int y) 
					{
						return !getTransparency(x, y);
					}

					public void visit(int x, int y){}    				
    			}, 
    			p1.x, p1.y, p2.x, p2.y, false);
	}
	
	public LinkedList<Point> getLine(Point p1, Point p2)
	{
		ILosAlgorithm ila = new BresLos(false);
		ila.existsLineOfSight(lineView, p1.x, p1.y, p2.x, p2.y, true);
		LinkedList<Point> line = new LinkedList<Point>();
		for(Point2I p2i: ila.getProjectPath())
		{
			line.add(new Point(p2i.x, p2i.y));
		}
		return line;
	}
    
    abstract class RL4JMapView implements ILosBoard
    {
		public boolean contains(int x, int y) 
		{
			return x >= 0 && x < N_x && y >= 0 && y < N_y;
		}
    }
}