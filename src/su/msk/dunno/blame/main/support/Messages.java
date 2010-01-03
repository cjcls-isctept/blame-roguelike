package su.msk.dunno.blame.main.support;

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
		message_capacity = 5;
	}
	
	public void addMessage(String s)
	{
		messages.add(s);
		if(messages.size() > message_capacity)messages.removeFirst();
	}
	
	public void showMessages()
	{
		int h = 60;
		for(String mes: messages)
		{
			MyFont.instance().drawString(mes, 20, h, 0.2f, Color.WHITE); h -=10;
		}
	}
}
