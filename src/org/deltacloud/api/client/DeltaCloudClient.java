package org.deltacloud.api.client;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dcdroid.api.HardwareProfile;
import org.dcdroid.api.Image;
import org.dcdroid.api.Instance;
import org.dcdroid.api.ObjectFactory;
import org.dcdroid.api.Realm;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DeltaCloudClient implements API
{
	private static enum DCNS
	{ 
		INSTANCES, REALMS, IMAGES, HARDWARE_PROFILES, START, STOP, REBOOT, DESTROY, INSTANCE_STATES;

		public String toString()
		{
			return "/" + name().toLowerCase();
		}
	}

	private static enum RequestType { POST, GET };

	private URL baseUrl;

	private String username;

	private String password;

	private ObjectFactory objectFactory;

	public DeltaCloudClient(URL url, String username, String password) throws DeltaCloudClientException
	{
		this.baseUrl = url;
		this.username = username;
		this.password = password;
		this.objectFactory = ObjectFactory.getInstance();
	}


	public Instance createInstance(String imageId, String hardwareProfileId, String realmId, String name) throws DeltaCloudClientException 
	{
		String query = "?image_id=" + imageId + "&hwp_id=" + hardwareProfileId + "&realm_id=" + realmId + "&name=" + "intance-name" + "&commit=create";
		return objectFactory.createInstance(sendRequest(DCNS.INSTANCES + query, RequestType.POST));
	}

	
	public HardwareProfile listHardwareProfile(String hardwareProfileId) throws DeltaCloudClientException 
	{
		String request = DCNS.HARDWARE_PROFILES + "/" + hardwareProfileId;
		return objectFactory.createHardwareProfile(sendRequest(request, RequestType.GET));
	}

	
	public List<HardwareProfile> listHardwareProfiles() throws DeltaCloudClientException 
	{
		String request = DCNS.HARDWARE_PROFILES.toString();
		return objectFactory.createHardwareProfiles(sendRequest(request, RequestType.GET)).getHardwareProfiles();
	}
	
	
	public List<Image> listImages() throws DeltaCloudClientException 
	{
		String request = DCNS.IMAGES.toString();
		return objectFactory.createImages(sendRequest(request, RequestType.GET)).getImages();
	}

	
	public List<Image> listImages(Map<String, String> params) throws DeltaCloudClientException 
	{
		String request = DCNS.IMAGES.toString() + "?";
		for(String key : params.keySet())
		{
			request += key + "=" + params.get(key) + "&";
		}
		return objectFactory.createImages(sendRequest(request, RequestType.GET)).getImages();
	}

	
	public Image listImages(String imageId) throws DeltaCloudClientException 
	{
		String request = DCNS.IMAGES + "/" + imageId;
		return objectFactory.createImages(sendRequest(request, RequestType.GET)).getImages().get(0);
	}

	
	public List<Instance> listInstances() throws DeltaCloudClientException 
	{
		String request = DCNS.INSTANCES.toString();
		return objectFactory.createInstances(sendRequest(request, RequestType.GET)).getInstances();
	}

	
	public Instance listInstances(String instanceId) throws DeltaCloudClientException 
	{
//		String request = DCNS.INSTANCES + "/" + instanceId;
//		Instance instance = unmarshall(sendRequest(request, RequestType.GET), Instance.class);
//		populateInstanceObjects(instance);
		return null;
	}
	
	
	public List<Realm> listRealms() throws DeltaCloudClientException 
	{
		String request = DCNS.REALMS.toString();
		return objectFactory.createRealms(sendRequest(request, RequestType.GET)).getRealms();
	}

	
	public Realm listRealms(String realmId) throws DeltaCloudClientException 
	{
		String request = DCNS.REALMS + "/" + realmId;
		//return unmarshall(sendRequest(request, RequestType.GET), Realm.class);
		return null;
	}
	
	
	public Instance createInstance(String imageId) throws DeltaCloudClientException 
	{
		String query = "?image_id=" + imageId;
		return objectFactory.createInstance(sendRequest(DCNS.INSTANCES + query, RequestType.POST));
	}

	
	public boolean performInstanceAction(String instanceId, String action) throws DeltaCloudClientException
	{
		Instance instance = listInstances(instanceId);
		if(instance.getActionNames().contains(action))
		{
			String request = DCNS.INSTANCES + "/" + instanceId + "/" + action;
			sendRequest(request, RequestType.POST);
			return true;
		}
		return false;
	}

	
	public boolean performAction(String href, String requestType) throws DeltaCloudClientException
	{
		try
		{
			RequestType req = null;
			if(requestType.toLowerCase().equals("get"))
			{
				req = RequestType.GET;
			}
			else
			{
				req = RequestType.POST;
			}
			String[] link = href.split("/api");
			sendRequest(link[1], req);
			return true;
		}
		catch(Exception e)
		{
			throw new DeltaCloudClientException(e);
		}
		
	}

	private Node sendRequest(String path, RequestType requestType) throws DeltaCloudClientException
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(baseUrl.getHost(), baseUrl.getPort()), new UsernamePasswordCredentials(username, password));
        
		String requestUrl = baseUrl.toString() + path;
		
		try
		{
			HttpUriRequest request = null;
			if(requestType == RequestType.POST)
			{
				request = new HttpPost(requestUrl);
			}
			else
			{
				request = new HttpGet(requestUrl);
			}
			
			request.setHeader("Accept", "application/xml");
			HttpResponse httpResponse = httpClient.execute(request);
			
			HttpEntity entity = httpResponse.getEntity();
			
			if(entity != null)
			{
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setIgnoringElementContentWhitespace(true);
				DocumentBuilder db = dbf.newDocumentBuilder();

				Document doc = db.parse(entity.getContent());
				doc.getDocumentElement().normalize();

				removeWhiteSpaceNodes(doc);

				return doc.getDocumentElement();
			}
		}
		catch(Exception e)
		{
			throw new DeltaCloudClientException("Error processing request to: " + requestUrl, e);
		}
		throw new DeltaCloudClientException("Could not execute request to:" + requestUrl);
	}
	
	private void removeWhiteSpaceNodes(Document doc) throws DeltaCloudClientException
	{
		try
		{
			XPathFactory xpathFactory = XPathFactory.newInstance();
			// XPath to find empty text nodes.
			XPathExpression xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");  
			NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);
		
			// Remove each empty text node from document.
			for (int i = 0; i < emptyTextNodes.getLength(); i++) 
			{
			    Node emptyTextNode = emptyTextNodes.item(i);
			    emptyTextNode.getParentNode().removeChild(emptyTextNode);
			}
		}
		catch(Exception e)
		{
			throw new DeltaCloudClientException("Error removing white space from document: ", e);
		}
	}
}
