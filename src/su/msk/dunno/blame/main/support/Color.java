package su.msk.dunno.blame.main.support;

public class Color 
{
	public static final Color RED = new Color(1, 0, 0);
	public static final Color GREEN = new Color(0, 1, 0);
	public static final Color BLUE = new Color(0, 0, 1);
	public static final Color CORNFLOWER = new Color(0.39f, 0.58f, 0.93f);
	public static final Color CYAN = new Color(0, 1, 1);
	public static final Color YELLOW = new Color(1, 1, 0);
	public static final Color WHITE = new Color(1, 1, 1);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color GRAY = new Color(0.3f, 0.3f, 0.3f);
	
	private float[] color_array;
	
	public Color(float red, float green, float blue)
	{
		color_array = new float[3];
		color_array[0] = red;
		color_array[1] = green;
		color_array[2] = blue;
	}
	
	public float getRed()
	{
		return color_array[0];
	}
	
	public float getGreen()
	{
		return color_array[1];
	}
	
	public float getBlue()
	{
		return color_array[2];
	}
}
