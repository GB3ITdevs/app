package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.PhoneNumber;

public class PhoneNumberJSONParser 
{
	
	public static List<PhoneNumber> parseFeed(String content) 
	{
		
		try 
		{
			//Create a JSON Array from the string parsed into the method
			JSONArray ar = new JSONArray(content);
			
			//Create a list of PhoneNumber objects
			List<PhoneNumber> phoneNumberList = new ArrayList<>();
			
			//Loop over each item in the array
			for (int i = 0; i < ar.length(); i++) 
			{
				//Create a new JSON object from the array item
				JSONObject obj = ar.getJSONObject(i);
				
				//Create a new PhoneNumber object
				PhoneNumber phoneNumber = new PhoneNumber();
				
				//Set each JSON PhoneNumber field to the appropriate PhoneNumber object field
				phoneNumber.setPhoneID(obj.getInt("phoneID"));
				phoneNumber.setInfoID(obj.getInt("infoID"));
				phoneNumber.setPhoneNumber(obj.getString("phoneNumber"));
			
				//Add the PhoneNumber object to the list of PhoneNumbers
				phoneNumberList.add(phoneNumber);
			}
		//Return the list of PhoneNumber objects
		return phoneNumberList;	
		} 
		
		//If there is an exception print the stack trace and return a null
		catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	//Method to parse the Android PhoneNumber object to JSON PhoneNumber format in order to post
		public static String POST(PhoneNumber phoneNumber)
		{
			//Create a new empty string
			String json = "";
			
			//Create a new JSON object
			JSONObject jsonPhoneNumber = new JSONObject();
			
			
			try {
					//For each field in the parsed in PhoneNumber object, set it to the appropriate JSON object field
					jsonPhoneNumber.accumulate("infoID", Integer.toString(phoneNumber.getInfoID()));
					jsonPhoneNumber.accumulate("phoneNumber", phoneNumber.getPhoneNumber());

				} 
			//If there is an exception print the stack trace
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
			//Set the string to the JSON object to string method
			json = "{\"phone_number\":" + jsonPhoneNumber.toString() + "}";
			
			//Return the JSON String
			return json;
		}

}
