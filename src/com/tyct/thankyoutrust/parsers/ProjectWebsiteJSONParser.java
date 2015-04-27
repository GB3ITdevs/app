package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.ProjectWebsite;

public class ProjectWebsiteJSONParser 
{
	public static List<ProjectWebsite> parseFeed(String content) 
	{
		
		try 
		{
			//Create a JSON Array from the string parsed into the method
			JSONArray ar = new JSONArray(content);
			
			//Create a list of ProjectWebsite objects
			List<ProjectWebsite> projectWebsiteList = new ArrayList<>();
			
			//Loop over each item in the array
			for (int i = 0; i < ar.length(); i++) 
			{
				//Create a new JSON object from the array item
				JSONObject obj = ar.getJSONObject(i);
				
				//Create a new Project object
				ProjectWebsite projectWebsite = new ProjectWebsite();
				
				//Set each JSON adminID field to the appropriate adminID object field
				projectWebsite.setWebsiteID(obj.getInt("websiteID"));
				projectWebsite.setProjectID(obj.getInt("projectID"));
				projectWebsite.setSiteAddress(obj.getString("siteAddress"));
			
				//Add the ProjectWebsite object to the list of ProjectWebsites
				projectWebsiteList.add(projectWebsite);
			}
		//Return the list of ProjectWebsite objects
		return projectWebsiteList;	
		} 
		
		//If there is an exception print the stack trace and return a null
		catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	//Method to parse the Android ProjectWebsite object to JSON ProjectWebsite format in order to post
		public static String POST(ProjectWebsite projectWebsite)
		{
			//Create a new empty string
			String json = "";
			
			//Create a new JSON object
			JSONObject jsonProjectWebsite = new JSONObject();
			
			
			try {
					//For each field in the parsed in ProjectWebsite object, set it to the appropriate JSON object field
					jsonProjectWebsite.accumulate("projectID", Integer.toString(projectWebsite.getProjectID()));
					jsonProjectWebsite.accumulate("siteAddress", projectWebsite.getSiteAddress());

				} 
			//If there is an exception print the stack trace
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
			//Set the string to the JSON object to string method
			json = "{\"project_website\":" + jsonProjectWebsite.toString() + "}";
			
			//Return the JSON String
			return json;
		}

}
