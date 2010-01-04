package su.msk.dunno.blame.path.astar;

import su.msk.dunno.blame.containers.Field;

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
