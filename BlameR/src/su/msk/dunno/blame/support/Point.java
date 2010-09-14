package su.msk.dunno.blame.support;

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
	
	public Point plus(int i, int j)
	{
		return new Point(x + i, y + j);
	}
	
	public Point minus(Point p)
	{
		return new Point(x - p.x, y - p.y);
	}
	
	public Point minus(int i, int j)
	{
		return new Point(x - i, y - j);
	}	
	
	public Point mul(int k)
	{
		return new Point(x*k, y*k);
	}

	public void set(Point point) 
	{
		x = point.x;
		y = point.y;
	}
	
	@Override public String toString()
	{
		return x+" : "+y;
	}
}
