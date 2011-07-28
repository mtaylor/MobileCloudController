package org.dcdroid.api;

import java.util.ArrayList;
import java.util.List;

public class HardwareProfiles 
{
    protected List<HardwareProfile> hardwareProfiles;

	public HardwareProfiles()
	{
		hardwareProfiles = new ArrayList<HardwareProfile>();
	}

    public List<HardwareProfile> getHardwareProfiles() 
    {
        return this.hardwareProfiles;
    }

    public void addHardwareProfile(HardwareProfile hardwareProfile)
    {
    	hardwareProfiles.add(hardwareProfile);
    }

    public void setHardwareProfiles(List<HardwareProfile> hardwareProfiles)
    {
    	this.hardwareProfiles = hardwareProfiles;
    }
}
