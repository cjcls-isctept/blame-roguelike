package su.msk.dunno.blame.map.gen;

import java.util.LinkedList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.Blame;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.map.tiles.Door;
import su.msk.dunno.blame.map.tiles.Floor;
import su.msk.dunno.blame.map.tiles.Wall;
import su.msk.dunno.blame.support.Point;

//	0 - floor, 1 - wall, 2 - door, 3 - station
public class RecursiveDivisionMethod 
{
	
	public static int[][] generate(int N_x, int N_y)
	{
		int[][] map = new int[N_x][N_y];
    	for(int i = 0; i < N_x; i++)
        {
        	for(int j = 0; j < N_y; j++)
        	{
        		map[i][j] = GenLib.LevelElementRoom;
        	}
        }		
		
		create4Rooms(map, 1, 1, N_x-2, N_y-2);
		addStations(map, Blame.num_stations);
		createSolidEdges(map);
		return map;
	}
	
	public static void create4Rooms(int[][] map, int startx, int starty, int endx, int endy)
	{
		Point p = getRandomPos(map, startx+1, starty+1, endx-1, endy-1);
		int x = startx;
		int y = p.y;
		while(x <= endx/* && map[x][y] != 1*/)
		{
			map[x][y] = GenLib.LevelElementWall;
			x++;
			//drawField(map, field);
		}
		x = p.x;
		y = starty;
		while(y <= endy/* && map[x][y] != 1*/)
		{
			map[x][y] = GenLib.LevelElementWall;
			y++;
			//drawField(map, field);
		}		
		if((p.x-1 - startx > 1+Math.random()*5 && p.y-1 - starty > 1+Math.random()*5) && p.x-1 >= 0 && p.y-1 >= 0)
			create4Rooms(map, startx, starty, p.x-1, p.y-1);
		if((endx - (p.x+1) > 1+Math.random()*5 && p.y-1 - starty > 1+Math.random()*5) && p.x+1 < map.length && p.y-1 >= 0)
			create4Rooms(map, p.x+1, starty, endx, p.y-1);
		if((p.x-1 - startx > 1+Math.random()*5 && endy - (p.y+1) > 1+Math.random()*5) && p.y+1 < map[0].length && p.x-1 >= 0)
			create4Rooms(map, startx, p.y+1, p.x-1, endy);
		if((endx - (p.x+1) > 1+Math.random()*5 && endy - (p.y+1) > 1+Math.random()*5) && p.x+1 < map.length && p.y+1 < map[0].length)
			create4Rooms(map, p.x+1, p.y+1, endx, endy);

		create3Doors(map, p, startx, starty, endx, endy);
	}
	
	public static void create3Doors(int[][] map, Point p, int startx, int starty, int endx, int endy)
	{
		int x = startx + (int)(Math.random()*(p.x-startx));
		int y = p.y;
		while((x == p.x) || (y-1 >= 0 && map[x][y-1] == GenLib.LevelElementWall) || 
			  (y+1 < map[0].length && map[x][y+1] == GenLib.LevelElementWall))
		{
			x = startx + (int)(Math.random()*(p.x-startx));
		}
		if(Math.random() < 0.3)map[x][y] = GenLib.LevelElementDoorClose;
		else map[x][y] = GenLib.LevelElementRoom;
		
		x = p.x + (int)(Math.random()*(endx+1-p.x));
		y = p.y;
		while((x == p.x) || (y-1 >= 0 && map[x][y-1] == GenLib.LevelElementWall) || 
			  (y+1 < map[0].length && map[x][y+1] == GenLib.LevelElementWall))
		{
			x = p.x + (int)(Math.random()*(endx+1-p.x));
		}
		if(Math.random() < 0.3)map[x][y] = GenLib.LevelElementDoorClose;
		else map[x][y] = GenLib.LevelElementRoom;
				
		x = p.x;
		y = starty + (int)(Math.random()*(endy-starty));
		while((y == p.y) || (x-1 >= 0 && map[x-1][y] == GenLib.LevelElementWall) || 
			  (x+1 < map.length && map[x+1][y] == GenLib.LevelElementWall))
		{
			y = starty + (int)(Math.random()*(endy-starty));
		}
		if(Math.random() < 0.3)map[x][y] = GenLib.LevelElementDoorClose;
		else map[x][y] = GenLib.LevelElementRoom;
	}
	
