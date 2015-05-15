package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tyct.thankyoutrust.model.AdminID;
import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.model.PhoneNumber;
import com.tyct.thankyoutrust.model.ProjectRating;
import com.tyct.thankyoutrust.model.User;
import com.tyct.thankyoutrust.parsers.AdminIDJSONParser;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AdminAllUsers extends Activity {

	// Session Manager Class
	SessionManager session;

	List<User> userList;
	List<AdminID> adminList;
	List<PhoneNumber> phoneNumberList;
	List<Community> communityList;

	List<MyTask> tasks;
	List<PhoneNumberTask> phoneNumbertask;
	List<CommunityTask> communitytask;

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
		phoneNumbertask = new ArrayList<>();
		communitytask = new ArrayList<>();

		// makes connection to database
		communityInfo();
		display();
		phoneNumberInfo();

		ListView groupAct = (ListView) findViewById(R.id.lstvewuser);
		groupAct.setOnItemClickListener(new ListViewUserClickHandler());
	}

	// Top menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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

	private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}
	
	private void requestPhoneNumberData(String uri) {
		PhoneNumberTask task = new PhoneNumberTask();
		task.execute(uri);
	}
	
	private void requestCommunityData(String uri) {
		CommunityTask task = new CommunityTask();
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
	
	public void phoneNumberInfo() {
		if (isOnline()) {
			requestPhoneNumberData("http://gb3it.pickworth.info:3000/phone_numbers");
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
	
	private class CommunityTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			communitytask.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			communityList = CommunityJSONParser.parseFeed(result);
			communitytask.remove(this);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}
	}

	/**
    	 * 
    	 * Populates userList
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
	
			setListView();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}
		
	/**
	 * 
	 * Gets phone List
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
				
				User selectedUser = (User) list.getItemAtPosition(posistion);

				int theUsersID = selectedUser.getUserID();
				String name = selectedUser.getFirstName() + " " + selectedUser.getLastName();
				String	email = selectedUser.getEmail();
				String	address = selectedUser.getStreetAddress();
				String	suburb = selectedUser.getSuburb();
				String	city = selectedUser.getCity();
				int	communityId = selectedUser.getCommunityID();
				String communityName = null;
				String phoneNumber = null;
				
				for(Community com : communityList) {
					if(communityId == com.getCommunityID())
					{
						communityName = com.getCommunityName();
					}
				}
				
				for(PhoneNumber phone : phoneNumberList)
				{
					if(theUsersID == phone.getUserID())
					{
						phoneNumber = phone.getPhoneNumber();
					}
				}
				
				Intent detailIntent = new Intent(AdminAllUsers.this, AdminUsersProfile.class);
				
				detailIntent.putExtra("name", name);
				detailIntent.putExtra("userId", theUsersID);
				detailIntent.putExtra("email", email);
				detailIntent.putExtra("address", address);
				detailIntent.putExtra("suburb", suburb);
				detailIntent.putExtra("city", city);
				detailIntent.putExtra("communityName", communityName);
				detailIntent.putExtra("phoneNumber", phoneNumber);
				detailIntent.putExtra("communityID", communityId);
				startActivity(detailIntent);
				
			}

		}
		
		private void setListView() {
			
			// Get reference to the listView
			ListView userNameListView = (ListView) findViewById(R.id.lstvewuser);
			Collections.sort(userList, new Comparator<User>()
					{
				@Override
				public int compare(User us1, User us2)
				{
					return us1.getLastName().compareToIgnoreCase(us2.getLastName());
				}
					});
			UserListAdapter adapter = new UserListAdapter(this, R.layout.admin_allusers_layout, userList, communityList);
			// Bind the ListView to the above adapter
			userNameListView.setAdapter(adapter);
		
		}
		
}
