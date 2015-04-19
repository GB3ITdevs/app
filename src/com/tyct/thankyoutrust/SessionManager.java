package com.tyct.thankyoutrust;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor reference for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "UserInfo";

	// All Shared Preferences Keys
	private static final String IS_USER_LOGIN = "IsUserLoggedIn";

	// User id (make variable public to access from outside)
	private static final String KEY_ID = "id";

	// Email address
	public static final String KEY_EMAIL = "email";

	// First and last names
	public static final String KEY_NAME = "fName";
	public static final String KEY_SURNAME = "lName";

	// Address
	public static final String KEY_ADDR = "address";

	// City
	public static final String KEY_CITY = "city";

	// Suburb
	public static final String KEY_SUB = "suburb";

	// Postcode
	public static final String KEY_PCODE = "postcode";
	
	// Admin
	public static final String KEY_ADMIN = "admin";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createUserLoginSession(int id, String email, String name,
			String surname, String suburb, String city,
			String postcode, String admin) {
		// Storing login value as TRUE
		editor.putBoolean(IS_USER_LOGIN, true);

		// Store info id in pref
		String ID = Integer.toString(id);
		editor.putString(KEY_ID, ID);
		// Store email in pref
		editor.putString(KEY_EMAIL, email);
		// Store name in pref
		editor.putString(KEY_NAME, name);
		editor.putString(KEY_SURNAME, surname);
		// Store address in pref
		//TODO editor.putString(KEY_ADDR, address);
		// Store city in pref
		editor.putString(KEY_CITY, city);
		// Store suburb in pref
		editor.putString(KEY_SUB, suburb);
		// Store postcode in pref
		editor.putString(KEY_PCODE, postcode);
		
		// Store padmin in pref
		editor.putString(KEY_ADMIN, admin);
		// commit changes
		editor.commit();
	}

	/**
	 * Update user details
	 * */
	public void updateUserDetails(String fName, String lName, String email,
			String city, String address, String suburb, String postcode,
			String password) {

	}

	/**
	 * Check login method will check user login status If false it will redirect
	 * user to login page Else do anything
	 * */
	public boolean checkLogin() {
		// Check login status
		if (!this.isUserLoggedIn()) {

			// user is not logged in, redirect to Login Activity
			Intent i = new Intent(_context, LoginActivity.class);

			// Closing all the Activities from stack
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);

			return true;
		}
		return false;
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {

		// Use hashmap to store user credentials
		HashMap<String, String> user = new HashMap<String, String>();

		// info id
		user.put(KEY_ID, pref.getString(KEY_ID, null));

		// user email id
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		user.put(KEY_SURNAME, pref.getString(KEY_SURNAME, null));

		// user address
		//TODO user.put(KEY_ADDR, pref.getString(KEY_ADDR, null));

		// user city
		user.put(KEY_CITY, pref.getString(KEY_CITY, null));

		// user suburb
		user.put(KEY_SUB, pref.getString(KEY_SUB, null));

		// user postcode
		user.put(KEY_PCODE, pref.getString(KEY_PCODE, null));
		
		// user admin
		user.put(KEY_ADMIN, pref.getString(KEY_ADMIN, null));

		// return user
		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {

		// Clear all user data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Welcome Screen
		Intent i = new Intent(_context, WelcomeScreen.class);

		// Close all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Start Login Activity
		_context.startActivity(i);
	}

	/**
	 * Check for logged in user
	 * */
	public boolean isUserLoggedIn() {
		return pref.getBoolean(IS_USER_LOGIN, false);
	}
}