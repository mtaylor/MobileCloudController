//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.26 at 02:05:59 PM BST 
//


package org.dcdroid.api;

import java.util.ArrayList;
import java.util.List;

public class Images 
{
    protected List<Image> images;

	public Images()
	{
		images = new ArrayList<Image>();
	}

	public List<Image> getImages() 
	{
        return this.images;
    }

	public void addImage(Image image)
	{
		images.add(image);
	}
}
