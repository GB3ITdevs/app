package com.tyct.thankyoutrust;

import java.util.List;

import com.tyct.thankyoutrust.model.ProjectRating;

public class AdministrativeCalculations 
{
	
	public int CalculateAverageRating(List<ProjectRating> ratingList, int projectID)
	{
		int totalOfRatings = 0;
		int ratingsSubmitted = 0;
		int average = 0;
		
		for (ProjectRating rating : ratingList)
		{
			if((rating.getProjectID() == projectID))
			{
				int currRating = rating.getRating();
				totalOfRatings = totalOfRatings + currRating;
				ratingsSubmitted++;
			}
		}
		
		if(ratingsSubmitted != 0)
		{
			average = totalOfRatings/ratingsSubmitted;		
		}
		return average;
	}

}
