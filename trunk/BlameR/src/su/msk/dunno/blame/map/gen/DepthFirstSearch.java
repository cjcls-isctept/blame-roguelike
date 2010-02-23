package su.msk.dunno.blame.map.gen;

import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.support.Point;

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
