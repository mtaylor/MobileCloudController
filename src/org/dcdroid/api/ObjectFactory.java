//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.26 at 02:05:59 PM BST 
//


package org.dcdroid.api;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ObjectFactory 
{
	private static ObjectFactory objectFactory = null;

    private ObjectFactory() 
    {
    }

    public static ObjectFactory getInstance()
    {
    	if(objectFactory == null)
    	{
    		objectFactory = new ObjectFactory();
    	}
    	return objectFactory;
    }

    public HardwareProfiles createHardwareProfiles(Node node)
    {
    	HardwareProfiles hardwareProfiles = new HardwareProfiles();
    	NodeList children = node.getChildNodes();
    	
    	for(int i = 0; i < children.getLength(); i ++)
    	{
    		hardwareProfiles.addHardwareProfile(createHardwareProfile(children.item(i)));
    	}
    	
    	return hardwareProfiles;
    }

    public HardwareProfile createHardwareProfile(Node node)
	{
		NamedNodeMap attr = node.getAttributes();
		NodeList children = node.getChildNodes();

		HardwareProfile hardwareProfile = new HardwareProfile();
		
		hardwareProfile.setHref(attr.getNamedItem("href").getNodeValue());
		hardwareProfile.setId(attr.getNamedItem("id").getNodeValue());
		
		for(int i = 0; i < children.getLength(); i ++)
		{
			Node child = children.item(i);
			if(child.getNodeName().equals("name"))
			{
				hardwareProfile.setName(child.getFirstChild().getNodeValue());
			}
			else if(child.getNodeName().equals("property"))
			{
				hardwareProfile.addProperty(createProperty(child));
			}
		}
		
		return hardwareProfile;
	}

    public Property createProperty(Node node)
	{
		NamedNodeMap attr = node.getAttributes();
		NodeList children = node.getChildNodes();

		Property property = new Property();
		property.setKind(attr.getNamedItem("kind").getNodeValue());
		property.setUnit(attr.getNamedItem("unit").getNodeValue());
		property.setName(attr.getNamedItem("name").getNodeValue());
		property.setValue(attr.getNamedItem("value").getNodeValue());

		for(int i = 0; i < children.getLength(); i ++)
		{
			Node child = children.item(i);
			if(child.getNodeName().equals("enum"))
			{
				property.setEnum(createEnum(child));
			}
			else if(child.getNodeName().equals("range"))
			{
				property.setRange(createRange(child));
			}
		}
		return property;
	}

    public org.dcdroid.api.Enum createEnum(Node node)
	{
		org.dcdroid.api.Enum enumerator = new Enum();

		NodeList children = node.getChildNodes();
		for(int i = 0; i < children.getLength(); i ++)
		{
			enumerator.addEntry(createEntry(children.item(i)));
		}
		return enumerator;
	}

    public Entry createEntry(Node node)
	{
		Entry entry = new Entry();
		entry.setValue(node.getNodeValue());
		return entry;
	}

    public Range createRange(Node node)
	{
		Range range = new Range();
		NamedNodeMap attr = node.getAttributes();
		
		range.setFirst(attr.getNamedItem("first").getNodeValue());
		range.setLast(attr.getNamedItem("last").getNodeValue());
		return range;
	}

    public Realms createRealms(Node node)
	{
    	Realms realms = new Realms();
    	NodeList children = node.getChildNodes();
    	
    	for(int i = 0; i < children.getLength(); i ++)
    	{
    		realms.addRealm(createRealm(children.item(i)));
    	}
    	
    	return realms;
	}

    public Realm createRealm(Node node)
	{
		NamedNodeMap attr = node.getAttributes();
		NodeList children = node.getChildNodes();

		Realm realm = new Realm();
		
		realm.setHref(attr.getNamedItem("href").getNodeValue());
		realm.setId(attr.getNamedItem("id").getNodeValue());

		for(int i = 0; i < children.getLength(); i ++)
		{
			Node child = children.item(i);
			if(child.getNodeName().equals("name"))
			{
				realm.setName(child.getFirstChild().getNodeValue());
			}
			else if (child.getNodeName().equals("state"))
			{
				realm.setState(child.getFirstChild().getNodeValue());
			}
			else if (child.getNodeName().equals("limit") && child.getChildNodes().getLength() > 0)
			{
				realm.setLimit(child.getFirstChild().getNodeValue());
			}
		}
		return realm;
	}
    
    public Images createImages(Node node)
    {
    	Images images = new Images();
    	NodeList children = node.getChildNodes();
    	
    	for(int i = 0; i < children.getLength(); i ++)
    	{
    		images.addImage(createImage(children.item(i)));
    	}
    	
    	return images;
    }
    
    public Image createImage(Node node)
    {		
    	NamedNodeMap attr = node.getAttributes();
		NodeList children = node.getChildNodes();
		
		Image image = new Image();

		image.setHref(attr.getNamedItem("href").getNodeValue());
		image.setId(attr.getNamedItem("id").getNodeValue());
			
		for(int i = 0; i < children.getLength(); i ++)
		{
			Node child = children.item(i);
			if(child.getNodeName().equals("name"))
			{
				image.setName(child.getFirstChild().getNodeValue());
			}
			else if(child.getNodeName().equals("owner_id"))
			{
				image.setOwnerId(child.getFirstChild().getNodeValue());
			}
			else if(child.getNodeName().equals("description"))
			{
				image.setDescription(child.getFirstChild().getNodeValue());
			}
			else if(child.getNodeName().equals("architecture"))
			{
				image.setArchitecture(child.getFirstChild().getNodeValue());
			}
		}
		return image;
    }

    public Instances createInstances(Node node) 
    {
    	Instances instances = new Instances();
    	NodeList children = node.getChildNodes();
    	
    	for(int i = 0; i < children.getLength(); i ++)
    	{
    		instances.addInstance(createInstance(children.item(i)));
    	}
    	
    	return instances;
    }

    public Instance createInstance(Node node) 
    {
    	NamedNodeMap attr = node.getAttributes();
		NodeList children = node.getChildNodes();
		
		Instance instance = new Instance();

		instance.setHref(attr.getNamedItem("href").getNodeValue());
		instance.setId(attr.getNamedItem("id").getNodeValue());		
		
		for(int i = 0; i < children.getLength(); i ++)
		{
			Node child = children.item(i);
			if(child.getNodeName().equals("name"))
			{
				instance.setName(child.getFirstChild().getNodeValue());
			}
			else if(child.getNodeName().equals("owner_id"))
			{
				instance.setOwnerId(child.getFirstChild().getNodeValue());
			}
			else if(child.getNodeName().equals("state"))
			{
				instance.setState(child.getFirstChild().getNodeValue());
			}
			else if(child.getNodeName().equals("launch_time"))
			{
				//instance.setLaunchTime((child.getFirstChild().getNodeValue());
			}
			else if(child.getNodeName().equals("image"))
			{
				instance.setImage(createImage(child));
			}
			else if(child.getNodeName().equals("hardware_profile"))
			{
				instance.setHardwareProfile(createHardwareProfile(child));
			}
			else if(child.getNodeName().equals("realm"))
			{
				instance.setRealm(createRealm(child));
			}
			else if(child.getNodeName().equals("actions"))
			{
				instance.setActions(createActions(child));
			}
			else if(child.getNodeName().equals("private_addresses"))
			{
				instance.setPrivateAddresses(createPrivateAddresses(child));
			}
			else if(child.getNodeName().equals("public_addresses"))
			{
				instance.setPublicAddresses(createPublicAddresses(child));
			}
		}
		return instance;
    }

    public Actions createActions(Node node) 
    {
    	Actions actions = new Actions();
    	NodeList children = node.getChildNodes();
    	
    	for(int i = 0; i < children.getLength(); i ++)
    	{
    		actions.addLink(createLink(children.item(i)));
    	}
    	
    	return actions;
    }

    public Link createLink(Node node) 
    {
    	NamedNodeMap attr = node.getAttributes();
    	
        Link link = new Link();	

		link.setHref(attr.getNamedItem("href").getNodeValue());
		link.setMethod(attr.getNamedItem("method").getNodeValue());
		link.setRel(attr.getNamedItem("rel").getNodeValue());

		return link;
    }
    
    public PrivateAddresses createPrivateAddresses(Node node) 
    {
    	PrivateAddresses privateAddresses = new PrivateAddresses();
    	NodeList children = node.getChildNodes();
		for(int i = 0; i < children.getLength(); i ++)
		{
			privateAddresses.addAddress(createAddress(children.item(i)));
		}
		return privateAddresses;
    }

    public PublicAddresses createPublicAddresses(Node node) 
    {
    	PublicAddresses publicAddresses = new PublicAddresses();
    	NodeList children = node.getChildNodes();
		for(int i = 0; i < children.getLength(); i ++)
		{
			publicAddresses.addAddress(createAddress(children.item(i)));
		}
		return publicAddresses;
    }

    
    public Address createAddress(Node node)
    {
    	Address address = new Address();
    	address.setAddress(node.getFirstChild().getNodeValue());
    	return address;
    }

    public Param createParam(Node node) 
    {
        return null;
    }
}
