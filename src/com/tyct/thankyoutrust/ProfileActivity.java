package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tyct.thankyoutrust.model.Users;
import com.tyct.thankyoutrust.parsers.UsersJSONParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
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

	/**
	 * Keep track of the tasks to ensure we can cancel it if requested.
	 */
	private UpdatePwTask mPwTask = null;
	private UpdatePersonInfo mPersonTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// set session manager
		updateSession();

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

		tasks = new ArrayList<>();
		personInfo();
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
		switch (item.getItemId()) {
		case R.id.action_projects:
			goTo = new Intent(ProfileActivity.this, Projects.class);
			startActivity(goTo);
			return true;
		case R.id.action_home:
			goTo = new Intent(ProfileActivity.this, MainActivity.class);
			startActivity(goTo);
			return true;
		case R.id.action_profile:
			goTo = new Intent(ProfileActivity.this, ProfileActivity.class);
			startActivity(goTo);
			finish();
			return true;
		case R.id.action_about_us:
			goTo = new Intent(ProfileActivity.this, AboutUs.class);
			startActivity(goTo);
			return true;
		case R.id.action_logout:
			session.logoutUser();
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	private void requestData(String uri) {
		UsersTask task = new UsersTask();
		task.execute(uri);
	}

	public void personInfo() {
		if (isOnline()) {
			requestData("http://gb3it.pickworth.info:3000/person_infos");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
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
		final EditText eiPassw = (EditText) textEntryView
				.findViewById(R.id.editTextInfoPw);

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
		alertDialogBuilder.setView(textEntryView).setCancelable(false)
				.setPositiveButton("Save Changes", null)
				.setNegativeButton("Cancel", null);

		// create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				Button positive = alertDialog
						.getButton(AlertDialog.BUTTON_POSITIVE);
				positive.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// Reset errors.
						eFName.setError(null);
						eLName.setError(null);
						eEmail.setError(null);
						eiPassw.setError(null);

						// Deal with input errors
						boolean cancel = false;
						View focusView = null;

						// Get values from the EditText fields
						String fName = eFName.getText().toString();
						String lName = eLName.getText().toString();
						String email = eEmail.getText().toString();
						String pw = eiPassw.getText().toString();

						// Check current password is correct
						for (Users user : userList) {
							if (user.getInfoID() == (infoId)) {
								// Check if the current password matches stored
								// password
								if (!user.getPassword().equals(pw)) {
									eiPassw.setError(getString(R.string.error_incorrect_password));
									focusView = eiPassw;
									cancel = true;
								}
								break;
							}
						}

						// Check for a valid email
						if (!isEmailValid(email)) {
							eiPassw.setError(getString(R.string.error_invalid_email));
							focusView = eiPassw;
							cancel = true;
						}

						if (cancel) {
							// There was an error; don't attempt update
							// and focus the first form field with an error.
							focusView.requestFocus();
						} else {
							// Show a progress spinner, and kick off a
							// background task to update user password
							mPersonTask = new UpdatePersonInfo(fName, lName,
									email);
							mPersonTask.execute((Void) null);
							alertDialog.dismiss();
						}
					}
				});
			}
		});

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
		final EditText eaPassw = (EditText) findViewById(R.id.editTextLocPw);

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
							// if this button is clicked, update details
							public void onClick(DialogInterface dialog, int id) {
								// Get values form EditText fields
//		TODO						String addr = eAddress.getText().toString();
//								String suburb = eSuburb.getText().toString();
//								String city = eCity.getText().toString();
//								String postcode = ePostcode.getText()
//										.toString();
//								String passw = eaPassw.getText().toString();
								dialog.cancel();
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
	protected void pwDialog() {

		LayoutInflater factory = LayoutInflater.from(this);

		// change_pw is a Layout XML file containing text fields to display in
		// alert dialog
		View textEntryView = factory.inflate(R.layout.change_pw, null);

		// Get each editText field
		final EditText tCurrentPw = (EditText) textEntryView
				.findViewById(R.id.editTextPw);
		final EditText tNewPw = (EditText) textEntryView
				.findViewById(R.id.editTextNewPw);
		final EditText tNewPwCheck = (EditText) textEntryView
				.findViewById(R.id.editTextCheckNewP);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ProfileActivity.this);

		// set title
		alertDialogBuilder.setTitle("Change Password");

		// set dialog message
		alertDialogBuilder.setView(textEntryView).setCancelable(false)
				.setPositiveButton("Submit Changes", null)
				.setNegativeButton("Cancel", null);

		// create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				Button positive = alertDialog
						.getButton(AlertDialog.BUTTON_POSITIVE);
				positive.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// Reset errors.
						tCurrentPw.setError(null);
						tNewPw.setError(null);
						tNewPwCheck.setError(null);

						// Deal with input errors
						boolean cancel = false;
						View focusView = null;

						// Get values from the EditText fields
						String pw = tCurrentPw.getText().toString();
						String newPw = tNewPw.getText().toString();
						String newPwCheck = tNewPwCheck.getText().toString();

						// Check current password is correct
						for (Users user : userList) {
							if (user.getInfoID() == (infoId)) {
								// Check if the current password matches stored
								// password
								if (!user.getPassword().equals(pw)) {
									tCurrentPw
											.setError(getString(R.string.error_incorrect_password));
									focusView = tCurrentPw;
									cancel = true;
								}
								break;
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
							// There was an error; don't attempt update
							// and focus the first form field with an error.
							focusView.requestFocus();
						} else {
							// Show a progress spinner, and kick off a
							// background task to update user password
							mPwTask = new UpdatePwTask(newPw);
							mPwTask.execute((Void) null);
							alertDialog.dismiss();
						}
					}
				});
			}
		});

		// show it
		alertDialog.show();
	}

	private boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 3;
	}

	private void updateSession() {
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
	 * Async task used to send the updated info to the database
	 */
	private class UpdatePersonInfo extends AsyncTask<Void, Void, Boolean> {

		private final String mInfo;
		private final String mFName;
		private final String mLName;
		private final String mEmail;

		public UpdatePersonInfo(String fName, String lName, String email) {
			mFName = fName;
			mLName = lName;
			mEmail = email;
			
			ArrayMap<String, String> user = new ArrayMap<String, String>();
			user.put("firstName", mFName);
			user.put("lastName", mLName);
			user.put("email", mEmail);
			mInfo = UsersJSONParser.PUTUsers(user);
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			// update details here
			HttpManager.updateData(
					"http://gb3it.pickworth.info:3000/person_infos/" + infoId,
					mInfo);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			
			session.updateUserDetails(mFName, mLName, mEmail);
			updateSession();
			mPwTask = null;
		}

		@Override
		protected void onCancelled() {
			mPwTask = null;
		}
	}

	/**
	 * Async task used to send the updated password to the database
	 */
	private class UpdatePwTask extends AsyncTask<Void, Void, Boolean> {

		private final String mPassword;

		public UpdatePwTask(String newPw) {
			ArrayMap<String, String> user = new ArrayMap<String, String>();
			user.put("password", newPw);
			mPassword = UsersJSONParser.PUTUsers(user);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// update details here
			HttpManager.updateData(
					"http://gb3it.pickworth.info:3000/person_infos/" + infoId,
					mPassword);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			updateSession();
			mPwTask = null;
		}

		@Override
		protected void onCancelled() {
			mPwTask = null;
		}
	}
}
