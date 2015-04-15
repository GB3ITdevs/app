package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.Project;

//Class to parse JSON Project data to and from the Android Project object
public class ProjectsJSONParser 
{
	//Method to parse JSON Project object to the Android Project Object
	public static List<Project> parseFeed(String content) 
	{
		
		try 
		{
			//Create a JSON Array from the string parsed into the method
			JSONArray ar = new JSONArray(content);
			
			//Create a list of Project objects
			List<Project> projectList = new ArrayList<>();
			
			//Loop over each item in the array
			for (int i = 0; i < ar.length(); i++) 
			{
				//Create a new JSON object from the array item
				JSONObject obj = ar.getJSONObject(i);
				
				//Create a new Project object
				Project project = new Project();
				
				//Set each JSON Project field to the appropriate Project object field
				project.setProjectID(obj.getInt("projectID"));
				project.setApplicantName(obj.getString("applicantName"));
				project.setFundsRequested(obj.getInt("fundsRequested"));
				project.setPostalCode(obj.getInt("postalCode"));
				project.setProjectBlurb(obj.getString("projectBlurb"));
				project.setProjectName(obj.getString("projectName"));
				project.setUseOfFunds(obj.getString("useOfFunds"));
			
				//Add the Project object to the list of Projects
				projectList.add(project);
			}
		//Return the list of Project objects
		return projectList;	
		} 
		
		//If there is an exception print the stack trace and return a null
		catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	//Method to parse the Android Project object to JSON Project format in order to post or update
	public static String POST(Project project)
	{
		//Create a new empty string
		String json = "";
		
		//Create a new JSON object
		JSONObject jsonProject = new JSONObject();
		
		
		try {
				//For each field in the parsed in Project object, set it to the appropriate JSON object field
				jsonProject.accumulate("projectBlurb", project.getProjectBlurb());
				jsonProject.accumulate("fundsRequested", Integer.toString(project.getFundsRequested()));
				jsonProject.accumulate("useOfFunds", project.getUseOfFunds());
				jsonProject.accumulate("applicantName", project.getApplicantName());
				jsonProject.accumulate("projectName", project.getProjectName());
				jsonProject.accumulate("postalCode", Integer.toString(project.getPostalCode()));
			} 
		//If there is an exception print the stack trace
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		//Set the string to the JSON object to string method
		json = "{\"project\":" + jsonProject.toString() + "}";
		
		//Return the JSON String
		return json;
		
	}

}

