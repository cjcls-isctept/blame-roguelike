package su.msk.dunno.blame.main.support;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public final class Vector2D 
implements Serializable
{
    private static final long serialVersionUID = -251004023998422420L;

    public float x;
    public float y;

    public Vector2D() 
    {
        x = 0;
        y = 0;
    }

    public Vector2D(float x, float y) 
    {
        this.x = x;
        this.y = y;
    }

    public Vector2D(double x, double y) 
    {
        this.x = (float) x;
        this.y = (float) y;
    }
    
    public Vector2D(Vector2D vec) 
    {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vector2D(MouseEvent mE) 
    {
        if (mE == null) 
        {
            this.x = 0;
            this.y = 0;
        } 
        else 
        {
            this.x = mE.getX();
            this.y = mE.getY();
        }
    }
//
//    public String toString() 
//    {
//        return (int) x + ":" + (int) y;
//    }
//
    public float norma() 
    {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float norma2() 
    {
        return x * x + y * y;
    }
    
    public Vector2D plus(Vector2D vec) 
    {
        return new Vector2D(x + vec.x, y + vec.y);
    }

    public Vector2D minus(Vector2D vec) 
    {
        return new Vector2D(x - vec.x, y - vec.y);
    }

    public Vector2D mul(float k) 
    {
        return new Vector2D(x * k, y * k);
    }

    public Vector2D mul(double k) 
    {
        return new Vector2D(x * k, y * k);
    }
//
    public Vector2D div(float k) 
    {
        if (k == 0)return new Vector2D(x / 0.001f, y / 0.001f);
        else return new Vector2D(x / k, y / k);
    }
//
//    public Vector2D neg() 
//    {
//        return new Vector2D(-x, -y);
//    }
//
    public Vector2D rotate(double an) 
    {
        float x_res = (float) (x * Math.cos(an) - y * Math.sin(an));
        float y_res = (float) (x * Math.sin(an) + y * Math.cos(an));
        return new Vector2D(x_res, y_res);
    }

    public Vector2D unary() 
    {
        return this.div(this.norma());
    }

    public Point toPoint() 
    {
        return new Point((int) (x), (int) (y));
    }
//
//    public float getDistance(Vector2D vec) 
//    {
//        return (float) Math.sqrt(getDistance2(vec));
//    }
//
    public float getDistance2(Vector2D vec) 
    {
        float xx = (x - vec.x);
        float yy = (y - vec.y);
        return xx * xx + yy * yy;
    }

    public float scalar_mul(Vector2D vec) 
    {
        return x * vec.x + y * vec.y;
    }

    public float skew_mul(Vector2D vec) 
    {
        return x * vec.y - vec.x * y;
    }

    public float getAng(Vector2D vec) 
    {
        return (float)(180/Math.PI*Math.acos(this.scalar_mul(vec) / this.norma() / vec.norma()));
    }

    public boolean equals(Vector2D vec) 
    {
        return this.x == vec.x && this.y == vec.y;
    }
    
    public boolean notZero()
    {
    	return x != 0 || y != 0;
    }
    
    public void makeZero()
    {
    	x = 0; y = 0;
    }
//    
//    public void draw(Graphics2D g)
//    {
//    	Color c = g.getColor();
//    	g.setColor(Color.WHITE);
//    	g.fillOval((int)x, (int)y, 3, 3);
//    	g.setColor(c);
//    }

	public Vector2D copy() 
	{
		return new Vector2D(x, y);
	}
}
