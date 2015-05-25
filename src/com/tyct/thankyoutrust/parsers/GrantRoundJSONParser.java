package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.GrantRound;

public class GrantRoundJSONParser {

	//Method to parse JSON Grant Round object to the Android Grant Round Object
	public static List<GrantRound> parseFeed(String content) 
	{
		
		try 
		{
			//Create a JSON Array from the string parsed into the method
			JSONArray ar = new JSONArray(content);
			
			//Create a list of Grant Round objects
			List<GrantRound> grantRoundList = new ArrayList<>();
			
			//Loop over each item in the array
			for (int i = 0; i < ar.length(); i++) 
			{
				//Create a new JSON object from the array item
				JSONObject obj = ar.getJSONObject(i);
				
				//Create a new Grant Round object
				GrantRound grantRound = new GrantRound();
				
				//Set each JSON Grant Round field to the appropriate Grant Round object field
				grantRound.setRoundID(obj.getInt("roundID"));	
				grantRound.setCommunityID(obj.getInt("communityID"));	
				grantRound.setEndDate(obj.getString("endDate"));
				grantRound.setStartDate(obj.getString("startDate"));
			
				//Add the Grant Round object to the list of Grant Rounds
				grantRoundList.add(grantRound);
			}
		//Return the list of Grant Round objects
		return grantRoundList;	
		} 
		
		//If there is an exception print the stack trace and return a null
		catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	//Method to parse the Android Grant Round object to JSON Grant Round format in order to post or update
	public static String POST(GrantRound grantRound)
	{
		//Create a new empty string
		String json = "";
		
		//Create a new JSON object
		JSONObject jsonGrantRound = new JSONObject();
		
		
		try {
				//For each field in the parsed in Grant Round object, set it to the appropriate JSON object field
				jsonGrantRound.accumulate("communityID", Integer.toString(grantRound.getCommunityID()));
				jsonGrantRound.accumulate("roundID", Integer.toString(grantRound.getRoundID()));
				jsonGrantRound.accumulate("startDate", grantRound.getStartDate());
				jsonGrantRound.accumulate("endDate", grantRound.getEndDate());
			} 
		//If there is an exception print the stack trace
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		//Set the string to the JSON object to string method
		json = "{\"grant_round\":" + jsonGrantRound.toString() + "}";
		
		//Return the JSON String
		return json;
		
	}

	
}
