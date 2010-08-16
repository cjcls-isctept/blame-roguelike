package su.msk.dunno.blame.objects.symbols;

import su.msk.dunno.blame.prototypes.AObject;
import su.msk.dunno.blame.support.MyFont;
import su.msk.dunno.blame.support.Point;

public class SocketSymbol extends AObject
{

	public SocketSymbol(int i, int j) 
	{
		super(i, j);
	}

	public SocketSymbol(Point cur_pos) 
	{
		super(cur_pos);
	}

	@Override public String getName() 
	{
		return "SocketPlace";
	}

	@Override public boolean getPassability() 
	{
		return true;
	}
	
	@Override public int getSymbol()
	{
		return MyFont.SOCKET;
	}

	@Override public boolean getTransparency()	
	{
		return true;
	}
}
