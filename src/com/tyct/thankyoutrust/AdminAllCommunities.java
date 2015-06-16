package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tyct.thankyoutrust.dialogs.AdminAddNewCommunity;
import com.tyct.thankyoutrust.dialogs.AdminCommunityDialog;
import com.tyct.thankyoutrust.dialogs.AdminEditCommunityDialog;
import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;

public class AdminAllCommunities extends Activity {
	// Session Manager Class
	SessionManager session;

	List<Community> communityList;
	List<MyTask> tasks;
	List<AddCommunityTask> addCommunityTask;
	List<EditCommunityTask> editCommunityTask;

	// Dialog Fragments
	AdminCommunityDialog adminDialog;
	AdminAddNewCommunity newCommunityDialog;
	AdminEditCommunityDialog editCommunityDialog;

	boolean dialogResult;

	Community newCommunityEntity;
	int selectedPostalCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_all_communities);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		tasks = new ArrayList<>();

		// start communities task
		display();

		// Clickable ListView
		ListView groupAct = (ListView) findViewById(R.id.ListViewCommunities);
		groupAct.setOnItemClickListener(new ListViewUserClickHandler());

		// add a community button
		ImageView addButton = (ImageView) findViewById(R.id.addImageButton);
		addButton.setOnClickListener(new AddCommunityButton());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_admin, menu);
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
		case R.id.admin:
			goTo = new Intent(AdminAllCommunities.this, AdminHomePage.class);
			startActivity(goTo);
			return true;
		case R.id.action_profile:
			goTo = new Intent(AdminAllCommunities.this, ProfileActivity.class);
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
	public void setDialogResults(boolean result, String option,
			int selectedCommunityId, String selectedCommunityName) {
		adminDialog.dismiss();

		if (result == true) {
			setOptionIntents(option, selectedCommunityId, selectedCommunityName);
		}
	}

	// Method to return data to the Dialog Fragment
	public void addNewCommunity(boolean result, String communityName,
			int postCode) {
		newCommunityDialog.dismiss();

		if (result == true) {
			newCommunityEntity = new Community();
			newCommunityEntity.setCommunityName(communityName);
			newCommunityEntity.setPostalCode(postCode);

			addCommunityTask = new ArrayList<>();
			AddCommunityTask task = new AddCommunityTask();
			task.execute();
		}
	}

	// Method to return data to the Dialog Fragment
	public void addNewCommunityToast(boolean result, String communityName,
			int postCode) {

		if (result == true) {
			if (communityName == null) {
				Toast.makeText(AdminAllCommunities.this,
						"Community Name missing", Toast.LENGTH_LONG).show();
			}
		}

	}

	// Method to return data to the Dialog Fragment
	public void editCommunity(boolean result, String communityName,
			int postCode, int communityId) {
		editCommunityDialog.dismiss();

		if (result == true) {
			editCommunityTask = new ArrayList<>();
			EditCommunityTask task = new EditCommunityTask(communityId,
					communityName, postCode);
			task.execute();
			// Toast.makeText(AdminAllCommunities.this, "community id: " +
			// communityId + "; " +communityName + ", " + postCode +" updated.",
			// Toast.LENGTH_LONG).show();
		}
	}

	// Method where selected options are implemented
	public void setOptionIntents(String options, int selectedCommunityId,
			String selectedCommunityName) {
		if (options == "Post a message in community") {
			Intent intent = new Intent(AdminAllCommunities.this,
					AdminMessageBoard.class);
			intent.putExtra("CommunityId", selectedCommunityId);
			intent.putExtra("CommunityName", selectedCommunityName);
			startActivity(intent);
		}

		if (options == "View Grant Round Reports") {
			Intent intent = new Intent(AdminAllCommunities.this,
					GrantRoundsActivity.class);
			intent.putExtra("CommunityId", selectedCommunityId);
			startActivity(intent);
		}

		if (options == "Edit Community") {

			editCommunityDialog = new AdminEditCommunityDialog(
					selectedCommunityName, selectedPostalCode,
					selectedCommunityId);
			FragmentManager fm = getFragmentManager();
			editCommunityDialog.show(fm, "confirm");
		}
	}

	public void display() {
		if (isOnline()) {
			requestData(HttpManager.serverURL + "communities");
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

	// Handles the ListView clicks
	public class ListViewUserClickHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> list, View itemview,
				int position, long id) {

			Community selectedCommunity = (Community) list
					.getItemAtPosition(position);

			String communityName = selectedCommunity.getCommunityName();
			int communityId = selectedCommunity.getCommunityID();
			selectedPostalCode = selectedCommunity.getPostalCode();

			adminDialog = new AdminCommunityDialog(communityName, communityId);
			FragmentManager fm = getFragmentManager();
			adminDialog.show(fm, "confirm");
		}
	}

	private void setListView() {
		// Get reference to the listView
		ListView communityNameListView = (ListView) findViewById(R.id.ListViewCommunities);

		// Sorting list
		// call Collections.sort, load with userlist and comparator
		Collections.sort(communityList, new Comparator<Community>() {
			public int compare(Community c1, Community c2) {
				// Sort alphabetically by community name and return the new
				// order.
				return c1.getCommunityName().compareToIgnoreCase(
						c2.getCommunityName());
			}
		});
		// set adapter
		CommunityListAdapter adapter = new CommunityListAdapter(this,
				R.layout.admin_allusers_layout, communityList);
		// Bind the ListView to the above adapter
		communityNameListView.setAdapter(adapter);

	}

	// Once plus symbol clicked opens a dialogue fragment.
	public class AddCommunityButton implements OnClickListener {

		@Override
		public void onClick(View v) {
			newCommunityDialog = new AdminAddNewCommunity();
			FragmentManager fm = getFragmentManager();
			newCommunityDialog.show(fm, "confirm");
		}

	}

	/**************************************************************************************************************************************************************
	 * ASYNC TASKS
	 **************************************************************************************************************************************************************/

	/**
	 * Get community list
	 */
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

	/**
	 * Add an admin (POST)
	 */
	private class AddCommunityTask extends AsyncTask<String, String, String> {

		String newCommunityString = CommunityJSONParser
				.POST(newCommunityEntity);

		@Override
		protected void onPreExecute() {
			addCommunityTask.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			HttpManager.postData(
					HttpManager.serverURL + "communities",
					newCommunityString);
			String result = "new community added";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			addCommunityTask.remove(this);
			// start communites task
			display();
		}

		@Override
		protected void onProgressUpdate(String... values) {
		}
	}

	/**
	 * Edit an admin (PUT)
	 */
	private class EditCommunityTask extends AsyncTask<String, String, String> {

		private final String updateCommunityInfo;
		private final String communityID;
		private final String displayCommunityName;

		public EditCommunityTask(int communityId, String communityName,
				int postalCode) {
			ArrayMap<String, String> community = new ArrayMap<String, String>();
			String postCode = Integer.toString(postalCode);
			community.put("postalCode", postCode);
			community.put("communityName", communityName);
			communityID = Integer.toString(communityId);
			displayCommunityName = communityName;
			updateCommunityInfo = CommunityJSONParser.PUTCommunity(community);
		}

		@Override
		protected void onPreExecute() {
			editCommunityTask.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			HttpManager.updateData(
					HttpManager.serverURL + "communities/"
							+ communityID, updateCommunityInfo);
			String result = displayCommunityName + " updated";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			selectedPostalCode = 0;

			String messageResult = (result);

			editCommunityTask.remove(this);
			Toast.makeText(AdminAllCommunities.this, messageResult,
					Toast.LENGTH_LONG).show();
			// refresh
			display();
		}

		@Override
		protected void onProgressUpdate(String... values) {
		}
	}

}