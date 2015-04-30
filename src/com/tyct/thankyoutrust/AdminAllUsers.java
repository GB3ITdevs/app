package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import com.tyct.thankyoutrust.model.AdminID;
import com.tyct.thankyoutrust.model.PhoneNumber;
import com.tyct.thankyoutrust.model.ProjectRating;
import com.tyct.thankyoutrust.model.User;
import com.tyct.thankyoutrust.parsers.AdminIDJSONParser;
import com.tyct.thankyoutrust.parsers.PhoneNumberJSONParser;
import com.tyct.thankyoutrust.parsers.ProjectRatingsJSONParser;
import com.tyct.thankyoutrust.parsers.UserJSONParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AdminAllUsers extends Activity {

	// Session Manager Class
	SessionManager session;

	List<User> userList;
	List<AdminID> adminList;
	List<ProjectRating> prList;
	List<PhoneNumber> phoneNumberList;


	List<MyTask> tasks;

	List<DeleteUserTask> deleteusertask;
	List<ProjectRatingTask> prtask;
	List<PhoneNumberTask> phoneNumbertask;

	String[] userNames;
	String selectedItem = "";
	int[] userIds;
	int selectedUserId;
	int adminid;
	AdminID adminEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_all_users);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		// start async task
		tasks = new ArrayList<>();
		//adminTask = new ArrayList<>();
		prtask = new ArrayList<>();
		phoneNumbertask = new ArrayList<>();

		// makes connection to database
		display();
		projectRatingInfo();
		phoneNumberInfo();
		
		ListView groupAct = (ListView) findViewById(R.id.lstvewuser);
		groupAct.setOnItemClickListener(new ListViewUserClickHandler());
	}

	// Top menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.admin_home_page, menu);
		return true;
	}

	// Menu Options
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent goTo = new Intent();
		switch (item.getItemId()) {
		case R.id.action_projects:
			goTo = new Intent(AdminAllUsers.this, Projects.class);
			startActivity(goTo);
			return true;
		case R.id.action_home:
			goTo = new Intent(AdminAllUsers.this, HomeActivity.class);
			startActivity(goTo);
			finish();
			return true;
		case R.id.action_profile:
			goTo = new Intent(AdminAllUsers.this, ProfileActivity.class);
			startActivity(goTo);
			return true;
		case R.id.action_about_us:
			goTo = new Intent(AdminAllUsers.this, AboutUs.class);
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

	// Gets the clicked users infoId
	public int getUserId(int positionClicked) {
		int infoID = userIds[positionClicked];
		return infoID;

	}

	// Method to setup the List View to display all users
	public void setUserList(List<User> userList) {

		userNames = new String[userList.size()];
		userIds = new int[userList.size()];
		int i = 0;
		// Add each user name from the project list to the array of strings
		for (User user : userList) {
			userNames[i] = user.getFirstName() + " " + user.getLastName();
			userIds[i] = user.getUserID();
			i++;
		}

		// Create the adapter
		ArrayAdapter<String> adminOptionsAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, userNames);
		// Create the ListView
		ListView userNameListView = (ListView) findViewById(R.id.lstvewuser);
		// Bind the ListView to the above adapter
		userNameListView.setAdapter(adminOptionsAdapter);
	}

	private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}

	private void requestProjectRatingData(String uri) {
		ProjectRatingTask task = new ProjectRatingTask();
		task.execute(uri);
	}
	
	private void requestPhoneNumberData(String uri) {
		PhoneNumberTask task = new PhoneNumberTask();
		task.execute(uri);
	}

	// Method that checks if phone is online
	public void display() {
		if (isOnline()) {
			requestData("http://gb3it.pickworth.info:3000/users");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void projectRatingInfo() {
		if (isOnline()) {
			requestProjectRatingData("http://gb3it.pickworth.info:3000/ratings");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}
	
	public void phoneNumberInfo() {
		if (isOnline()) {
			requestPhoneNumberData("http://gb3it.pickworth.info:3000/phone_numbers");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}

	// Connect to database
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	/**
    	 * 
    	 * 
    	 */
	private class ProjectRatingTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			prtask.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			prList = ProjectRatingsJSONParser.parseFeed(result);
			prtask.remove(this);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}
	}

	/**
    	 * 
    	 * 
    	 */
	private class MyTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// updateDisplay("Starting task");

		}

		@Override
		protected String doInBackground(String... params) {

			// HttpManager
			String content = HttpManager.getData(params[0]);
			return content;

		}

		@Override
		protected void onPostExecute(String result) {

			// populates userlist from parser
			userList = UserJSONParser.parseFeed(result);
			// populates the list view using the setUserList Method.
			setUserList(userList);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}

	// Handles the posting side
	private class DeleteUserTask extends AsyncTask<String, String, String> {
		// ratings string
		String ratingUriString = "http://gb3it.pickworth.info:3000/ratings/";

		// user Id string
		String userIdUriString = "http://gb3it.pickworth.info:3000/users/";

		int num = 0;

		@Override
		protected void onPreExecute() {
			deleteusertask.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			HttpManager.deleteData(userIdUriString + selectedUserId);

			// for (int i=0; i<projectRatingIdDeleteList.length; i++)
			// {
			// num = projectRatingIdDeleteList[i];
			// //HttpManager.deleteData(ratingUriString + num);
			// Toast.makeText(AdminAllUsers.this, "rating id = " + num,
			// Toast.LENGTH_LONG).show();
			// }

			String result = "User Deleted";

			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			// String messageResult = (result);

			deleteusertask.remove(this);

			startActivity(new Intent(AdminAllUsers.this, AdminAllUsers.class));
			finish();
		}

		@Override
		protected void onProgressUpdate(String... values) {
		}

	}
	
	/**
	 * 
	 * 
	 */
private class PhoneNumberTask extends AsyncTask<String, String, String> {

	@Override
	protected void onPreExecute() {
		phoneNumbertask.add(this);
	}

	@Override
	protected String doInBackground(String... params) {
		String content = HttpManager.getData(params[0]);
		return content;
	}

	@Override
	protected void onPostExecute(String result) {
		phoneNumberList = PhoneNumberJSONParser.parseFeed(result);
		phoneNumbertask.remove(this);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// updateDisplay(values[0]);
	}

}
	
	// Handles the ListView clicks
		public class ListViewUserClickHandler implements OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> list, View itemview,
					int posistion, long id) {
				String name = (String) list.getItemAtPosition(
						posistion).toString();
				int theUsersID = getUserId(posistion);
				
				SharedPreferences prefs = getSharedPreferences("prefsFile", MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				
				//get User Data
				String email = null;
				String address = null;
				String suburb = null;
				String city = null;
				//int communityId;
			//	String community = null;
				//String phoneNumber = null;
				
				for (User user : userList) {
					if(user.getUserID() == theUsersID)
					{
					//userName = user.getFirstName() + " " + user.getLastName();
					email = user.getEmail();
					address = user.getStreetAddress();
					suburb = user.getSuburb();
					city = user.getCity();
					//communityId = user.getCommunityID();
					break;
					}
				}
				
//				for(PhoneNumber phone : phoneNumberList)
//				{
//					if(theUsersID == phone.getUserID())
//					{
//						phoneNumber = phone.getPhoneNumber();
//					}
//				}
//				
				editor.putString("name", name);
				editor.putInt("userId", theUsersID);
				editor.putString("email", email);
				editor.putString("address", address);
				editor.putString("suburb", suburb);
				editor.putString("city", city);
				//editor.putString("phone", phoneNumber);
				editor.commit();
				
				Intent intent = new Intent(AdminAllUsers.this, AdminUsersProfile.class);
				startActivity(intent);
				
			}

		}

	public void selectRatingsToDelete() {
		prtask = new ArrayList<>();
		int[] projectRatingIdDeleteList;

		projectRatingIdDeleteList = new int[prList.size()];
		int i = 0;
		// Add each user name from the project list to the array of strings
		for (ProjectRating pr : prList) {
			if (pr.getUserID() == selectedUserId) {

				projectRatingIdDeleteList[i] = pr.getRatingID();
				i++;

			}
		}
	}

	// method to delete a user
	public void deleteUser() {

		selectRatingsToDelete();

//		deleteusertask = new ArrayList<>();
//		DeleteUserTask task = new DeleteUserTask();
//		task.execute();

	}

	

	
	
}
