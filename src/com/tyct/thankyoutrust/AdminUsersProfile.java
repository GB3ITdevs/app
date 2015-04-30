package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import com.tyct.thankyoutrust.dialogs.AdminOptionsDialog;
import com.tyct.thankyoutrust.model.AdminID;
import com.tyct.thankyoutrust.parsers.AdminIDJSONParser;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AdminUsersProfile extends Activity {
	
	//Async Tasks
	List<PostAdminTask> postadmintask;
	List<AdminTask> adminTask;
	List<DeleteAdminTask> deleteAdminTask;
	
	//Lists
	List<AdminID> adminList;
	
	// Session Manager Class
	SessionManager session;
	
	//Global Variables
	boolean dialogResult;
	AdminOptionsDialog adminDialog;
	String userName;
	int userId;
	int adminId;
	AdminID adminEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_users_profile);
		
		//get users name
		SharedPreferences prefs = getSharedPreferences("prefsFile", MODE_PRIVATE);
		userName = prefs.getString("name", "name");
		userId = prefs.getInt("userId", 0);	
		
		// start async task
		adminTask = new ArrayList<>();
		
		// makes connection to database
		adminInfo();
		
		//Poputlate Text fields with chosen user
		populateFields();

		//Button 
		Button optionsButton = (Button) findViewById(R.id.btnOptions);
		optionsButton.setOnClickListener(new buttonOptionsClick());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_users_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void populateFields() {
		
		SharedPreferences prefs = getSharedPreferences("prefsFile", MODE_PRIVATE);
		String namedPerson = prefs.getString("name", "name");	
		String email = prefs.getString("email", "email");
		String address = prefs.getString("address", "address");
		String suburb = prefs.getString("suburb", "suburb");
		String city = prefs.getString("city", "city");
		String phoneNumber = prefs.getString("phone","phoneNumber");
		
		// Get references
				TextView uName = (TextView) findViewById(R.id.tvProfName);
				TextView uEmail = (TextView) findViewById(R.id.tvProfEmail);
				TextView uPhone = (TextView) findViewById(R.id.tvPPhoneNumber);
				TextView uAddress = (TextView) findViewById(R.id.tvPStreetAddress);
				TextView uSuburb = (TextView) findViewById(R.id.tvPSuburb);
				TextView uCity = (TextView) findViewById(R.id.tvPCity);
			//	TextView uCommunity = (TextView) findViewById(R.id.tvPCommunity);
								
				//set textfields	
				uName.setText(namedPerson);
				uEmail.setText(email);
				uPhone.setText(phoneNumber);
				uAddress.setText(address);
				uSuburb.setText(suburb);
				uCity.setText(city);
	}
	
	//button that brings up admins options for said user
	public class buttonOptionsClick implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			adminDialog = new AdminOptionsDialog(userName);
			FragmentManager fm = getFragmentManager();
			adminDialog.show(fm, "confirm");		
		}
		
	}
	
	// Method to return data to the Dialog Fragment
		public void setDialogResults(boolean result, String option) {
			adminDialog.dismiss();

			if (result == true) {
				setOptionIntents(option);
			}
		}
		
		// Method where selected options are implemented
		public void setOptionIntents(String options) {
			if (options == "Set as admin") {
				addAdmin();
			}

			if (options == "Remove as admin") {
				deleteAdmin();
			}

			if (options == "Delete user") {
				Toast.makeText(this, "Delete User", Toast.LENGTH_LONG).show();
			}
			
			if (options == "Change community") {
				Toast.makeText(this, "Delete User", Toast.LENGTH_LONG).show();
			}
		}
		
		// method to add an admin (POST)
		public void addAdmin() {
			boolean isAdmin = false;
			adminTask = new ArrayList<>();

			for (AdminID adm : adminList) {
				if (userId == adm.getUserID()) {
					isAdmin = true;
					Toast.makeText(this, "User is already an admin",
							Toast.LENGTH_LONG).show();
					break;
				}
			}

			if (!isAdmin) {
				adminEntity = new AdminID();
				adminEntity.setUserID(userId);

				postadmintask = new ArrayList<>();
				PostAdminTask task = new PostAdminTask();
				task.execute();
			}
		}
		
		// method to delete an administrator
		public void deleteAdmin() {

			adminTask = new ArrayList<>();

			for (AdminID adm : adminList) {
				if (userId == adm.getUserID()) {
					adminId = adm.getAdminID();
					deleteAdminTask = new ArrayList<>();
					DeleteAdminTask task = new DeleteAdminTask();
					task.execute();
				}
			}	
		}
		
		
		//Checks to see if phone is online
		protected boolean isOnline() {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				return true;
			} else {
				return false;
			}
		}	
		
		//************************************************** REQUEST DATA *******************************************************************************
		private void requestAdminData(String uri) {
			AdminTask task = new AdminTask();
			task.execute(uri);
		}
		
		
		//************************************************** URI STRINGS FOR DATA *******************************************************************
		public void adminInfo() {
			if (isOnline()) {
				requestAdminData("http://gb3it.pickworth.info:3000/administrators");
			} else {
				Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
						.show();
			}
		}

		/*****************************************************************************************************************************
    	 * 
    	 * 						ASYNC TASKS
    	 *****************************************************************************************************************************/	
		
		/**
    	 *  Admin Task to load the adminList
    	 */
	private class AdminTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			adminTask.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			adminList = AdminIDJSONParser.parseFeed(result);
			adminTask.remove(this);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}
	}
	
		/** 
    	 * Delete an admin Async task (DELETE)
    	 */
		private class DeleteAdminTask extends AsyncTask<String, String, String> {
			String deleteAdmin = "http://gb3it.pickworth.info:3000/administrators/";

			@Override
			protected void onPreExecute() {
				deleteAdminTask.add(this);
			}

			@Override
			protected String doInBackground(String... params) {
				HttpManager.deleteData(deleteAdmin + adminId);
				String result = "Admin Deleted";
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				deleteAdminTask.remove(this);
				Toast.makeText(AdminUsersProfile.this, result , Toast.LENGTH_LONG).show();
			}

			@Override
			protected void onProgressUpdate(String... values) {
			}

		}
		
		/** 
    	 * Add an admin (POST)
    	 */
		private class PostAdminTask extends AsyncTask<String, String, String> {
			
			
			String adminEntityString = AdminIDJSONParser.POST(adminEntity);

			@Override
			protected void onPreExecute() {
				postadmintask.add(this);
			}

			@Override
			protected String doInBackground(String... params) {
				HttpManager.postData("http://gb3it.pickworth.info:3000/administrators", adminEntityString);
				String result = "Admin added";
				return result;
			}

			@Override
			protected void onPostExecute(String result) {

				// String messageResult = (result);

				postadmintask.remove(this);
				Toast.makeText(AdminUsersProfile.this, result, Toast.LENGTH_LONG)
				.show();
			}

			@Override
			protected void onProgressUpdate(String... values) {
			}

		}
}
