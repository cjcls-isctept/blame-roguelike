package su.msk.dunno.blame.main.support;

public class Point
{
	public int x;
	public int y;
	
	public Point()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Point p)
	{
		return x == p.x && y == p.y;
	}
	
	public int getDist2(Point p)
	{
		return (x-p.x)*(x-p.x) + (y-p.y)*(y-p.y);
	}
	
	public Point plus(Point p)
	{
		return new Point(x + p.x, y + p.y);
	}
	
	public Point minus(Point p)
	{
		return new Point(x - p.x, y - p.y);
	}
	
	public Point mul(int k)
	{
		return new Point(x*k, y*k);
	}
}
