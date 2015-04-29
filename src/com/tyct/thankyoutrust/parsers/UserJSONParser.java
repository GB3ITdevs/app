package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.util.ArrayMap;

import com.tyct.thankyoutrust.model.User;

public class UserJSONParser {

	// Retrieve list of users & their associated data from database
	public static List<User> parseFeed(String content) {

		try {
			JSONArray ar = new JSONArray(content);
			List<User> userList = new ArrayList<>();

			for (int i = 0; i < ar.length(); i++) {

				JSONObject obj = ar.getJSONObject(i);
				User user = new User();

				user.setUserID(obj.getInt("userID"));
				user.setCommunityID(obj.getInt("communityID"));
				user.setFirstName(obj.getString("firstName"));
				user.setLastName(obj.getString("lastName"));
				user.setPassword(obj.getString("password"));
				user.setEmail(obj.getString("email"));
				user.setPostalCode(obj.getInt("postalCode"));
				user.setSuburb(obj.getString("suburb"));
				user.setCity(obj.getString("city"));
				user.setStreetAddress(obj.getString("streetAddress"));

				userList.add(user);
			}

			return userList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

	// Post user data to database
	public static String POSTUser(User user) {
		String json = "";

		JSONObject jsonUser = new JSONObject();

		try {

			jsonUser.accumulate("communityID", user.getCommunityID());
			jsonUser.accumulate("firstName", user.getFirstName());
			jsonUser.accumulate("lastName", user.getLastName());
			jsonUser.accumulate("password", user.getPassword());
			jsonUser.accumulate("email", user.getEmail());
			jsonUser.accumulate("postalCode",
					Integer.toString(user.getPostalCode()));
			jsonUser.accumulate("suburb", user.getSuburb());
			jsonUser.accumulate("city", user.getCity());
			jsonUser.accumulate("streetAddress", user.getStreetAddress());

		}

		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		json = "{\"user\":" + jsonUser.toString() + "}";

		return json;

	}

	// Put user data to database
	public static String PUTUser(ArrayMap<String, String> fields) {
		String json = "";
		JSONObject jsonUser = new JSONObject();
		try {
			for (int i = 0; i < fields.size(); i++) {
				jsonUser.accumulate(fields.keyAt(i), fields.valueAt(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json = "{\"person_info\":" + jsonUser.toString() + "}";
		return json;
	}
}