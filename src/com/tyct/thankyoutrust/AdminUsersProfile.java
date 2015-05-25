package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import com.tyct.thankyoutrust.dialogs.AdminChangeCommunitiesDialog;
import com.tyct.thankyoutrust.dialogs.AdminOptionsDialog;
import com.tyct.thankyoutrust.model.AdminID;
import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.model.User;
import com.tyct.thankyoutrust.parsers.AdminIDJSONParser;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;
import com.tyct.thankyoutrust.parsers.UserJSONParser;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
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
	List<CommunityTask> communityTask;
	List<ChangeCommunityTask> changeCommunityTask;
	
	//Lists
	List<AdminID> adminList;
	List<Community> communityList;	
	
	// Session Manager Class
	SessionManager session;
	
	//Global Variables
	String userName;
	int userId;
	int adminId;
	AdminID adminEntity;
	String[] communityNames;
	int newCommunityId=0;
	int userCommunityId;
	
	//Dialog Fragements
	AdminOptionsDialog adminDialog;
	AdminChangeCommunitiesDialog changeCommunityDialog;
	boolean dialogResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_users_profile);
		
		// start asyncTask
		adminTask = new ArrayList<>();
		communityTask = new ArrayList<>();
		
		// makes connection to database
		adminInfo();
		communityInfo();
			
		//Populate Text fields with chosen user
		populateFields();

		//Button 
		Button optionsButton = (Button) findViewById(R.id.btnOptions);
		optionsButton.setOnClickListener(new buttonOptionsClick());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent goTo = new Intent();
		switch (item.getItemId()) {
		case R.id.action_projects:
			goTo = new Intent(AdminUsersProfile.this, Projects.class);
			startActivity(goTo);
			return true;
		case R.id.action_home:
			goTo = new Intent(AdminUsersProfile.this, HomeActivity.class);
			startActivity(goTo);
			finish();
			return true;
		case R.id.action_profile:
			goTo = new Intent(AdminUsersProfile.this, ProfileActivity.class);
			startActivity(goTo);
			return true;
		case R.id.action_about_us:
			goTo = new Intent(AdminUsersProfile.this, AboutUs.class);
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
	
	
	//populate text fields
	private void populateFields() {
		
		//get all data from passed in intent
		Intent intent = getIntent();
		Bundle allData = intent.getExtras();

		//get data
		String name = allData.getString("name");
		String email = allData.getString("email");
		String address = allData.getString("address");
		String suburb = allData.getString("suburb");
		String city = allData.getString("city");
		String communityName = allData.getString("communityName");
		int id = allData.getInt("userId");
		String phoneNumber = allData.getString("phoneNumber");
		int communityId = allData.getInt("communityID");
		
		//setup textfields
		TextView uName = (TextView) findViewById(R.id.tvProfName);
		TextView uEmail = (TextView) findViewById(R.id.tvProfEmail);
		TextView uAddress = (TextView) findViewById(R.id.tvPStreetAddress);
		TextView uSuburb = (TextView) findViewById(R.id.tvPSuburb);
		TextView uCity = (TextView) findViewById(R.id.tvPCity);
		TextView uCommunityName = (TextView) findViewById(R.id.tvPCommunity);
		TextView uPhone = (TextView) findViewById(R.id.tvPPhoneNumber);
		
		//set text fields
		uName.setText(name);
		uEmail.setText(email);
		uAddress.setText(address);
		uSuburb.setText(suburb);
		uCity.setText(city);
		uCommunityName.setText(communityName);
		uPhone.setText(phoneNumber);
		
		//make global
		userName = name;
		userId = id;
		userCommunityId = communityId;
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
				if(option == null)
				{
				Toast.makeText(this, "No option selected", Toast.LENGTH_LONG).show();
				}
				else
				{
				setOptionIntents(option);
				}
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
			
			if (options == "Change community") {
				changeCommunityDialog = new AdminChangeCommunitiesDialog();
				FragmentManager fragman = getFragmentManager();
				changeCommunityDialog.show(fragman, "confirm");
			}
		}
		
		// method to add an admin (POST)
		public void addAdmin() {
			boolean isAdmin = false;
			adminTask = new ArrayList<>();

			for (AdminID adm : adminList) {
				if (userId == adm.getUserID()) {
					isAdmin = true;
					Toast.makeText(this, userName +" is already an admin",
							Toast.LENGTH_LONG).show();
					break;
				}
			}
			//Add as an admin if user is not already an admin
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
			
			boolean isAdmin = false;
			adminTask = new ArrayList<>();
			
			//check to see if user is an admin
			for (AdminID adm : adminList) {
				if (userId == adm.getUserID()) {
					isAdmin = true;
					break;
				}
			}
			
			//if user is an admin then start Async task to delete user
			if(isAdmin)
			{
				for (AdminID adm : adminList)
				{
					if (userId == adm.getUserID())
					{
						adminId = adm.getAdminID();
						deleteAdminTask = new ArrayList<>();
						DeleteAdminTask task = new DeleteAdminTask();
						task.execute();
						break;
					}
				}				
			}	
			else //if user not an admin then display a toast
			{
				Toast.makeText(this, userName +" is not an admin",
						Toast.LENGTH_LONG).show();
			}
		}
		
		// method to change community of selected user
		public void setNewCommunity(boolean result, String newCommunity) {
			changeCommunityDialog.dismiss();//dismiss dialog box
			
			//if clicked submit do this
			if (result == true) 
			{
				if (isOnline()) 
				{
					// get the name of each community in communityList
					for (Community com : communityList) {
						if(newCommunity == com.getCommunityName()){
							newCommunityId = com.getCommunityID();
						}	
					}					
					//If user is already part of the community then don't execute
					if(userCommunityId == newCommunityId)
					{
						Toast.makeText(this, userName + " is already in this community", Toast.LENGTH_LONG).show();
					}
					//go to asyncTask to change community
					else
					{
					ChangeCommunityTask task = new ChangeCommunityTask (newCommunity);
					task.execute();
					}
				} 
				//Show toast if network is unavailable
				else 
				{
					Toast.makeText(this, "Network isn't available.", Toast.LENGTH_LONG).show();
				}
			} 
			
			//if clicked cancel
			else 
			{
				Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
			}
		}
		
		//Method to set CommunityNames
		public void setCommunityNames(List<Community> com){
		
			// Convert communityList to an array
			communityNames = new String[communityList.size()];

			// get the name of each community in communityList
			for (Community community : communityList) {
				int i = communityList.indexOf(community);
				communityNames[i] = community.getCommunityName();
			}			
		}
		
		//Get communityNames which is called by the AdminChangeCommunitiesDailog
		public String[] getCommunityNames()
		{
			return communityNames;
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
		
		private void requestCommunityData(String uri) {
			CommunityTask task = new CommunityTask();
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

		public void communityInfo() {
			if (isOnline()) {
				requestCommunityData("http://gb3it.pickworth.info:3000/communities");
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
				String result = "Admin Removed";
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				deleteAdminTask.remove(this);
				String messageResult = result;
				Toast.makeText(AdminUsersProfile.this, messageResult , Toast.LENGTH_LONG).show();
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

				String messageResult = (result);

				postadmintask.remove(this);
				Toast.makeText(AdminUsersProfile.this, messageResult, Toast.LENGTH_LONG)
				.show();
			}

			@Override
			protected void onProgressUpdate(String... values) {
			}
		}
		
		/** 
    	 * Get community List AsyncTask
    	 */
		private class CommunityTask extends AsyncTask<String, String, String> {

			@Override
			protected void onPreExecute() {
				communityTask.add(this);
			}

			@Override
			protected String doInBackground(String... params) {
				String content = HttpManager.getData(params[0]);
				return content;
			}

			@Override
			protected void onPostExecute(String result) {
				communityList = CommunityJSONParser.parseFeed(result);
				communityTask.remove(this);

				setCommunityNames(communityList);
			}
		}
		
		/** 
    	 * Change new Community for User (Update user Community)
    	 */
		private class ChangeCommunityTask extends AsyncTask<Void, Void, Boolean> {

			private final String changeCommunity;
			private final String communityName;

			public ChangeCommunityTask(String community) {
				ArrayMap<String, String> user = new ArrayMap<String, String>();
				
				String communityId = Integer.toString(newCommunityId);
				user.put("communityID", communityId);
				changeCommunity = UserJSONParser.PUTUser(user);
				communityName = community;
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				// update details here
				HttpManager.updateData(
						"http://gb3it.pickworth.info:3000/users/" + userId,
						changeCommunity);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean success) {			
				newCommunityId = 0; //set community varibale back to 0
				//update textView for communityName
				TextView uCommunityName = (TextView) findViewById(R.id.tvPCommunity);
				uCommunityName.setText(communityName);			
			}
		}
}
