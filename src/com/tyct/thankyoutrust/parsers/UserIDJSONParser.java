package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.UserID;

public class UserIDJSONParser 
{

	public static List<UserID> parseFeed(String content) 
	{
		
		try 
		{
			//Create a JSON Array from the string parsed into the method
			JSONArray ar = new JSONArray(content);
			
			//Create a list of user ID objects
			List<UserID> userIDList = new ArrayList<>();
			
			//Loop over each item in the array
			for (int i = 0; i < ar.length(); i++) 
			{
				//Create a new JSON object from the array item
				JSONObject obj = ar.getJSONObject(i);
				
				//Create a new Project object
				UserID userID = new UserID();
				
				//Set each JSON user ID field to the appropriate user ID object field
				userID.setUserID(obj.getInt("userID"));
				userID.setInfoID(obj.getInt("infoID"));
				userID.setContactID(obj.getInt("contactID"));
			
				//Add the user ID object to the list of Projects
				userIDList.add(userID);
			}
		//Return the list of user ID objects
		return userIDList;	
		} 
		
		//If there is an exception print the stack trace and return a null
		catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	//Method to parse the Android user ID object to JSON user ID format in order to post
		public static String POST(UserID userID)
		{
			//Create a new empty string
			String json = "";
			
			//Create a new JSON object
			JSONObject jsonUserID = new JSONObject();
			
			
			try {
					//For each field in the parsed in user ID object, set it to the appropriate JSON object field
					jsonUserID.accumulate("infoID", Integer.toString(userID.getInfoID()));
					jsonUserID.accumulate("contactID", Integer.toString(userID.getContactID()));

				} 
			//If there is an exception print the stack trace
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
			//Set the string to the JSON object to string method
			json = "{\"user\":" + jsonUserID.toString() + "}";
			
			//Return the JSON String
			return json;
		}
}
