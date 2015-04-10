package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

		// Set Edit Info button
		Button editInfo = (Button) findViewById(R.id.btnProfEditInfo);
		editInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				infoDialog();
			}
		});

		// Set Edit Address button
		Button editAddr = (Button) findViewById(R.id.btnProfEditLocation);
		editAddr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				addressDialog();
			}
		});

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

	/**
	 * Shows pop-up dialog for user to edit their personal details
	 */
	public void infoDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ProfileActivity.this);

		LayoutInflater factory = LayoutInflater.from(this);

		// edit_info is a Layout XML file containing text fields to display in
		// alert dialog
		final View textEntryView = factory.inflate(R.layout.edit_info, null);

		// Get each editText field
		final EditText eFName = (EditText) textEntryView
				.findViewById(R.id.editTextEditFName);
		final EditText eLName = (EditText) textEntryView
				.findViewById(R.id.editTextEditLName);
		final EditText eEmail = (EditText) textEntryView
				.findViewById(R.id.editTextEditEmail);

		// Display name
		if (userStored.get("fName") != null) {
			String userName = userStored.get("fName");
			eFName.setText(userName);
		}

		// Display surname
		if (userStored.get("fName") != null) {
			String surname = userStored.get("lName");
			eLName.setText(surname);
		}

		// Display email address
		if (userStored.get("email") != null) {
			String userEmail = userStored.get("email");
			eEmail.setText(userEmail);
		}

		// set title
		alertDialogBuilder.setTitle("Edit Personal Details");

		// set dialog message
		alertDialogBuilder
				.setView(textEntryView)
				// .setMessage(
				// "Leave a field blank if you do not wish to make any changes to it")
				.setCancelable(false)
				.setPositiveButton("Submit Changes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked,
								// update details

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	/**
	 * Shows pop-up dialog for user to edit their address details
	 */
	public void addressDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ProfileActivity.this);

		LayoutInflater factory = LayoutInflater.from(this);

		// edit_info is a Layout XML file containing text fields to display in
		// alert dialog
		final View textEntryView = factory.inflate(R.layout.edit_address, null);

		// Get each editText field
		final EditText eAddress = (EditText) findViewById(R.id.editTextEditAddress);
		final EditText eSuburb = (EditText) findViewById(R.id.editTextEditSuburb);
		final EditText eCity = (EditText) findViewById(R.id.editTextEditCity);
		final EditText ePostcode = (EditText) findViewById(R.id.editTextEditPostcode);

		// Display address (pending  addition to session manager)
//		if (userStored.get("address") != null) {
//			String address = userStored.get("address");
//			eAddress.setText(address);
//		}

		// Display suburb
		if (userStored.get("suburb") != null) {
			String suburb = userStored.get("suburb");
			eSuburb.setText(suburb);
		}

		// Display city
		if (userStored.get("city") != null) {
			String city = userStored.get("city");
			eCity.setText(city);
		}

		// Display postcode
		if (userStored.get("postcode") != null) {
			String postcode = userStored.get("postcode");
			ePostcode.setText(postcode);
		}

		// set title
		alertDialogBuilder.setTitle("Edit Address Details");

		// set dialog message
		alertDialogBuilder
				.setView(textEntryView)
				// .setMessage(
				// "Leave a field blank if you do not wish to make any changes to it")
				.setCancelable(false)
				.setPositiveButton("Submit Changes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked,
								// update details

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public class UpdateBtnHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			boolean empty = true;

			List<EditText> fields = new ArrayList<>();

			// Get each editText field
			EditText eFName = (EditText) findViewById(R.id.editTextEditFName);
			fields.add(eFName);

			EditText eLName = (EditText) findViewById(R.id.editTextEditLName);
			fields.add(eLName);

			EditText eEmail = (EditText) findViewById(R.id.editTextEditEmail);
			fields.add(eEmail);

			EditText eCity = (EditText) findViewById(R.id.editTextProfCity);
			fields.add(eCity);

			EditText eSuburb = (EditText) findViewById(R.id.editTextProfSuburb);
			fields.add(eSuburb);

			EditText ePostcode = (EditText) findViewById(R.id.editTextProfPostcode);
			fields.add(ePostcode);

			// Check whether any fields have been filled
			for (EditText field : fields) {
				if (!TextUtils.isEmpty(field.getText().toString().trim())) {
					empty = false;
				}
			}

			// If statement, checks to make sure that user has entered anything
			// into the edit text fields
			if (empty == true) {
				// If Edit Text is empty it will show a toast
				Toast.makeText(ProfileActivity.this,
						eFName.getText() + "All fields empty",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(ProfileActivity.this, "noice1m8",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
