package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.util.ArrayMap;

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
				project.setFundsBlurb(obj.getString("fundsBlurb"));
				project.setRoundID(obj.getInt("roundID"));
				project.setProjectBlurb(obj.getString("projectBlurb"));
				project.setApplicantEmail(obj.getString("applicantEmail"));
				project.setOrganizationBlurb(obj.getString("organizationBlurb"));
				project.setStatus(obj.getString("status"));
			
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
				jsonProject.accumulate("fundsBlurb", project.getFundsBlurb());
				jsonProject.accumulate("organizationBlurb", project.getOrganizationBlurb());
				jsonProject.accumulate("applicantName", project.getApplicantName());
				jsonProject.accumulate("applicantEmail", project.getApplicantEmail());
				jsonProject.accumulate("roundID", Integer.toString(project.getRoundID()));
				jsonProject.accumulate("status", project.getStatus());
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
	
	// Update project data in the database
	public static String PUTProject(ArrayMap<String, String> fields) {
		String json = "";
		JSONObject jsonProject = new JSONObject();
		try {
			for (int i = 0; i < fields.size(); i++) {
				jsonProject.accumulate(fields.keyAt(i), fields.valueAt(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json = "{\"project\":" + jsonProject.toString() + "}";
		return json;
	}

}

