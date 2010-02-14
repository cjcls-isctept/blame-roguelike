package su.msk.dunno.blame.map.gen;

import su.msk.dunno.blame.main.support.Point;
import su.msk.dunno.blame.map.Field;

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
