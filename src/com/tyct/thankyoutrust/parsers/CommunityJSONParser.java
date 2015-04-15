package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.Community;

public class CommunityJSONParser 
{
	
	public static List<Community> parseFeed(String content) 
	{
		
		try 
		{
			//Create a JSON Array from the string parsed into the method
			JSONArray ar = new JSONArray(content);
			
			//Create a list of Community objects
			List<Community> communityList = new ArrayList<>();
			
			//Loop over each item in the array
			for (int i = 0; i < ar.length(); i++) 
			{
				//Create a new JSON object from the array item
				JSONObject obj = ar.getJSONObject(i);
				
				//Create a new Community object
				Community community = new Community();
				
				//Set each JSON Community field to the appropriate Community object field
				community.setPostalCode(obj.getInt("postalCode"));
				community.setCommunityName(obj.getString("communityName"));
			
				//Add the Community object to the list of Communities
				communityList.add(community);
			}
		//Return the list of Community objects
		return communityList;	
		} 
		
		//If there is an exception print the stack trace and return a null
		catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	//Method to parse the Android Community object to JSON Community format in order to post
		public static String POST(Community community)
		{
			//Create a new empty string
			String json = "";
			
			//Create a new JSON object
			JSONObject jsonCommunity = new JSONObject();
			
			
			try {
					//For each field in the parsed in Community object, set it to the appropriate JSON object field
					jsonCommunity.accumulate("postalCode", Integer.toString(community.getPostalCode()));
					jsonCommunity.accumulate("communityName", community.getCommunityName());

				} 
			//If there is an exception print the stack trace
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
			//Set the string to the JSON object to string method
			json = "{\"community\":" + jsonCommunity.toString() + "}";
			
			//Return the JSON String
			return json;
		}

}
