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
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// Session class instance
        session = new SessionManager(getApplicationContext());
        
        Button update = (Button) findViewById(R.id.buttonUpdateProfile);
        
        populateFields();
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
		if (item.getItemId() == R.id.action_projects) 
		{
			goTo = new Intent(ProfileActivity.this, Projects.class);
			
		}
		if (item.getItemId() == R.id.action_home) 
		{
			goTo = new Intent(ProfileActivity.this, MainActivity.class);
		}
		if (item.getItemId() == R.id.action_profile) 
		{
			goTo = new Intent(ProfileActivity.this, ProfileActivity.class);
		}
		if (item.getItemId() == R.id.action_about_us) 
		{
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
        
        EditText eFName = (EditText) findViewById(R.id.editTextProfFName);
        EditText eLName = (EditText) findViewById(R.id.editTextProfLName);
        EditText eEmail = (EditText) findViewById(R.id.editTextProfEmail);
        EditText eCity = (EditText) findViewById(R.id.editTextProfCity);
        EditText eSuburb = (EditText) findViewById(R.id.editTextProfSuburb);
        EditText ePostcode = (EditText) findViewById(R.id.editTextProfPostcode);
        
        // get user data from session
        HashMap<String, String> userStored = session.getUserDetails();
        
        // Display name
        String userName = userStored.get("fName");
        // If the user's name exists in db
        if (userName != null) {
        	// Get user's surname
        	String surname = userStored.get("lName");
        	// Concatenate user's names into a single string
        	pName.setText(userName + " " + surname);
		}
        
        // Display email address
        String userEmail = userStored.get("email");
        pEmail.setText(userEmail);
        
        // get user id
        int userId = Integer.parseInt(userStored.get("id"));
	}
}
