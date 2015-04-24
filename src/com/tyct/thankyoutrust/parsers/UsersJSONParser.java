package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.util.ArrayMap;

import com.tyct.thankyoutrust.model.Users;

public class UsersJSONParser {

	// Retrieve list of users & their associated data from database
	public static List<Users> parseFeed(String content) {

		try {
			JSONArray ar = new JSONArray(content);
			List<Users> userList = new ArrayList<>();

			for (int i = 0; i < ar.length(); i++) {

				JSONObject obj = ar.getJSONObject(i);
				Users user = new Users();

				user.setInfoID(obj.getInt("infoID"));
				user.setFirstName(obj.getString("firstName"));
				user.setLastName(obj.getString("lastName"));
				user.setPassword(obj.getString("password"));
				user.setEmail(obj.getString("email"));

				userList.add(user);
			}

			return userList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

	// Post user data to database
	public static String POSTUsers(Users user) {
		String json = "";

		JSONObject jsonUser = new JSONObject();

		try {
			jsonUser.accumulate("firstName", user.getFirstName());
			jsonUser.accumulate("lastName", user.getLastName());
			jsonUser.accumulate("password", user.getPassword());
			jsonUser.accumulate("email", user.getEmail());
		}

		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		json = "{\"person_info\":" + jsonUser.toString() + "}";

		return json;

	}

	// Put user data to database
	public static String PUTUsers(ArrayMap<String, String> fields) {
		String json = "";

		JSONObject jsonUser = new JSONObject();

		try {
			for (int i = 0; i < fields.size(); i++) {
				jsonUser.accumulate(fields.keyAt(i), fields.valueAt(i));
			}
			
		}

		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		json = "{\"person_info\":" + jsonUser.toString() + "}";

		return json;

	}
}