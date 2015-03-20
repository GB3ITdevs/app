package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.Project;

public class ProjectsJSONParser {
	public static List<Project> parseFeed(String content) {
		
		try {
			JSONArray ar = new JSONArray(content);
			List<Project> projectList = new ArrayList<>();
			
			for (int i = 0; i < ar.length(); i++) {
				
				JSONObject obj = ar.getJSONObject(i);
				Project project = new Project();
				
				project.setProjectID(obj.getInt("projectID"));
				project.setApplicantName(obj.getString("applicantName"));
				project.setFundsRequested(obj.getInt("fundsRequested"));
				project.setPostalCode(obj.getInt("postalCode"));
				project.setProjectBlurb(obj.getString("projectBlurb"));
				project.setProjectName(obj.getString("projectName"));
				project.setUseOfFunds(obj.getString("useOfFunds"));
				
			
				projectList.add(project);
				
			}
			
			return projectList;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String POST(Project project)
	{
		String json = "";
		
		JSONObject jsonProject = new JSONObject();
		
		
		try {
				//jsonProject.accumulate("projectID", project.getProjectID());
				//jsonProject.accumulate("postalCode", Integer.toString(project.getPostalCode()));
				//jsonProject.accumulate("applicantName", project.getApplicantName());
				//jsonProject.accumulate("projectName", project.getProjectName());
				//jsonProject.accumulate("projectBlurb", project.getProjectBlurb());
				//jsonProject.accumulate("fundsRequested", Integer.toString(project.getFundsRequested()));
				//jsonProject.accumulate("useOfFunds", project.getUseOfFunds());

				jsonProject.accumulate("projectBlurb", project.getProjectBlurb());
				jsonProject.accumulate("fundsRequested", Integer.toString(project.getFundsRequested()));
				jsonProject.accumulate("useOfFunds", project.getUseOfFunds());
				jsonProject.accumulate("applicantName", project.getApplicantName());
				jsonProject.accumulate("projectName", project.getProjectName());
				jsonProject.accumulate("postalCode", Integer.toString(project.getPostalCode()));
			} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		json = "'{\"project\":" + jsonProject.toString() + "}'";
		
		json  = "'{\"project\": {\"postalCode\":\"9001\", \"applicantName\":\"Joel\", \"projectName\":\"Testing\", \"projectBlurb\":\"some project info\", \"fundsRequested\":\"100\", \"useOfFunds\":\"help people\"}}'";
		
		return json;
		
	}

}

