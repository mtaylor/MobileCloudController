package org.dcdroid.ui.provideraccounts;

public class ProviderAccount 
{
	private long id;

	private String name;
	
	private String username;
	
	private String password;

	private long providerId;
	
	public long getProviderId() 
	{
		return providerId;
	}

	public void setProviderId(long providerId) 
	{
		this.providerId = providerId;
	}

	public long getId() 
	{
		return id;
	}

	public void setId(long id) 
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public void save()
	{
		
	}
}
