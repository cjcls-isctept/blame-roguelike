package su.msk.dunno.blame.main;

import java.util.Calendar;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import su.msk.dunno.blame.containers.Field;
import su.msk.dunno.blame.containers.LivingList;
import su.msk.dunno.blame.livings.Cibo;
import su.msk.dunno.blame.livings.Killy;
import su.msk.dunno.blame.main.support.MyFont;
import su.msk.dunno.blame.main.support.listeners.EventManager;
import su.msk.dunno.blame.main.support.listeners.KeyListener;

public class Blame 
{
	public static boolean isFullscreen = false;
	
	public static int width = 640;
	public static int height = 480;
	
	public static int N_x = 100;
	public static int N_y = 100;
	
	private boolean isRunning;
	
	public static int scale = 20;
	public static int framerate = 70;
	
	private static Killy killy;
	private static Cibo cibo;
	public static boolean playCibo;
	
	Field field;
	LivingList livings;

	// variables below are for infinite (with pressed key) moving purposes
	private int frames;
	private long msek = Calendar.getInstance().getTimeInMillis();
	public static int fps;
	
	public Blame()
	{
		initGL();				
		initEvents();

		/*for(int i=0; i < 5000; i++)*/field = new Field(N_x, N_y, "random");
		
		livings = new LivingList(field);
		killy = new Killy(field.getRandomPos(), 
						  field, livings);
		cibo = new Cibo(field.getRandomPos(killy.cur_pos.plus(-2,2), killy.cur_pos.plus(2,-2)), 
				        field, livings);	// generate cibo near killy
		livings.addKilly(killy);
		livings.addCibo(cibo);
		livings.addCreatures(40);
		
		isRunning = true;
		run();
	}
	
	public void run()
	{
		while(isRunning)
		{
			checkRequests();
			(playCibo?cibo:killy).process();
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
			else {
				Display.setDisplayMode(new DisplayMode(width, height)); }
			
			Display.setTitle("Blame v0.0.1");
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
	
	private void getFPS()
	{
		frames++;
		if (Calendar.getInstance().getTimeInMillis() - msek >= 1000)
		{
			fps = frames;
			frames = 0;
			msek = Calendar.getInstance().getTimeInMillis();
		}
	}
	
	public static void main(String args[])
	{
		new Blame();
	}
}
