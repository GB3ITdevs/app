package com.tyct.thankyoutrust;

import java.util.HashMap;
import java.util.List;

import com.tyct.thankyoutrust.LoginActivity.UserLoginTask;
import com.tyct.thankyoutrust.model.Users;
import com.tyct.thankyoutrust.parsers.UsersJSONParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

	// Async Tasks
	List<UsersTask> tasks;

	// retrieved user list
	List<Users> userList;

	// Session Manager Class
	SessionManager session;

	// Stored session data
	HashMap<String, String> userStored;

	// User info id
	int infoId;

	// Get value from each editText field
	EditText tCurrentPw;
	String pw;

	EditText tNewPw;
	String newPw;

	EditText tNewPwCheck;
	String newPwCheck;

	/**
	 * Keep track of the task to ensure we can cancel it if requested.
	 */
	private UpdatePwTask mAuthTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		// Get user data from session
		userStored = session.getUserDetails();

		// get user info id
		if (userStored.get("id") != null) {
			infoId = Integer.parseInt(userStored.get("id"));
		}

		// Populate data at top of screen
		populateFields();

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

		// Set Change Password button
		Button editPw = (Button) findViewById(R.id.btnChangePassword);
		editPw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				pwDialog();
			}
		});
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

	// Populate the fields at the top of the screen
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
		String location = "";
		// TODO if (userStored.get("address") != null) {
		// String address = userStored.get("address");
		// // If exists, append to location
		// location += address;
		// }
		if (userStored.get("suburb") != null) {
			String suburb = userStored.get("suburb");
			// If exists, append to location
			location += suburb;
		}
		if (userStored.get("city") != null) {
			// If exists, append to location
			String city = userStored.get("city");
			location += city;
		}
		if (userStored.get("postcode") != null) {
			// If exists, append to location
			String postcode = userStored.get("postcode");
			location += postcode;
		}
		pLocation.setText(location);
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

		// edit_address is a Layout XML file containing text fields to display
		// in alert dialog
		final View textEntryView = factory.inflate(R.layout.edit_address, null);

		// Get each editText field
		final EditText eAddress = (EditText) findViewById(R.id.editTextEditAddress);
		final EditText eSuburb = (EditText) findViewById(R.id.editTextEditSuburb);
		final EditText eCity = (EditText) findViewById(R.id.editTextEditCity);
		final EditText ePostcode = (EditText) findViewById(R.id.editTextEditPostcode);

		// Display address (TODO pending addition to session manager)
		// if (userStored.get("address") != null) {
		// String address = userStored.get("address");
		// eAddress.setText(address);
		// }

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

	/**
	 * Shows pop-up dialog to allow user to change their password
	 */
	public void pwDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ProfileActivity.this);

		LayoutInflater factory = LayoutInflater.from(this);

		// change_pw is a Layout XML file containing text fields to display in
		// alert dialog
		View textEntryView = factory.inflate(R.layout.change_pw, null);

		// Get each editText field
		tCurrentPw = (EditText) findViewById(R.id.editTextEditAddress);
		tNewPw = (EditText) findViewById(R.id.editTextEditSuburb);
		tNewPwCheck = (EditText) findViewById(R.id.editTextEditCity);

		// set title
		alertDialogBuilder.setTitle("Change Password");

		// set dialog message
		alertDialogBuilder
				.setView(textEntryView)
				.setCancelable(false)
				.setPositiveButton("Submit Changes",
						new DialogInterface.OnClickListener() {
							// If this button is clicked, update password
							public void onClick(DialogInterface dialog, int id) {
								// Reset errors.
								tCurrentPw.setError(null);
								tNewPw.setError(null);
								tNewPwCheck.setError(null);

								// Deal with input errors
								boolean cancel = false;
								View focusView = null;

								// Get values from the EditText fields
								pw = tCurrentPw.getText().toString();
								newPw = tNewPw.getText().toString();
								newPwCheck = tNewPwCheck.getText().toString();

								// Check current password is correct
								for (Users user : userList) {
									if (user.getInfoID() == (infoId)) {
										// Check if the current password matches stored password
										if (user.getPassword().equals(pw)) {
											tCurrentPw
													.setError(getString(R.string.error_incorrect_password));
											focusView = tCurrentPw;
											cancel = true;
										}
									}
								}

								// Check new password matches re-entered password
								if (!newPw.equals(newPwCheck)) {
									tNewPwCheck
											.setError(getString(R.string.error_nonmatching_password));
									focusView = tNewPwCheck;
									cancel = true;
								}

								// Check for a valid password, if the user entered one.
								if (!TextUtils.isEmpty(newPw)
										&& !isPasswordValid(newPw)) {
									tNewPw.setError(getString(R.string.error_invalid_password));
									focusView = tNewPw;
									cancel = true;
								}

								if (cancel) {
									// There was an error; don't attempt login
									// and focus the first form field with an error.
									focusView.requestFocus();
								} else {
									// Show a progress spinner, and kick off a
									// background task to perform the user login attempt.
									// showProgress(true);
									mAuthTask = new UpdatePwTask(newPw);
									mAuthTask.execute((Void) null);
								}
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

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 3;
	}

	/**
	 * Represents an asynchronous task used to retrieve user data from person
	 * info.
	 */
	private class UsersTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			tasks.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			userList = UsersJSONParser.parseFeed(result);
			tasks.remove(this);
		}
	}

	/**
	 * Async task used to send the updated password to the database
	 */
	private class UpdatePwTask extends AsyncTask<Void, Void, Boolean> {

		private final String mPassword;

		public UpdatePwTask(String newPw) {
			mPassword = newPw;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			// update details here
			// TODO pending PUT access
			// HttpManager.putData("http://gb3it.pickworth.info:3000/person_infos",
			// mPassword);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			mAuthTask = null;
			// showProgress(false);
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			// showProgress(false);
		}
	}
}
