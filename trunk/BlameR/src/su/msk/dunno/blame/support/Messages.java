package su.msk.dunno.blame.support;

import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

import su.msk.dunno.blame.main.Blame;

public class Messages 
{
	private static Messages instance;
	private LinkedList<String> messages;
	private int message_capacity;
	private ResourceBundle localized_messages;
	
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
		if(Blame.lang != null)localized_messages = ResourceBundle.getBundle("Messages", new Locale(Blame.lang));
		else localized_messages = ResourceBundle.getBundle("Messages", new Locale("en"));
	}
	
	public String getPropMessage(String message_code)
	{
		return localized_messages.getString(message_code);
	}
	
	public String getPropMessage(String message_code, String parameter1)
	{
		String s = localized_messages.getString(message_code); 
		s = s.replaceFirst("\\?", parameter1);
		return s;
	}
	
	public String getPropMessage(String message_code, String parameter1, String parameter2) 
	{
		String s = localized_messages.getString(message_code); 
		s = s.replaceFirst("\\?", parameter1);
		s = s.replaceFirst("\\?", parameter2);
		return s;
	}
	
	public void addPropMessage(String message_code)
	{
		String s = localized_messages.getString(message_code);
		addMessage(s);
	}
	
	public void addPropMessage(String message_code, String parameter1)
	{
		String s = localized_messages.getString(message_code);
		s = s.replaceFirst("\\?", parameter1);
		addMessage(s);
	}
	
	public void addPropMessage(String message_code, String parameter1, String parameter2)
	{
		String s = localized_messages.getString(message_code);
		s = s.replaceFirst("\\?", parameter1);
		s = s.replaceFirst("\\?", parameter2);
		addMessage(s);
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
