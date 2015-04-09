package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tyct.thankyoutrust.model.UserID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

		populateFields();

		Button btnUpdate = (Button) findViewById(R.id.buttonUpdateProfile);
		btnUpdate.setOnClickListener(new UpdateBtnHandler());
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

	public class UpdateBtnHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			boolean notEmpty = false;
			
			List<EditText> fields = new ArrayList<>();
			
			// Get Text from each editText field and convert to string
			EditText eFName = (EditText) findViewById(R.id.editTextProfFName);
			fields.add(eFName);
			
			String firstName = eFName.getText().toString();
			if (TextUtils.isEmpty(firstName.trim())) {
				notEmpty = false;
			}

			EditText eLName = (EditText) findViewById(R.id.editTextProfLName);
			fields.add(eLName);
			String lastName = eLName.getText().toString();
			if (TextUtils.isEmpty(lastName.trim())) {
				notEmpty = false;
			}

			EditText eEmail = (EditText) findViewById(R.id.editTextProfEmail);
			fields.add(eEmail);
			String email = eEmail.getText().toString();
			if (TextUtils.isEmpty(email.trim())) {
				notEmpty = false;
			}

			EditText eCity = (EditText) findViewById(R.id.editTextProfCity);
			fields.add(eCity);
			String city = eCity.getText().toString();
			if (TextUtils.isEmpty(city.trim())) {
				notEmpty = false;
			}

			EditText eSuburb = (EditText) findViewById(R.id.editTextProfSuburb);
			fields.add(eSuburb);
			String suburb = eSuburb.getText().toString();
			if (TextUtils.isEmpty(suburb.trim())) {
				notEmpty = false;
			}

			EditText ePostcode = (EditText) findViewById(R.id.editTextProfPostcode);
			fields.add(ePostcode);
			String postcode = ePostcode.getText().toString();
			if (TextUtils.isEmpty(postcode.trim())) {
				notEmpty = false;
			}
			
			for (EditText field : fields) {
				if (TextUtils.isEmpty(field.getText().toString().trim())) {
					notEmpty = true;
				}
			}
			
//			private boolean validate(EditText[] fields){
//		        for(int i=0; i<fields.length; i++){
//		            EditText currentField=fields[i];
//		            if(currentField.getText().toString().length()<=0){
//		                return false;
//		            }
//		        }
//		        return true;
//		}
//
//		boolean fieldsOK=validate(new EditText[]{ev_last_name, ev_first_name, ev_email});

			// If statement, checks to make sure that user has entered anything
			// into the edit text fields
			if (notEmpty = false) {
				// If Edit Text is empty it will show a toast
				Toast.makeText(ProfileActivity.this,
						eFName.getText() + "All fields empty",
						Toast.LENGTH_LONG).show();
			} else {

			}
		}
	}
}
