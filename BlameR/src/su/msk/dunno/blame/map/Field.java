package su.msk.dunno.blame.map;

import java.util.LinkedList;
import java.util.ListIterator;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import rlforj.los.BresLos;
import rlforj.los.ILosAlgorithm;
import rlforj.los.ILosBoard;
import rlforj.los.PrecisePermissive;
import rlforj.math.Point2I;
import su.msk.dunno.blame.decisions.Move;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.gen.GenLib;
import su.msk.dunno.blame.map.tiles.Door;
import su.msk.dunno.blame.map.tiles.Floor;
import su.msk.dunno.blame.map.tiles.Wall;
import su.msk.dunno.blame.objects.Livings;
import su.msk.dunno.blame.objects.buildings.RebuildStation;
import su.msk.dunno.blame.prototypes.AAnimation;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.LinkObject;
import su.msk.dunno.blame.support.Messages;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.Vector2D;

public class Field 
{
	private LinkObject[][] objects;	// must be private in the release!!!
	public LinkedList<AAnimation> animations;	// must be private!!!
	private LinkedList<AObject> lightSources;
	private int N_x, N_y;
	
	private RL4JMapView drawView;
	private RL4JMapView lineView;
	private PrecisePermissive pp = new PrecisePermissive();
	
	public Vector2D playerMovingCoord;
	
	private int center_x = (Blame.width-195)/2;		// variables for draw methods 
	private int center_y = (Blame.height-75)/2+75;
	private int nx = center_x/15;
	private int ny = center_y/20;
	
