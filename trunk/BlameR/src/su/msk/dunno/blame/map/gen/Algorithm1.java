package su.msk.dunno.blame.map.gen;

import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.map.Field;


public class Algorithm1 
{
	public static int[][] generate(Field field)
	{
		int[][] map = new int[field.getN_x()][field.getN_y()];
		makeCoridor(map, field.getRandomPos(), (int)(Math.random()*4), 900);
		return map;
	}
	
	public static void makeCoridor(int[][] map, Point p, int dir, int count)
	{
		int len = (int)(Math.random()*10);
		int i = 0;
		switch(dir)
		{
		case 0:	// up
			
			for(i = 0; i < len; i++)
			{
				if(p.y+i < map[0].length)map[p.x][p.y+i] = 1;
				else break;
			}
			p = new Point(p.x, p.y+(int)(Math.random()*i));
			break;
		case 1:	// down
			for(i = 0; i < len; i++)
			{
				if(p.y-i >= 0)map[p.x][p.y-i] = 1;
				else break;
			}
			p = new Point(p.x, p.y-(int)(Math.random()*i));
			break;
		case 2:	//left
			for(i = 0; i < len; i++)
			{
				if(p.x-i >= 0)map[p.x-i][p.y] = 1;
				else break;
			}
			p = new Point(p.x-(int)(Math.random()*i), p.y);
			break;
		case 3:	// right
			for(i = 0; i < len; i++)
			{
				if(p.x+i < map.length)map[p.x+i][p.y] = 1;
				else break;
			}
			p = new Point(p.x+(int)(Math.random()*i), p.y);
			break;
		}
		count--;
		if(count == 0)return;
		else 
		{
			int new_dir = 0;
			if(dir == 0 || dir == 1)new_dir = 2+(int)(Math.random()*2);
			if(dir == 2 || dir == 3)new_dir = (int)(Math.random()*2);
			makeCoridor(map, p, new_dir, count);
		}
	}
}
