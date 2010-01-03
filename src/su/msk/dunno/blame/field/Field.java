package su.msk.dunno.blame.field;

import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.field.gen_algorithmes.RecursiveDivisionMethod;
import su.msk.dunno.blame.field.tiles.Door;
import su.msk.dunno.blame.field.tiles.Floor;
import su.msk.dunno.blame.field.tiles.Wall;
import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.main.support.Color;
import su.msk.dunno.blame.main.support.LinkObject;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.prototypes.ALiving;
import su.msk.dunno.blame.prototypes.AObject;

public class Field 
{
	public LinkObject[][] objects;	// must be private in the release!!!
	private LinkedList<AObject> lightSources;
	private int N_x, N_y;
	
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
	
	public void setMapTile(Point p, AObject ao)
	{
		objects[p.x][p.y].set(0, ao);
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
				MyFont.instance().drawChar(objects[i][j].getLast().getSymbol(), i*20, j*20, 0.2f, objects[i][j].getLast().getColor());
			}
		}
		GL11.glPopMatrix();
	}
	
	public void draw(ALiving player)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(220-player.cur_pos.x*Blame.scale, 
				  		  240-player.cur_pos.y*Blame.scale, 
				  		  0.0f);
		for(int i = 0; i < N_x; i++)
		{
			for(int j = 0; j < N_y; j++)
			{
				if(objects[i][j].getLast().isAlwaysDraw())
				{
					MyFont.instance().drawChar(objects[i][j].getLast().getSymbol(), i*Blame.scale, j*Blame.scale, Blame.scale*0.01f, objects[i][j].getLast().getColor());
				}
				if(isLocationEnlighted(player, i, j))
				{
					MyFont.instance().drawChar(objects[i][j].getLast().getSymbol(), i*Blame.scale, j*Blame.scale, Blame.scale*0.01f, objects[i][j].getLast().getColor());
					objects[i][j].getFirst().wasDrawed = true;
				}
				else
				{
					if(objects[i][j].getFirst().wasDrawed)MyFont.instance().drawChar(objects[i][j].getFirst().getSymbol(), i*Blame.scale, j*Blame.scale, Blame.scale*0.01f, Color.GRAY);
				}				
			}
		}
		GL11.glPopMatrix();
	}
	
	public boolean isLocationEnlighted(ALiving player, int i, int j)
	{
		for(AObject source: lightSources)
		{
			if(/*isVisible(player.cur_pos, source.cur_pos, player.getDov()) && */isVisible(source.cur_pos, new Point(i, j), source.getDov()))
			{
				return true;
			}
		}
		return false;
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
	
	public String getDirection(Point from, Point to)
	{
		if(from.equals(to))return "stays still";
		int x = to.x - from.x;
		int y = to.y - from.y;
		if(y > 0)
		{
			if(x > 0)return "up/right";
			else if(x == 0)return "up";
			else/* if(x < 0)*/return "up/left";
		}
		else if(y == 0)
		{
			if(x > 0)return "right";
			else/* if(x < 0)*/return "left";
		}
		else/* if(y < 0)*/
		{
			if(x > 0)return "down/right";
			else if(x == 0)return "down";
			else/* if(x < 0)*/return "down/left";
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
		return !isIntersectionBresenham(p1.x, p1.y, p2.x, p2.y, false, false);
	}
	
	public boolean isMapVisible(Point p1, Point p2, int dov)
	{
		//if(p2.getDist2(p1) > dov*dov)return false;
    	if(p2.equals(p1))return true;
		return !isIntersectionBresenham(p1.x, p1.y, p2.x, p2.y, false, true);
	}
	
	public LinkedList<Point> getLine(Point p1, Point p2)
	{
		LinkedList<Point> line = new LinkedList<Point>();
		int delta_x = Math.abs(p2.x - p1.x) << 1;
	    int delta_y = Math.abs(p2.y - p1.y) << 1;
	    int x = p1.x; int y = p1.y;

	    int ix = p2.x > p1.x?1:-1;
	    int iy = p2.y > p1.y?1:-1;
	    
	    if (delta_x >= delta_y)
	    {
	    	int error = delta_y - (delta_x >> 1);
	    	while (x != p2.x)
	        {
	    		line.add(new Point(x, y));
	        	if (error >= 0)
		        {
	        		if (error != 0 || ix > 0)
		            {
	        			y += iy;
		                error -= delta_x;
		            }
		        }
		        x += ix;
		        error += delta_y;
	        }
	    }
	    else
	    {
	    	 int error = delta_x - (delta_y >> 1);
		     while (y != p2.y)
		     {
		    	 line.add(new Point(x, y));
		    	 if (error >= 0)
		         {
		    		 if (error != 0 || iy > 0)
		             {
		    			 x += ix;
		                 error -= delta_y;
		             }
		         }
		         y += iy;
		         error += delta_x;
		     }
	    }
	    line.add(p2);
		return line;
	}
	
    public boolean isIntersectionBresenham(int x1, int y1, int x2, int y2, boolean isPassabilityCheck, boolean isMapCheck)
	{
    	int delta_x = Math.abs(x2 - x1) << 1;
	    int delta_y = Math.abs(y2 - y1) << 1;
	    int x = x1; int y = y1;

	    int ix = x2 > x1?1:-1;
	    int iy = y2 > y1?1:-1;

	    if (delta_x >= delta_y)
	    {
	        // error may go below zero
	        int error = delta_y - (delta_x >> 1);
            if (error >= 0)
            {
            	if (error != 0 || ix > 0)
                {
                    y += iy;
                    error -= delta_x;
                }
            }
            x += ix;
            error += delta_y;
	        while (x != x2)
	        {
	    	    if(isPassabilityCheck)
	    	    {
	    		    if(x >= 0 && x < N_x && y >= 0 && y < N_y)
	    		    {
	    		    	if(isMapCheck)if(!getMapPassability(x, y))return true;
	    		    	else if(!getPassability(x, y))return true;
	    		    }
	    	    }
	    	    else
	    	    {
	    		    if(x >= 0 && x < N_x &&
	    		       y >= 0 && y < N_y &&
	    		 	   !getTransparency(x, y))return true;	    	
	    	    }
	            if (error >= 0)
	            {
	            	if (error != 0 || ix > 0)
	                {
	                    y += iy;
	                    error -= delta_x;
	                }
	            }
	            x += ix;
	            error += delta_y;
	        }
	    }
	    else
	    {
	        int error = delta_x - (delta_y >> 1);
            if (error >= 0)
            {
            	if (error != 0 || iy > 0)
                {
                    x += ix;
                    error -= delta_y;
                }
            }
            y += iy;
            error += delta_x;
	        while (y != y2)
	        {
	    	    if(isPassabilityCheck)
	    	    {
	    		    if(x >= 0 && x < N_x && y >= 0 && y < N_y)
	    		    {
	    		    	if(isMapCheck)if(!getMapPassability(x, y))return true;
	    		    	else if(!getPassability(x, y))return true;
	    		    }
	    	    }
	    	    else
	    	    {
	    		    if(x >= 0 && x < N_x &&
	    		       y >= 0 && y < N_y &&
	    		 	   !getTransparency(x, y))return true;	    	
	    	    }
	            if (error >= 0)
	            {
	            	if (error != 0 || iy > 0)
	                {
	                    x += ix;
	                    error -= delta_y;
	                }
	            }
	            y += iy;
	            error += delta_x;
	        }
	    }
		return false;
	}
}
