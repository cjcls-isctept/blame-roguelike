package su.msk.dunno.blame.main.support;

import java.util.LinkedList;

import su.msk.dunno.blame.prototypes.AObject;


public class LinkObject extends LinkedList<AObject> 
{
	private static final long serialVersionUID = -6865353800497251458L;
	
	public LinkedList<AObject> clone()
	{
		LinkedList<AObject> llao = new LinkedList<AObject>();
		for(AObject ao: this)
		{
			llao.add(ao);
		}
		return llao;
	}
}
