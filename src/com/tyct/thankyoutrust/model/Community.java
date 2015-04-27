package com.tyct.thankyoutrust.model;

public class Community 
{
	private int communityID;
	private int postalCode;
	private String communityName;
	
	
	public int getPostalCode() 
	{
		return postalCode;
	}
	
	public void setPostalCode(int postalCode) 
	{
		this.postalCode = postalCode;
	}
	
	
	
	public String getCommunityName() 
	{
		return communityName;
	}
	
	public void setCommunityName(String communityName) 
	{
		this.communityName = communityName;
	}

	
	
	public int getCommunityID() {
		return communityID;
	}

	public void setCommunityID(int communityID) {
		this.communityID = communityID;
	}
	
	
}
