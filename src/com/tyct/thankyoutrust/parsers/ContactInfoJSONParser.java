package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.ContactInfo;

public class ContactInfoJSONParser 
{

	public static List<ContactInfo> parseFeed(String content) 
	{
		
		try 
		{
			//Create a JSON Array from the string parsed into the method
			JSONArray ar = new JSONArray(content);
			
			//Create a list of ContactInfo objects
			List<ContactInfo> contactInfoList = new ArrayList<>();
			
			//Loop over each item in the array
			for (int i = 0; i < ar.length(); i++) 
			{
				//Create a new JSON object from the array item
				JSONObject obj = ar.getJSONObject(i);
				
				//Create a new ContactInfo object
				ContactInfo contactInfo = new ContactInfo();
				
				//Set each JSON ContactInfo field to the appropriate ContactInfo object field
				contactInfo.setContactID(obj.getInt("contactID"));
				contactInfo.setPostalCode(obj.getInt("postalCode"));
				contactInfo.setSuburb(obj.getString("suburb"));
				contactInfo.setCity(obj.getString("city"));
				contactInfo.setStreetNumber(obj.getInt("streetNumber"));
				contactInfo.setStreet(obj.getString("street"));
			
				//Add the ContactInfo object to the list of ContactInfos
				contactInfoList.add(contactInfo);
			}
		//Return the list of ContactInfo objects
		return contactInfoList;	
		} 
		
		//If there is an exception print the stack trace and return a null
		catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	//Method to parse the Android ContactInfo object to JSON ContactInfo format in order to post
		public static String POST(ContactInfo contactInfo)
		{
			//Create a new empty string
			String json = "";
			
			//Create a new JSON object
			JSONObject jsonContactInfo = new JSONObject();
			
			
			try {
					//For each field in the parsed in ContactInfo object, set it to the appropriate JSON object field
					jsonContactInfo.accumulate("postalCode", Integer.toString(contactInfo.getPostalCode()));
					jsonContactInfo.accumulate("suburb", contactInfo.getSuburb());
					jsonContactInfo.accumulate("city", contactInfo.getCity());
					jsonContactInfo.accumulate("streetNumber", Integer.toString(contactInfo.getStreetNumber()));
					jsonContactInfo.accumulate("street", contactInfo.getStreet());

				} 
			//If there is an exception print the stack trace
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
			//Set the string to the JSON object to string method
			json = "{\"contact_info\":" + jsonContactInfo.toString() + "}";
			
			//Return the JSON String
			return json;
		}

}
