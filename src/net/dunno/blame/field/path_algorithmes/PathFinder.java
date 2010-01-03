package net.dunno.blame.field.path_algorithmes;

import java.io.Serializable;
import java.util.LinkedList;

import net.dunno.blame.main.support.Point;

/**
 * A description of an implementation that can find a path from one 
 * location on a tile map to another based on information provided
 * by that tile map.
 * 
 * @see TileBasedMap
 * @author Kevin Glass
 */
public abstract class PathFinder
implements Serializable
{
	static final long serialVersionUID = 1352403651137066850L;
	
	public static final int SUCCEEDED = 0;
	public static final int STOPPED = 1;
	public static final int FAILED = 2;
	
	public LinkedList<Point> path = new LinkedList<Point>();

	/**
	 * Find a path from the starting location provided (sx,sy) to the target
	 * location (tx,ty) avoiding blockages and attempting to honour costs 
	 * provided by the tile map.
	 * 
	 * @param mover The entity that will be moving along the path. This provides
	 * a place to pass context information about the game entity doing the moving, e.g.
	 * can it fly? can it swim etc.
	 * 
	 * @param sx The x coordinate of the start location
	 * @param sy The y coordinate of the start location
	 * @param tx The x coordinate of the target location
	 * @param ty Teh y coordinate of the target location
	 * @return The path found from start to end, or null if no path can be found.
	 */
	public abstract int findPath(Point p1, Point p2);
}
