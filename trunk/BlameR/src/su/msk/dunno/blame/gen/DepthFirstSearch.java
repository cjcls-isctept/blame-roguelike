package su.msk.dunno.blame.gen;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.main.support.Point;

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
