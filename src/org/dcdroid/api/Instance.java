package org.dcdroid.api;

import java.util.ArrayList;
import java.util.List;

public class Instance {

    protected String name;
    protected String ownerId;
    protected Image image;
    protected HardwareProfile hardwareProfile;
    protected Realm realm;
    protected String state;
    protected PublicAddresses publicAddresses;
    protected PrivateAddresses privateAddresses;
    protected Actions actions;
    protected String href;
    protected String id;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String value) {
        this.ownerId = value;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image value) {
        this.image = value;
    }

    public HardwareProfile getHardwareProfile() {
        return hardwareProfile;
    }

    public void setHardwareProfile(HardwareProfile value) {
        this.hardwareProfile = value;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm value) {
        this.realm = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String value) {
        this.state = value;
    }

    public PublicAddresses getPublicAddresses() {
        return publicAddresses;
    }

    public void setPublicAddresses(PublicAddresses value) {
        this.publicAddresses = value;
    }

    public PrivateAddresses getPrivateAddresses() {
        return privateAddresses;
    }

    public void setPrivateAddresses(PrivateAddresses value) {
        this.privateAddresses = value;
    }

    public Actions getActions() {
        return actions;
    }

    public void setActions(Actions value) 
    {
        this.actions = value;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String value) {
        this.href = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }
    
    public List<String> getActionNames()
    {
    	ArrayList<String> actions = new ArrayList<String>();
    	for(Link link : getActions().getLinks())
    	{
    		actions.add(link.getRel());
    	}
    	return actions;
    }

    public void print()
    {
    	System.out.println(" ============== Instance ============");
		System.out.println("Instance: " + getId());
		System.out.println("\tName: " + getName());
		System.out.println("\tOwnerId: " + getOwnerId());
		System.out.println("\tImage: " + getImage().getId());
		System.out.println("\tRealm: " + getRealm().getId());
		System.out.println("\tHardwareProfile: " + getHardwareProfile().getId());
		System.out.println("\tState: " + getState());
		System.out.println("\tPublic Addresses:");
		for(Address address : getPublicAddresses().getAddresses())
		{
			System.out.println("\t\t" + address.getAddress());
		}

		System.out.println("\tPrivate Addresses:");
		for(Address address : getPrivateAddresses().getAddresses())
		{
			System.out.println("\t\t" + address.getAddress());
		}
		
		System.out.println("\tActions:");
		for(Link link : getActions().getLinks())
		{
			System.out.println("\t\tMethod: " + link.getMethod() + ", Rel: " + link.getRel() + ", Href: " + link.getHref());
		}
    }
}
