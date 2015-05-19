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
	List<MyTask> tasks;
	
	//Dialog Fragments
	AdminCommunityDialog adminDialog;
	boolean dialogResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_all_communities);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		tasks = new ArrayList<>();
		
		//start communites task
		display();
		
		//Clickable ListView
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

		// Method to return data to the Dialog Fragment
			public void setDialogResults(boolean result, String option, int selectedCommunityId, String selectedCommunityName) {
				adminDialog.dismiss();

				if (result == true) {
					setOptionIntents(option, selectedCommunityId, selectedCommunityName);
				}
			}
			
			// Method where selected options are implemented
			public void setOptionIntents(String options, int selectedCommunityId, String selectedCommuntiyName) {
				if (options == "Post a message in community") {
					
					//Toast.makeText(this, "New page", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(AdminAllCommunities.this, AdminMessageBoard.class);
					intent.putExtra("CommunityId", selectedCommunityId);
					intent.putExtra("CommunityName", selectedCommuntiyName);
					startActivity(intent);
				}
				
				if (options == "View Reports") {
					Intent intent = new Intent(AdminAllCommunities.this, AdminReports.class);
					intent.putExtra("CommunityId", selectedCommunityId);
					intent.putExtra("CommunityName", selectedCommuntiyName);
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
	
	private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
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
			setListView();

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
				int position, long id) {
		
			Community selectedCommunity = (Community) list.getItemAtPosition(position);
			
			String	communityName = selectedCommunity.getCommunityName();
			int communityId = selectedCommunity.getCommunityID();
		
			adminDialog = new AdminCommunityDialog(communityName, communityId);
			FragmentManager fm = getFragmentManager();
			adminDialog.show(fm, "confirm");				
		}
	}
	
	private void setListView() {
		// Get reference to the listView
		ListView communityNameListView = (ListView) findViewById(R.id.ListViewCommunities);		
		//set adapter
		CommunityListAdapter adapter = new CommunityListAdapter(this, R.layout.admin_allusers_layout, communityList);
		// Bind the ListView to the above adapter
		communityNameListView.setAdapter(adapter);
	
	}
	
}
