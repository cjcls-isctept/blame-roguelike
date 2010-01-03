package net.dunno.blame.field.path_algorithmes.astar;

import net.dunno.blame.field.Field;

public class Closest2Heuristic
implements AStarHeuristic
{
	private static final long serialVersionUID = -87023141015856772L;

	public float getCost(Field field, int x, int y, int tx, int ty) 
	{
		float dx = tx - x;
		float dy = ty - y;
		
		return dx*dx + dy*dy;
	}

}
