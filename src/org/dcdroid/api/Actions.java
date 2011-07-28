package org.dcdroid.api;

import java.util.ArrayList;
import java.util.List;

public class Actions 
{
	protected List<Link> links;

	public Actions()
	{
		links = new ArrayList<Link>();
	}
    

    public List<Link> getLinks() 
    {
    	return links;
    }

    public void addLink(Link link)
    {
    	links.add(link);
    }
}
