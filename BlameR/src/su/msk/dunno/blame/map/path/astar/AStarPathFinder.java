package su.msk.dunno.blame.map.path.astar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.map.path.PathFinder;
import su.msk.dunno.blame.support.Point;


/**
 * A path finder implementation that uses the AStar heuristic based algorithm
 * to determine a path. 
 * 
 * @author Kevin Glass
 */
public class AStarPathFinder extends PathFinder 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5170748405221083340L;
	/** The set of nodes that have been searched through */
	private ArrayList<Node> closed = new ArrayList<Node>();
	/** The set of nodes that we do not yet consider fully searched */
	private SortedList open = new SortedList();
	
	/** The map being searched */
	private Field field;
	/** The maximum depth of search we're willing to accept before giving up */
	private int maxSearchDistance;
	
	/** The complete set of nodes across the map */
	private Node[][] nodes;
	/** The heuristic we're applying to determine which nodes to search first */
	private AStarHeuristic heuristic;
	
	/** Indicator if a given tile has been visited during the search */
	//private boolean[][] visited;
	
	/**
	 * Create a path finder with the default heuristic - closest to target.
	 * 
	 * @param map The map to be searched
	 * @param maxSearchDistance The maximum depth we'll search before giving up
	 * @param allowDiagMovement True if the search should try diaganol movement
	 */
	public AStarPathFinder(Field field) 
	{
		this(field, new ClosestHeuristic());
	}

	/**
	 * Create a path finder 
	 * 
	 * @param heuristic The heuristic used to determine the search order of the map
	 * @param map The map to be searched
	 * @param maxSearchDistance The maximum depth we'll search before giving up
	 * @param allowDiagMovement True if the search should try diaganol movement
	 */
	public AStarPathFinder(Field field, AStarHeuristic heuristic) 
	{
		this.heuristic = heuristic;
		this.field = field;
		this.maxSearchDistance = 500;
		
		//visited = new boolean[map.tiles_onArea.length][map.tiles_onArea[0].length];
		
		nodes = new Node[field.getN_x()][field.getN_y()];
		for (int x = 0; x < nodes.length; x++) 
		{
			for (int y = 0; y < nodes[0].length; y++) 
			{
				nodes[x][y] = new Node(x,y);
			}
		}
	}
	
	public int findPath(Point p1, Point p2)
	{
		return this.findPath(p1.x, p1.y, p2.x, p2.y);
	}
	
	/**
	 * @see PathFinder#findPath(Mover, int, int, int, int)
	 */
	public int findPath(int sx, int sy, int tx, int ty) 
	{
		// easy first check, if the destination is blocked, we can't get there
		if (!field.getPassability(tx, ty)) 
		{
			return FAILED;
		}
		
		// initial state for A*. The closed group is empty. Only the starting
		// tile is in the open list and it's cost is zero, i.e. we're already there
		path.clear();
		
		nodes[sx][sy].cost = 0;
		nodes[sx][sy].depth = 0;
		closed.clear();
		open.clear();
		open.add(nodes[sx][sy]);
		
		nodes[tx][ty].parent = null;
		
		// while we haven't found the goal and haven't exceeded our max search depth
		int maxDepth = 0;
		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) 
		{
			// pull out the first node in our open list, this is determined to 
			// be the most likely to be the next step based on our heuristic
			Node current = getFirstInOpen();
			if (current == nodes[tx][ty]) {
				break;
			}
			
			removeFromOpen(current);
			addToClosed(current);
			
			// search through all the neighbours of the current node evaluating
			// them as next steps
			for (int x = -1; x < 2; x++) 
			{
				for (int y = -1; y < 2; y++) 
				{
					// not a neighbour, its the current tile
					if ((x == 0) && (y == 0))
					{
						continue;
					}
					
					// if we're not allowing diaganol movement then only 
					// one of x or y can be set
					if ((x != 0) && (y != 0)) 
					{
						continue;
					}
					
					// determine the location of the neighbour and evaluate it
					int xp = x + current.x;
					int yp = y + current.y;
					
					if (isValidLocation(sx, sy, xp, yp)) 
					{
						// the cost to get to this node is cost the current plus the movement
						// cost to reach this node. Note that the heursitic value is only used
						// in the sorted open list
						float nextStepCost = current.cost + getMovementCost(current.x, current.y);
						Node neighbour = nodes[xp][yp];
						//visited[xp][yp] = true;
						
						// if the new cost we've determined for this node is lower than 
						// it has been previously makes sure the node hasn't been discarded. We've
						// determined that there might have been a better path to get to
						// this node so it needs to be re-evaluated
						if (nextStepCost < neighbour.cost) 
						{
							if (inOpenList(neighbour)) 
							{
								removeFromOpen(neighbour);
							}
							if (inClosedList(neighbour)) 
							{
								removeFromClosed(neighbour);
							}
						}
						
						// if the node hasn't already been processed and discarded then
						// reset it's cost to our current cost and add it as a next possible
						// step (i.e. to the open list)
						if (!inOpenList(neighbour) && !(inClosedList(neighbour))) 
						{
							neighbour.cost = nextStepCost;
							neighbour.heuristic = getHeuristicCost(xp, yp, tx, ty);
							maxDepth = Math.max(maxDepth, neighbour.setParent(current));
							addToOpen(neighbour);
						}
					}
				}
			}
		}

		// since we've got an empty open list or we've run out of search 
		// there was no path. Just return null
		if (nodes[tx][ty].parent == null) 
		{
			return FAILED;
		}
		
		// At this point we've definitely found a path so we can uses the parent
		// references of the nodes to find out way from the target location back
		// to the start recording the nodes on the way.
		Node target = nodes[tx][ty];
		while (target != nodes[sx][sy]) 
		{
			path.addFirst(new Point(target.x, target.y));
			target = target.parent;
		}
		path.addFirst(new Point(sx, sy));
		
		// thats it, we have our path 
		return SUCCEEDED;
	}

	/**
	 * Get the first element from the open list. This is the next
	 * one to be searched.
	 * 
	 * @return The first element in the open list
	 */
	protected Node getFirstInOpen() 
	{
		return open.first();
	}
	
	/**
	 * Add a node to the open list
	 * 
	 * @param node The node to be added to the open list
	 */
	protected void addToOpen(Node node) 
	{
		open.add(node);
	}
	
	/**
	 * Check if a node is in the open list
	 * 
	 * @param node The node to check for
	 * @return True if the node given is in the open list
	 */
	protected boolean inOpenList(Node node) 
	{
		return open.contains(node);
	}
	
	/**
	 * Remove a node from the open list
	 * 
	 * @param node The node to remove from the open list
	 */
	protected void removeFromOpen(Node node) 
	{
		open.remove(node);
	}
	
	/**
	 * Add a node to the closed list
	 * 
	 * @param node The node to add to the closed list
	 */
	protected void addToClosed(Node node) 
	{
		closed.add(node);
	}
	
	/**
	 * Check if the node supplied is in the closed list
	 * 
	 * @param node The node to search for
	 * @return True if the node specified is in the closed list
	 */
	protected boolean inClosedList(Node node) 
	{
		return closed.contains(node);
	}
	
	/**
	 * Remove a node from the closed list
	 * 
	 * @param node The node to remove from the closed list
	 */
	protected void removeFromClosed(Node node)
	{
		closed.remove(node);
	}
	
	/**
	 * Check if a given location is valid for the supplied mover
	 * 
	 * @param mover The mover that would hold a given location
	 * @param sx The starting x coordinate
	 * @param sy The starting y coordinate
	 * @param x The x coordinate of the location to check
	 * @param y The y coordinate of the location to check
	 * @return True if the location is valid for the given mover
	 */
	protected boolean isValidLocation(int sx, int sy, int x, int y) 
	{
		boolean invalid = (x < 0) || (y < 0) || 
						  (x >= field.getN_x()) || 
						  (y >= field.getN_y());
		
		if ((!invalid) && ((sx != x) || (sy != y))) 
		{
			invalid = "Wall".equals(field.getTileType(x, y));
		}
		
		return !invalid;
	}
	
	/**
	 * Get the cost to move through a given location
	 * 
	 * @param mover The entity that is being moved
	 * @param sx The x coordinate of the tile whose cost is being determined
	 * @param sy The y coordiante of the tile whose cost is being determined
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The cost of movement through the given tile
	 */
	public float getMovementCost(int x, int y) 
	{
		if("Floor".equals(field.getTileType(x, y)))return 1;
		if("Close Door".equals(field.getTileType(x, y)))return 3;
		return 1;
	}

	/**
	 * Get the heuristic cost for the given location. This determines in which 
	 * order the locations are processed.
	 * 
	 * @param mover The entity that is being moved
	 * @param x The x coordinate of the tile whose cost is being determined
	 * @param y The y coordiante of the tile whose cost is being determined
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The heuristic cost assigned to the tile
	 */
	public float getHeuristicCost(int x, int y, int tx, int ty) {
		return heuristic.getCost(field, x, y, tx, ty);
	}
	
	/**
	 * A simple sorted list
	 *
	 * @author kevin
	 */
	private class SortedList implements Serializable
	{
		private static final long serialVersionUID = -5190940667525666414L;
		/** The list of elements */
		private ArrayList<Node> list = new ArrayList<Node>();
		
		/**
		 * Retrieve the first element from the list
		 *  
		 * @return The first element from the list
		 */
		public Node first() {
			return list.get(0);
		}
		
		/**
		 * Empty the list
		 */
		public void clear() {
			list.clear();
		}
		
		/**
		 * Add an element to the list - causes sorting
		 * 
		 * @param o The element to add
		 */
		public void add(Node n) 
		{
			list.add(n);
			Collections.sort(list);
		}
		
		/**
		 * Remove an element from the list
		 * 
		 * @param o The element to remove
		 */
		public void remove(Node n) 
		{
			list.remove(n);
		}
	
		/**
		 * Get the number of elements in the list
		 * 
		 * @return The number of element in the list
 		 */
		public int size() 
		{
			return list.size();
		}
		
		/**
		 * Check if an element is in the list
		 * 
		 * @param o The element to search for
		 * @return True if the element is in the list
		 */
		public boolean contains(Node n) 
		{
			return list.contains(n);
		}
	}
	
	/**
	 * A single node in the search graph
	 */
	private class Node implements Comparable<Node>, Serializable
	{
		private static final long serialVersionUID = 2396450716582941481L;
		/** The x coordinate of the node */
		private int x;
		/** The y coordinate of the node */
		private int y;
		/** The path cost for this node */
		private float cost;
		/** The parent of this node, how we reached it in the search */
		private Node parent;
		/** The heuristic cost of this node */
		private float heuristic;
		/** The search depth of this node */
		private int depth;
		
		/**
		 * Create a new node
		 * 
		 * @param x The x coordinate of the node
		 * @param y The y coordinate of the node
		 */
		public Node(int x, int y) 
		{
			this.x = x;
			this.y = y;
		}
		
		/**
		 * Set the parent of this node
		 * 
		 * @param parent The parent node which lead us to this node
		 * @return The depth we have no reached in searching
		 */
		public int setParent(Node parent) 
		{
			depth = parent.depth + 1;
			this.parent = parent;
			
			return depth;
		}
		
		/**
		 * @see Comparable#compareTo(Object)
		 */
		public int compareTo(Node other_node) 
		{
			float f = heuristic + cost;
			float of = other_node.heuristic + other_node.cost;
			
			if (f < of) 
			{
				return -1;
			} 
			else if (f > of) 
			{
				return 1;
			} 
			else 
			{
				return 0;
			}
		}
	}
}
