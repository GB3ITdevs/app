package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.ProjectRating;

//Class to parse JSON Project Rating data to and from the Android Project Rating object
public class ProjectRatingsJSONParser 
{
		//Method to parse JSON Project Rating object to the Android Project Object
		public static List<ProjectRating> parseFeed(String content) 
		{
			
			try 
			{
				//Create a JSON Array from the string parsed into the method
				JSONArray ar = new JSONArray(content);
				
				//Create a list of Project Rating objects
				List<ProjectRating> projectRatingList = new ArrayList<>();
				
				//Loop over each item in the array
				for (int i = 0; i < ar.length(); i++) 
				{
					//Create a new JSON object from the array item
					JSONObject obj = ar.getJSONObject(i);
					
					//Create a new Project Rating object
					ProjectRating rating = new ProjectRating();
					
					//Set each JSON Project Rating field to the appropriate Project Rating object field
					rating.setRatingID(obj.getInt("ratingID"));
					rating.setProjectID(obj.getInt("projectID"));
					rating.setUserID(obj.getInt("userID"));
					rating.setRating(obj.getInt("rating"));
				
					//Add the Project Rating object to the list of Project Ratings
					projectRatingList.add(rating);
				}
			//Return the list of Project Rating objects
			return projectRatingList;	
			} 
			
			//If there is an exception print the stack trace and return a null
			catch (JSONException e) 
			{
				e.printStackTrace();
				return null;
			}
		}
	
		//Method to parse the Android Project Rating object to JSON Project Rating format in order to post
		public static String POST(ProjectRating rating)
		{
			//Create a new empty string
			String json = "";
			
			//Create a new JSON object
			JSONObject jsonProjectRating = new JSONObject();
			
			
			try {
					//For each field in the parsed in Project object, set it to the appropriate JSON object field
					jsonProjectRating.accumulate("projectID", Integer.toString(rating.getProjectID()));
					jsonProjectRating.accumulate("userID", Integer.toString(rating.getUserID()));
					jsonProjectRating.accumulate("rating", Integer.toString(rating.getRating()));
				} 
			//If there is an exception print the stack trace
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
			//Set the string to the JSON object to string method
			json = "{\"rating\":" + jsonProjectRating.toString() + "}";
			
			//Return the JSON String
			return json;
			
		}

}
