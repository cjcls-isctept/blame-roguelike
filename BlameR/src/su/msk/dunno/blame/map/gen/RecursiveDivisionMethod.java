package su.msk.dunno.blame.map.gen;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.map.tiles.Door;
import su.msk.dunno.blame.map.tiles.Floor;
import su.msk.dunno.blame.map.tiles.Wall;

//	0 - floor, 1 - wall, 2 - door, 3 - station
public class RecursiveDivisionMethod 
{
	public static int[][] generate(Field field)
	{
		int[][] map = new int[field.getN_x()][field.getN_y()];
		
		create4Rooms(map, field, 1, 1, field.getN_x()-2, field.getN_y()-2);
		createSolidEdges(map);
		for(int i = 0; i < 3; i++)addStation(map);
		return map;
	}
	
	public static void create4Rooms(int[][] map, Field field, int startx, int starty, int endx, int endy)
	{
		Point p = field.getRandomPos(startx+1, starty+1, endx-1, endy-1);
		int x = startx;
		int y = p.y;
		while(x <= endx/* && map[x][y] != 1*/)
		{
			map[x][y] = 1;
			x++;
			//drawField(map, field);
		}
		x = p.x;
		y = starty;
		while(y <= endy/* && map[x][y] != 1*/)
		{
			map[x][y] = 1;
			y++;
			//drawField(map, field);
		}		
		if((p.x-1 - startx > 1+Math.random()*5 && p.y-1 - starty > 1+Math.random()*5) && p.x-1 >= 0 && p.y-1 >= 0)create4Rooms(map, field, startx, starty, p.x-1, p.y-1);
		if((endx - (p.x+1) > 1+Math.random()*5 && p.y-1 - starty > 1+Math.random()*5) && p.x+1 < field.getN_x() && p.y-1 >= 0)create4Rooms(map, field, p.x+1, starty, endx, p.y-1);
		if((p.x-1 - startx > 1+Math.random()*5 && endy - (p.y+1) > 1+Math.random()*5) && p.y+1 < field.getN_y() && p.x-1 >= 0)create4Rooms(map, field, startx, p.y+1, p.x-1, endy);
		if((endx - (p.x+1) > 1+Math.random()*5 && endy - (p.y+1) > 1+Math.random()*5) && p.x+1 < field.getN_x() && p.y+1 < field.getN_y())create4Rooms(map, field, p.x+1, p.y+1, endx, endy);

		create3Doors(map, p, startx, starty, endx, endy);
	}
	
	public static void drawField(int[][] map, Field field)
	{
		for(int i = 0; i < field.getN_x(); i++)
		{
			for(int j = 0; j < field.getN_y(); j++)
			{
				switch(map[i][j])
				{
				case 0: field.objects[i][j].set(0, new Floor(i, j)); break;
				case 1: field.objects[i][j].set(0, new Wall(i, j)); break;
				case 2: field.objects[i][j].set(0, new Door(i, j)); break;
				}
			}
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
		GL11.glLoadIdentity();
		field.draw(20);
		Display.sync(60);
		Display.update();
	}
	
	public static void create3Doors(int[][] map, Point p, int startx, int starty, int endx, int endy)
	{
		int x = startx + (int)(Math.random()*(p.x-startx));
		int y = p.y;
		while((x == p.x) || (y-1 >= 0 && map[x][y-1] == 1) || (y+1 < map[0].length && map[x][y+1] == 1))
		{
			x = startx + (int)(Math.random()*(p.x-startx));
		}
		if(Math.random() < 0.3)map[x][y] = 2;
		else map[x][y] = 0;
		
		x = p.x + (int)(Math.random()*(endx+1-p.x));
		y = p.y;
		while((x == p.x) || (y-1 >= 0 && map[x][y-1] == 1) || (y+1 < map[0].length && map[x][y+1] == 1))
		{
			x = p.x + (int)(Math.random()*(endx+1-p.x));
		}
		if(Math.random() < 0.3)map[x][y] = 2;
		else map[x][y] = 0;
				
		x = p.x;
		y = starty + (int)(Math.random()*(endy-starty));
		while((y == p.y) || (x-1 >= 0 && map[x-1][y] == 1) || (x+1 < map.length && map[x+1][y] == 1))
		{
			y = starty + (int)(Math.random()*(endy-starty));
		}
		if(Math.random() < 0.3)map[x][y] = 2;
		else map[x][y] = 0;
	}
	
	public static void createSolidEdges(int[][] map)
	{
		for(int i = 0; i < map.length; i++)
		{
			map[i][0] = 1;
			map[i][map[0].length-1] = 1;
		}
		for(int j = 0; j < map[0].length; j++)
		{
			map[0][j] = 1;
			map[map.length-1][j] = 1;
		}
	}
	
    public static void addStation(int[][] map)
    {
    	int N_x = map.length;
    	int N_y = map[0].length;
    	Point p = new Point((int)(Math.random()*N_x-1)+1, (int)(Math.random()*N_y-1)+1);
    	float even = 0;
        int min_x = p.x-4;
        int max_x = p.x+4;
        int min_y = p.y-4;
        int max_y = p.y+4;
        for(int i = min_x; i <= max_x; i++)
        {
        	for(int j = min_y; j <= max_y; j++)
        	{
        		if(i >= 0 && i < N_x && j >= 0 && j < N_y && (map[i][j] == 1 || map[i][j] == 2))map[i][j] = 0;
        	}
        }
        for(int i = min_x+1; i <= max_x-1; i++)
        {
        	even++;
        	if(i >= 0 && i < N_x)
        	{
        		if(even == 3 && min_y >= 0)map[i][min_y+1] = 2;
    	        else if(min_y+1 >= 0)map[i][min_y+1] = 1;
        		if(even == 3 && max_y < N_y)map[i][max_y-1] = 2;
        		else if(max_y-1 < N_y)map[i][max_y-1] = 1;
        	}
        }
        for(int j = min_y+2; j <= max_y-2; j++)
        {
        	even++;
        	if(j >= 0 && j < N_y)
        	{
        		if(even == 7 && min_x >= 0)map[min_x+1][j] = 2;
        		else if(min_x+1 >= 0)map[min_x+1][j] = 1;
        		if(even == 7 && max_x < N_x)map[max_x-1][j] = 2;
            	else if(max_x-1 < N_x)map[max_x-1][j] = 1;
        	}
        }
        map[p.x][p.y] = 3;
    }
}
