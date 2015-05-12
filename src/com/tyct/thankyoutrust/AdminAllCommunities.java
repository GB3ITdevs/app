package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import com.tyct.thankyoutrust.AdminAllUsers.ListViewUserClickHandler;
import com.tyct.thankyoutrust.dialogs.AdminChangeCommunitiesDialog;
import com.tyct.thankyoutrust.dialogs.AdminCommunityDialog;
import com.tyct.thankyoutrust.dialogs.AdminOptionsDialog;
import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.model.PhoneNumber;
import com.tyct.thankyoutrust.model.User;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AdminAllCommunities extends Activity {
	// Session Manager Class
	SessionManager session;

	List<Community> communityList;
	String[] communityNames;
	List<MyTask> tasks;
	
	//Dialog Fragements
	AdminCommunityDialog adminDialog;
	boolean dialogResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_all_communities);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		tasks = new ArrayList<>();

		display();
		
		ListView groupAct = (ListView) findViewById(R.id.ListViewCommunities);
		groupAct.setOnItemClickListener(new ListViewUserClickHandler());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent goTo = new Intent();
		switch (item.getItemId()) {
		case R.id.action_projects:
			goTo = new Intent(AdminAllCommunities.this, Projects.class);
			startActivity(goTo);
			return true;
		case R.id.action_home:
			goTo = new Intent(AdminAllCommunities.this, HomeActivity.class);
			startActivity(goTo);
			finish();
			return true;
		case R.id.action_profile:
			goTo = new Intent(AdminAllCommunities.this, ProfileActivity.class);
			startActivity(goTo);
			return true;
		case R.id.action_about_us:
			goTo = new Intent(AdminAllCommunities.this, AboutUs.class);
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

	private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}

	@SuppressWarnings("null")
	public void setCommunityList(List<Community> communityList) {

		int i = 0;

		if (communityList != null) {
			communityNames = new String[communityList.size()];

			// Add each user name from the project list to the array of strings
			for (Community community : communityList) {
				String communityNa = community.getCommunityName();
				int code = community.getPostalCode();
				String postalCode = String.valueOf(code);
				communityNames[i] = communityNa + " " + postalCode;
				i++;
			}
		}

		ArrayAdapter<String> adminOptionsAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, communityNames);
		ListView userNameListView = (ListView) findViewById(R.id.ListViewCommunities);
		userNameListView.setAdapter(adminOptionsAdapter);
	}
	
	//button that brings up admins options for said user
		public class buttonOptionsClick implements OnClickListener
		{

			@Override
			public void onClick(View v) {
				adminDialog = new AdminCommunityDialog();
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
				if (options == "Post a message in community") {
					Intent intent = new Intent(AdminAllCommunities.this, AdminUsersProfile.class);
					startActivity(intent);
				}
			}

	public void display() {
		if (isOnline()) {
			requestData("http://gb3it.pickworth.info:3000/communities");
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

	private class MyTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// updateDisplay("Starting task");

		}

		@Override
		protected String doInBackground(String... params) {

			String content = HttpManager.getData(params[0]);
			return content;

		}

		@Override
		protected void onPostExecute(String result) {

			communityList = CommunityJSONParser.parseFeed(result);
			setCommunityList(communityList);

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
			
			adminDialog = new AdminCommunityDialog();
			FragmentManager fm = getFragmentManager();
			adminDialog.show(fm, "confirm");	
			
			
		}

	}
	
}
