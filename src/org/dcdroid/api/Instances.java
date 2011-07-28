package org.dcdroid.api;

import java.util.ArrayList;
import java.util.List;

public class Instances 
{
    protected List<Instance> instances;

	public Instances()
	{
		instances = new ArrayList<Instance>();
	}

    public List<Instance> getInstances() 
    {
        return this.instances;
    }

    public void addInstance(Instance instance)
    {
    	instances.add(instance);
    }
}
