package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.AdminID;

public class AdminIDJSONParser 
{
	public static List<AdminID> parseFeed(String content) 
	{
		
		try 
		{
			//Create a JSON Array from the string parsed into the method
			JSONArray ar = new JSONArray(content);
			
			//Create a list of adminID objects
			List<AdminID> adminIDList = new ArrayList<>();
			
			//Loop over each item in the array
			for (int i = 0; i < ar.length(); i++) 
			{
				//Create a new JSON object from the array item
				JSONObject obj = ar.getJSONObject(i);
				
				//Create a new Project object
				AdminID adminID = new AdminID();
				
				//Set each JSON adminID field to the appropriate adminID object field
				adminID.setAdminID(obj.getInt("userID"));
				adminID.setAdminID(obj.getInt("infoID"));
				adminID.setAdminID(obj.getInt("contactID"));
			
				//Add the admin ID object to the list of admin IDs
				adminIDList.add(adminID);
			}
		//Return the list of adminID objects
		return adminIDList;	
		} 
		
		//If there is an exception print the stack trace and return a null
		catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	//Method to parse the Android adminID object to JSON adminID format in order to post
		public static String POST(AdminID adminID)
		{
			//Create a new empty string
			String json = "";
			
			//Create a new JSON object
			JSONObject jsonAdminID = new JSONObject();
			
			
			try {
					//For each field in the parsed in adminID object, set it to the appropriate JSON object field
					jsonAdminID.accumulate("infoID", Integer.toString(adminID.getInfoID()));

				} 
			//If there is an exception print the stack trace
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
			//Set the string to the JSON object to string method
			json = "{\"administrator\":" + jsonAdminID.toString() + "}";
			
			//Return the JSON String
			return json;
		}

}