	public Field(int N_x, int N_y, String mapname)	// constructor for map generation or map loading
	{
		this(N_x, N_y);
		if("random".equals(mapname))
		{
			int[][] map = new int[N_x][N_y];
			if("rdm".equals(Blame.dungeonType))map = GenLib.cretaRDM(N_x, N_y, Blame.num_stations);
			else if("antnest".equals(Blame.dungeonType))map = GenLib.CreateAntNest(N_x, N_y);
			else if("caves".equals(Blame.dungeonType))map = GenLib.CreateCaves(N_x, N_y);
			else if("mines".equals(Blame.dungeonType))map = GenLib.CreateMines(N_x, N_y);
			else if("maze".equals(Blame.dungeonType))map = GenLib.CreateMaze(N_x, N_y);
			else if("standard".equals(Blame.dungeonType))map = GenLib.CreateStandardDunegon(N_x, N_y);
			for(int i = 0; i < N_x; i++)
			{
				for(int j = 0; j < N_y; j++)
				{
					switch(map[i][j])
					{
					case '#': objects[i][j].set(0, new Wall(i, j)); break;
					case '.': objects[i][j].set(0, new Floor(i, j)); break;
					case ',': objects[i][j].set(0, new Floor(i, j)); break;
					case '+': objects[i][j].set(0, new Door(i, j)); break;
					case '/': objects[i][j].set(0, new Door(i, j)); break;
					case 'A':
						objects[i][j].set(0, new Floor(i, j));
						RebuildStation rs = new RebuildStation(i, j, this);
						Livings.instance().addObject(rs);
						break;
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
				Point player_point = Blame.getCurrentPlayer().curPos;
				if(!objects[x][y].getLast().isDrawPrevented() &&
				   player_point.x - x < center_x/(Blame.scale*3/4) &&
				   x - player_point.x < center_x/(Blame.scale*3/4) &&
				   player_point.y - y < center_y/(Blame.scale)-4 &&
				   y - player_point.y < center_y/(Blame.scale)-4)
				{
					MyFont.instance().drawDisplayList(objects[x][y].getLast().getSymbol(), 
											   		  x*100*3/4, 
											   		  y*100, 
											   		  objects[x][y].getLast().getColor());
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
		objects[ao.curPos.x][ao.curPos.y].add(ao);
		if(ao.isLightSource())lightSources.add(ao);
	}
	
	public void addObject(Point p, AObject ao)
	{
		ao.curPos = p;
		objects[p.x][p.y].add(objects[p.x][p.y].size()-1, ao);
		if(ao.isLightSource())lightSources.add(ao);
	}
	
	public boolean removeObject(AObject ao)
	{
		if(ao.isLightSource())lightSources.remove(ao);
		if(!objects[ao.curPos.x][ao.curPos.y].contains(ao))return false;
		else
		{
			objects[ao.curPos.x][ao.curPos.y].remove(ao);
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
	
	public void draw(int scale)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(20, 
				  		  80, 
				  		  0.0f);
		GL11.glScalef(scale*0.01f, scale*0.01f, 1.0f);
		for(int i = 0; i < N_x; i++)
		{
			for(int j = 0; j < N_y; j++)
			{
				/*if(objects[i][j].getLast().wasDrawed)
				{
					*/MyFont.instance().drawDisplayList(objects[i][j].getLast().getSymbol(), 
											     		i*100*3/4, 
											     		j*100, 
											     		objects[i][j].getLast().getColor());
				//}
			}
		}
		playAnimations();
		GL11.glPopMatrix();
	}
	
	public void draw(Point player_point)
	{
		GL11.glPushMatrix();
		if(!Blame.getCurrentPlayer().isDrawPrevented())playerMovingCoord = new Vector2D(player_point.x*(Blame.scale*3/4), 
															 							player_point.y*Blame.scale);
		GL11.glTranslatef(center_x-playerMovingCoord.x, 
			  		  	  center_y-playerMovingCoord.y, 
			  		  	  0.0f);
		GL11.glScalef(Blame.scale*0.01f, Blame.scale*0.01f, 1.0f);
		for(int i = Math.max(0, player_point.x-20*(nx-1)/Blame.scale); 
				i < Math.min(player_point.x+20*nx/Blame.scale, N_x); i++)
		{
			for(int j = Math.max(0, player_point.y-20*(ny-5)/Blame.scale); 
					j < Math.min(player_point.y+20*(ny-4)/Blame.scale, N_y); j++)
			{
				if(objects[i][j].getLast().isAlwaysDraw())
				{
					MyFont.instance().drawDisplayList(objects[i][j].getLast().getSymbol(), 
													  i*100*3/4, 
													  j*100, 
													  objects[i][j].getLast().getColor());
				}
				else if(objects[i][j].getFirst().wasDrawed && !objects[i][j].getFirst().isDrawPrevented())
				{
					MyFont.instance().drawDisplayList(objects[i][j].getFirst().getSymbol(), 
						 							  i*100*3/4, 
						 							  j*100, 
						 							  Color.GRAY);
				}
			}
		}
		for(AObject source: lightSources)
		{
			pp.visitFieldOfView(drawView, source.curPos.x, source.curPos.y, source.getDov());
		}
		playAnimations();
		/*MyFont.instance().drawString(""+Blame.scale,     
				 					 Blame.width-190, 100, 0.2f, Color.WHITE);
		MyFont.instance().drawString(""+(center_x-playerMovingCoord.x),     
									 Blame.width-190, 80, 0.2f, Color.WHITE);*/	
		GL11.glPopMatrix();
	}
	
	public void playAnimation(AAnimation a)
	{
		addAnimation(a);
		while(!a.isEnded)
		{
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);		
			GL11.glLoadIdentity();			
			draw(Blame.getCurrentPlayer().curPos);		
			Blame.getCurrentPlayer().drawStats();
			Messages.instance().showMessages();
			Display.sync(Blame.framerate);
			Display.update();
			Blame.getFPS();
		}
	}
	
	private void playAnimations()
	{
		if(animations.size() > 0)
		{
			int cur_time = animations.getFirst().cur_time;
			for(ListIterator<AAnimation> li = animations.listIterator(); li.hasNext();)
			{
				AAnimation a = li.next();
				if(a.cur_time == cur_time)a.play();
				//else a.nextFrame();
				if(a.isEnded)li.remove();
			}
		}
	}
	
	public void drawLine(LinkedList<AObject> selectLine)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(center_x-playerMovingCoord.x, 
	  		  	  		  center_y-playerMovingCoord.y, 
	  		  	  		  0.0f);
		GL11.glScalef(Blame.scale*0.01f, Blame.scale*0.01f, 1.0f);
		for(AObject ao: selectLine)
		{
			MyFont.instance().drawDisplayList(ao.getSymbol(), ao.curPos.x*100*3/4, ao.curPos.y*100, ao.getColor());
		}
		GL11.glPopMatrix();
	}
	
	public boolean changeLocation(ALiving al)	// add checks for edges and passability!
	{
		if(!al.getOldPos().equals(al.curPos))
		{
			if(!al.isDead && getPassability(al.curPos))
			{
				objects[al.getOldPos().x][al.getOldPos().y].remove(al);
				objects[al.curPos.x][al.curPos.y].add(al);
				//if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" moves "+getDirection(al.getOld_pos(), al.cur_pos));
			}
			else
			{
				//if(al.isNearPlayer())Messages.instance().addMessage(al.getName()+" fails to move "+getDirection(al.getOld_pos(), al.cur_pos));
				al.curPos = al.getOldPos();
				return false;	// if Player fails to go - there is no move (see nextStep@LivingLinkedList)!
			}
		}
		return true;
	}

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
	
	public Point getNearestFree(Point p, int radius)
	{
		for(int k = 1; k <= radius; k++)
		{
			for(int i = p.x-k; i <= p.x+k; i++)
			{
				for(int j = p.y-k; j <= p.y+k; j++)
				{
					if(getPassability(i, j))return new Point(i,j);
				}
			}
		}
		return null;
	}
	
	public LinkedList<AObject> getNeighbours(AObject player, int dov) // dov = depth of vision
	{
		LinkedList<AObject> neighbours = new LinkedList<AObject>();
		for(int i = Math.max(0, player.curPos.x-dov); i <= Math.min(player.curPos.x+dov, N_x-1); i++)
		{
			for(int j = Math.max(0, player.curPos.y-dov); j <= Math.min(player.curPos.y+dov, N_y-1); j++)
			{
				for(AObject next: objects[i][j])
				{
					if(isVisible(player.curPos, next.curPos, dov) && !next.equals(player))neighbours.add(next);
				}
			}
		}
		return neighbours;
	}
	
	public LinkObject getObjectsAtPoint(Point p)
	{
		if(p.x >= 0 && p.x < N_x && p.y >= 0 && p.y < N_y)return objects[p.x][p.y];
		else return new LinkObject();
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
					ao.curPos = p;
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
	
	public Point getRandomCorner()
	{
		int corner = (int)(Math.random()*4);
		switch(corner)
		{
		case 0: return getRandomPos(1,1,3,3);
		case 1: return getRandomPos(N_x-4,3,N_x-2,1);
		case 2: return getRandomPos(1,N_y-2,3,N_y-4);
		case 3: return getRandomPos(N_x-4,N_y-4,N_x-2,N_y-2);
		default: return getRandomPos();		// if all corners unavailable return random position
		}
	}
	
	public Point getRandomPos()
	{
		return getRandomPos(0, 0, N_x-1, N_y-1);
	}
	
	public Point getRandomPos(int startx, int starty, int endx, int endy)
	{
		int i, j;
		int count = 1000;
		i = startx + (int)(Math.random()*(endx+1 - startx));
		j = starty + (int)(Math.random()*(endy+1 - starty));
		while(!this.getPassability(i, j))
		{
			if(count < 0)return null;
			i = startx + (int)(Math.random()*(endx+1 - startx));
			j = starty + (int)(Math.random()*(endy+1 - starty));
			count--;
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
	
	public boolean isMapPointVisible(Point p1, Point p2, int dov)
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