package su.msk.dunno.blame.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import su.msk.dunno.blame.map.Field;
import su.msk.dunno.blame.objects.Livings;
import su.msk.dunno.blame.objects.livings.Cibo;
import su.msk.dunno.blame.objects.livings.Killy;
import su.msk.dunno.blame.support.Color;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.listeners.EventManager;
import su.msk.dunno.blame.support.listeners.KeyListener;

public class Blame 
{
	public static boolean isFullscreen = false;
	
	public static int width = 800;
	public static int height = 600;
	
	public static int N_x = 100;
	public static int N_y = 100;

	private int num_creatures = 60;
	public static int num_stations = 10;
	
	private boolean isRunning;
	
	public static int scale = 20;
	public static int framerate = 70;
	
	private static Killy killy;
	private static Cibo cibo;
	public static boolean playCibo;
	
	Field field;

	// variables below are for infinite (with pressed key) moving purposes
	private static int frames;
	private static long msek = System.currentTimeMillis();
	public static int fps;
	
	public Blame()
	{
		fillVariables();
		initGL();
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT*/);		
		GL11.glLoadIdentity();
		MyFont.instance().drawString("Loading system...", 20, height-20, 0.2f, Color.GREEN);
		Display.sync(Blame.framerate);
		Display.update();
		
		MyFont.instance().initDisplayLists();
		initEvents();

		/*for(int i=0; i < 5000; i++)*/field = new Field(N_x, N_y, "random");
		
		Livings.instance().addField(field);
		killy = new Killy(field.getRandomCorner(), field);
		cibo = new Cibo(field.getRandomPos(killy.cur_pos.plus(-2,2), killy.cur_pos.plus(2,-2)), field);	// generate cibo near killy
		Livings.instance().addKilly(killy);
		Livings.instance().addCibo(cibo);
		Livings.instance().addCreatures(num_creatures);
		
		isRunning = true;
		run();
	}	

	public void run()
	{
		while(isRunning)
		{
			checkRequests();
			/*GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);		
			GL11.glLoadIdentity();
			MyFont.instance().drawDisplayList(MyFont.WALL, 320, 240, Color.GREEN);*/
			(playCibo?cibo:killy).process();
			/*Display.sync(Blame.framerate);
			Display.update();*/
			getFPS();
		}
		MyFont.instance().destroy();
		Display.destroy();
	}
	
	public void checkRequests()
	{
    	if (Display.isCloseRequested()) 
	    {
	    	isRunning = false;
	    }
    	EventManager.instance().checkEvents();
	}
	
	public void initGL() {
		try {
			if(isFullscreen)Display.setFullscreen(true);
			else Display.setDisplayMode(new DisplayMode(width, height));
			
			Display.setTitle("Blame v0.0.2");
			Display.setVSyncEnabled(true);
			Display.create(); } 
		catch (LWJGLException e) {
			e.printStackTrace(); }
		
		width = Display.getDisplayMode().getWidth();
		height = Display.getDisplayMode().getHeight();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0,0,0,0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		//GL11.glClearDepth(1.0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				
        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix
        GLU.gluOrtho2D(0, width, 0, height);
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }
	
	public void initEvents()
	{		
		EventManager.instance().addListener(Keyboard.KEY_ADD, new KeyListener(1)
        {
        	public void onKeyDown()
        	{
        		if(scale < 80)scale++;
        	}
        });
		EventManager.instance().addListener(Keyboard.KEY_SUBTRACT, new KeyListener(1)
        {
        	public void onKeyDown()
        	{
        		if(scale > 5)scale--;
        	}
        });
		EventManager.instance().addListener(Keyboard.KEY_TAB, new KeyListener(0)
        {
        	public void onKeyDown()
        	{
        		playCibo = !playCibo;
        	}
        });
	}
	
	public static void getFPS()
	{
		frames++;
		if (System.currentTimeMillis() - msek >= 1000)
		{
			fps = frames;
			frames = 0;
			msek = System.currentTimeMillis();
		}
	}
	
	public static Killy getCurrentPlayer()
	{
		return playCibo?cibo:killy;
	}
	
	public static Killy getPlayer(String name)
	{
		if("Killy".equals(name))return killy;
		else if("Cibo".equals(name))return cibo;
		else return null;
	}
	
	private void fillVariables() 
	{
		Properties p = new Properties();
		try 
		{
			p.load(new FileInputStream("options.txt"));
			framerate = Integer.valueOf(p.getProperty("framerate"));
			isFullscreen = p.getProperty("fullscreen").equalsIgnoreCase("yes");
			width = Integer.valueOf(p.getProperty("width"));
			height = Integer.valueOf(p.getProperty("height"));
			N_x = Integer.valueOf(p.getProperty("N_x"));
			N_y = Integer.valueOf(p.getProperty("N_y"));
			num_creatures = Integer.valueOf(p.getProperty("creatures"));
			num_stations = Integer.valueOf(p.getProperty("stations"));
		} 
		catch (FileNotFoundException e) 
		{
			framerate = 70;
			isFullscreen = false;
			width = 800;
			height = 600;
		} 
		catch (IOException e) 
		{
			framerate = 70;
			isFullscreen = false;
			width = 800;
			height = 600;
		}
	}
	
	public static void main(String args[])
	{
		new Blame();
	}
}
