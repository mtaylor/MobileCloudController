package org.dcdroid.api;

import java.util.ArrayList;
import java.util.List;

public class HardwareProfile {

    protected String name;
    protected List<Property> properties;
    protected String id;
    protected String href;

    public HardwareProfile()
    {
    	properties = new ArrayList<Property>();
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public List<Property> getProperties() 
    {
        return this.properties;
    }

    public void addProperty(Property property)
    {
    	properties.add(property);
    }

    public void setProperties(List<Property> properties)
    {
    	this.properties = properties;
    }

    public Property getProperty(String name)
    {
    	for(Property property : properties)
    	{
    		if (property.name.equals(name))
    		{
    			return property;
    		}
    	}
    	return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String value) {
        this.href = value;
    }

    public void print()
    {
    	System.out.println("============ Hardware Profile =============");
		System.out.println(getId());
		System.out.println(getName());
		System.out.println(getHref());
		System.out.println("\t======== Properties ========");
		for(Property property : getProperties())
		{
			System.out.println("\t" + property.getName());
			System.out.println("\t" + property.getKind());
			System.out.println("\t" + property.getUnit());
			System.out.println("\t" + property.getValue());
			if(property.getKind().equals("range"))
			{
				System.out.println("\t\t======== Range =======");
				System.out.println("\t\t" + property.getRange().getFirst());
				System.out.println("\t\t" + property.getRange().getLast());
			}
			else if (property.getKind().equals("enum"))
			{
				System.out.println("\t\t======== Enums =======");
				for(Entry entry : property.getEnum().getEntries())
				{
					System.out.println("\t\t" + entry.getValue());
				}
			}
		}
	}
}