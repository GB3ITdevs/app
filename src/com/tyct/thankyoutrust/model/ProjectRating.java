package com.tyct.thankyoutrust.model;

public class ProjectRating 
{
	private int ratingID;
	private int projectID;
	private int infoID;
	private int rating;

	public int getRatingID() 
	{
		return ratingID;
	}
	
	public void setRatingID(int ratingID) 
	{
		this.ratingID = ratingID;
	}
	
	public int getProjectID() 
	{
		return projectID;
	}
	
	public void setProjectID(int projectID) 
	{
		this.projectID = projectID;
	}
	
	public int getInfoID() 
	{
		return infoID;
	}
	
	public void setInfoID(int infoID) 
	{
		this.infoID = infoID;
	}
	
	public int getRating() 
	{
		return rating;
	}
	
	public void setRating(int rating) 
	{
		this.rating = rating;
	}

}