	public static void createSolidEdges(int[][] map)
	{
		for(int i = 0; i < map.length; i++)
		{
			map[i][0] = GenLib.LevelElementWall;
			map[i][map[0].length-1] = GenLib.LevelElementWall;
		}
		for(int j = 0; j < map[0].length; j++)
		{
			map[0][j] = GenLib.LevelElementWall;
			map[map.length-1][j] = GenLib.LevelElementWall;
		}
	}
	
	public static void addStations(int[][] map, int num)
	{
		int out_r = Math.max(9, Math.min(map.length, map[0].length)/num);
		LinkedList<Point> station_points = new LinkedList<Point>();
		for(int i = 0; i < num; i++)
		{
			Point p = addStation(map, out_r, station_points);
			if(p != null)station_points.add(p);
		}
	}
	
    public static Point addStation(int[][] map, int out_r, LinkedList<Point> prev_points)
    {    	
    	int N_x = map.length;
    	int N_y = map[0].length;
    	float even = 0;
    	
    	Point p = new Point((int)(Math.random()*(N_x-2))+1,
    						(int)(Math.random()*(N_y-2))+1);
    	int count = 10;
    	boolean isChosen = false;
    	while(!isChosen)
    	{
    		count--;
    		if(count < 0)return null;
    		p = new Point((int)(Math.random()*(N_x-2))+1,
						  (int)(Math.random()*(N_y-2))+1);
    		isChosen = true;
    		if(p.x == 4 || p.x == N_x-5 || p.y == 4 || p.y == N_y-5)
    		{
    			isChosen = false;
    		}
    		if(isChosen)
    		{
    			for(Point prev: prev_points)
        		{
        			if(p.getDist2(prev) < out_r*out_r)
        			{
        				isChosen = false;
        				break;
        			}
        		}
    		}
    	}
    	
        int min_x = p.x-4;
        int max_x = p.x+4;
        int min_y = p.y-4;
        int max_y = p.y+4;
        for(int i = min_x; i <= max_x; i++)
        {
        	for(int j = min_y; j <= max_y; j++)
        	{
        		if(i >= 0 && i < N_x && j >= 0 && j < N_y && 
        		   (map[i][j] == GenLib.LevelElementWall || map[i][j] == GenLib.LevelElementDoorClose))
        			map[i][j] = GenLib.LevelElementRoom;
        	}
        }
        for(int i = min_x+1; i <= max_x-1; i++)
        {
        	even++;
        	if(i >= 0 && i < N_x)
        	{
        		if(even == 4 && min_y >= 0)map[i][min_y+1] = GenLib.LevelElementDoorClose;
    	        else if(min_y+1 >= 0)map[i][min_y+1] = GenLib.LevelElementWall;
        		if(even == 4 && max_y < N_y)map[i][max_y-1] = GenLib.LevelElementDoorClose;
        		else if(max_y-1 < N_y)map[i][max_y-1] = GenLib.LevelElementWall;
        	}
        }
        for(int j = min_y+2; j <= max_y-2; j++)
        {
        	even++;
        	if(j >= 0 && j < N_y)
        	{
        		if(even == 10 && min_x >= 0)map[min_x+1][j] = GenLib.LevelElementDoorClose;
        		else if(min_x+1 >= 0)map[min_x+1][j] = GenLib.LevelElementWall;
        		if(even == 10 && max_x < N_x)map[max_x-1][j] = GenLib.LevelElementDoorClose;
            	else if(max_x-1 < N_x)map[max_x-1][j] = GenLib.LevelElementWall;
        	}
        }
        map[p.x][p.y] = GenLib.LevelElementStation;
        return p;
    }
    
	public static Point getRandomPos(int[][]map, int startx, int starty, int endx, int endy)
	{
		int i, j;
		int count = 1000;
		i = startx + (int)(Math.random()*(endx+1 - startx));
		j = starty + (int)(Math.random()*(endy+1 - starty));
		while(map[i][j] != GenLib.LevelElementRoom)
		{
			if(count < 0)return null;
			i = startx + (int)(Math.random()*(endx+1 - startx));
			j = starty + (int)(Math.random()*(endy+1 - starty));
			count--;
		}
		return new Point(i, j);
	}
}
