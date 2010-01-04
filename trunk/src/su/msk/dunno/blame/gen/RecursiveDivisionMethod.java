package su.msk.dunno.blame.gen;


import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.tiles.Door;
import su.msk.dunno.blame.tiles.Floor;
import su.msk.dunno.blame.tiles.Wall;

//	0 - floor, 1 - wall, 2 - door
public class RecursiveDivisionMethod 
{
	public static int[][] generate(Field field)
	{
		int[][] map = new int[field.getN_x()][field.getN_y()];
		
		create4Rooms(map, field, 0, 0, field.getN_x()-1, field.getN_y()-1);
		createSolidEdges(map);
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
		if((p.x-1 - startx > Math.random()*20 && p.y-1 - starty > Math.random()*20) && p.x-1 >= 0 && p.y-1 >= 0)create4Rooms(map, field, startx, starty, p.x-1, p.y-1);
		if((endx - (p.x+1) > Math.random()*20 && p.y-1 - starty > Math.random()*20) && p.x+1 < field.getN_x() && p.y-1 >= 0)create4Rooms(map, field, p.x+1, starty, endx, p.y-1);
		if((p.x-1 - startx > Math.random()*20 && endy - (p.y+1) > Math.random()*20) && p.y+1 < field.getN_y() && p.x-1 >= 0)create4Rooms(map, field, startx, p.y+1, p.x-1, endy);
		if((endx - (p.x+1) > Math.random()*20 && endy - (p.y+1) > Math.random()*20) && p.x+1 < field.getN_x() && p.y+1 < field.getN_y())create4Rooms(map, field, p.x+1, p.y+1, endx, endy);

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
		field.draw();
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
}
