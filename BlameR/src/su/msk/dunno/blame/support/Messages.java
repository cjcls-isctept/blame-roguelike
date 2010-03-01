package su.msk.dunno.blame.support;

import java.util.LinkedList;

public class Messages 
{
	private static Messages instance;
	private LinkedList<String> messages;
	private int message_capacity;
	
	public static Messages instance()
	{
		if(instance == null)
		{
			instance = new Messages();
		}
		return instance;
	}
	
	public Messages()
	{
		messages = new LinkedList<String>();
		message_capacity = 6;
	}
	
	public void addMessage(String s)
	{
		messages.add(s);
		if(messages.size() > message_capacity)messages.removeFirst();
	}
	
	public void showMessages()
	{
		int h = 75;
		for(String mes: messages)
		{
			TrueTypeFont.instance().drawString(mes, 20, h, Color.WHITE); h -=15;
		}
	}

	public void clear() 
	{
		messages.clear();
	}
}
