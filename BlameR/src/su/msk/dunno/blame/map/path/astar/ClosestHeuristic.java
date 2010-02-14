package su.msk.dunno.blame.map.path.astar;

import su.msk.dunno.blame.map.Field;

/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile.
 * 
 * @author Kevin Glass
 */
public class ClosestHeuristic implements AStarHeuristic 
{
	private static final long serialVersionUID = 227464874137277757L;

	/**
	 * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
	 */
	public float getCost(Field field, int x, int y, int tx, int ty) 
	{		
		float dx = tx - x;
		float dy = ty - y;
		
		float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));
		
		return result;
	}

}
