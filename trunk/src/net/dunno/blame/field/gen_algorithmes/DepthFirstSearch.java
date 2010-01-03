package net.dunno.blame.field.gen_algorithmes;

import net.dunno.blame.field.Field;
import net.dunno.blame.main.support.Point;

public class DepthFirstSearch 
{
	public static int[][] generate(Field field)
	{
		int[][] map = new int[field.getN_x()][field.getN_y()];
		
		return map;
	}
	
	public void dps(int[][] map, Point p)
	{
		map[p.x][p.y] = 1;				
	}
}
