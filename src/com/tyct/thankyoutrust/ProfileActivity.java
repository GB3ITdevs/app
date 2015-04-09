package com.tyct.thankyoutrust;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileActivity extends Activity {

	// Session Manager Class
	SessionManager session;

	// Stored session data
	HashMap<String, String> userStored;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		// get user data from session
		userStored = session.getUserDetails();

		Button update = (Button) findViewById(R.id.buttonUpdateProfile);

		populateFields();

		EditText eFName = (EditText) findViewById(R.id.editTextProfFName);
		EditText eLName = (EditText) findViewById(R.id.editTextProfLName);
		EditText eEmail = (EditText) findViewById(R.id.editTextProfEmail);
		EditText eCity = (EditText) findViewById(R.id.editTextProfCity);
		EditText eSuburb = (EditText) findViewById(R.id.editTextProfSuburb);
		EditText ePostcode = (EditText) findViewById(R.id.editTextProfPostcode);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent goTo = new Intent();
		if (item.getItemId() == R.id.action_projects) {
			goTo = new Intent(ProfileActivity.this, Projects.class);

		}
		if (item.getItemId() == R.id.action_home) {
			goTo = new Intent(ProfileActivity.this, MainActivity.class);
		}
		if (item.getItemId() == R.id.action_profile) {
			goTo = new Intent(ProfileActivity.this, ProfileActivity.class);
		}
		if (item.getItemId() == R.id.action_about_us) {
			goTo = new Intent(ProfileActivity.this, AboutUs.class);
		}
		startActivity(goTo);
		return false;
	}

	private void populateFields() {
		// Get references
		TextView pName = (TextView) findViewById(R.id.textViewProfName);
		TextView pEmail = (TextView) findViewById(R.id.textViewProfEmail);
		TextView pLocation = (TextView) findViewById(R.id.textViewProfLocation);

		// Display name
		if (userStored.get("fName") != null) {
			String userName = userStored.get("fName");
			String surname = userStored.get("lName");
			// Concatenate names into a single string
			pName.setText(userName + " " + surname);
		}

		// Display email address
		if (userStored.get("email") != null) {
			String userEmail = userStored.get("email");
			pEmail.setText(userEmail);
		}

		// Display location
		if (userStored.get("suburb") != null) {
			String suburb = userStored.get("suburb");
			String city = userStored.get("city");
			String postcode = userStored.get("postcode");
			// Concatenate names into a single string
			pLocation.setText(suburb + ", " + city + "\n" + postcode);
		}
	}
}
