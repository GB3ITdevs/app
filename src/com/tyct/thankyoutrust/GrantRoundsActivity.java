package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tyct.thankyoutrust.dialogs.AdminAddGrantRoundDialog;
import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.model.GrantRound;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;
import com.tyct.thankyoutrust.parsers.GrantRoundJSONParser;

public class GrantRoundsActivity extends Activity {
	// Session Manager Class
	SessionManager session;

	private int mCommunityID;

	private List<GrantRound> grantRoundList;
	private List<GrantRound> communityRoundList;
	private List<Community> communityList;
	
	String communityName;

	// Dialog Fragments
	AdminAddGrantRoundDialog newGrantRoundDialog;

	boolean dialogResult;
	
	GrantRound newGrantRoundEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grant_rounds);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		// get all data from passed in intent
		Intent intent = getIntent();
		Bundle allData = intent.getExtras();

		// get data
		mCommunityID = allData.getInt("CommunityId");

		grantRoundList = new ArrayList<>();
		communityRoundList = new ArrayList<>();

		retrieveGrantRounds();
		retrieveCommunities();

		// Clickable ListView of grant rounds for this community
		ListView roundList = (ListView) findViewById(R.id.listViewGrantRounds);
		roundList.setOnItemClickListener(new ListViewUserClickHandler());

		// add a community button
		ImageView addButton = (ImageView) findViewById(R.id.imageBtnAddGrantRound);
		addButton.setOnClickListener(new AddGrantRoundButton());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_admin, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent goTo = new Intent();
		switch (item.getItemId()) {
		case R.id.action_projects:
			goTo = new Intent(GrantRoundsActivity.this, Projects.class);
			startActivity(goTo);
			return true;
		case R.id.action_home:
			goTo = new Intent(GrantRoundsActivity.this, HomeActivity.class);
			startActivity(goTo);
			finish();
			return true;
		case R.id.admin:
			goTo = new Intent(GrantRoundsActivity.this, AdminHomePage.class);
			startActivity(goTo);
			return true;
		case R.id.action_profile:
			goTo = new Intent(GrantRoundsActivity.this, ProfileActivity.class);
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

	public void retrieveGrantRounds() {
		if (isOnline()) {
			requestData(HttpManager.serverURL+"grant_rounds");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void retrieveCommunities() {
		if (isOnline()) {
			requestCommunityData(HttpManager.serverURL+"communities");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}

	private void requestData(String uri) {
		GrantRoundTask task = new GrantRoundTask();
		task.execute(uri);
	}

	private void requestCommunityData(String uri) {
		CommunityTask task = new CommunityTask();
		task.execute(uri);
	}

	// Method to return data to the Dialog Fragment
	public void addNewGrantRound(boolean result, String startDate,
			String endDate) {
		newGrantRoundDialog.dismiss();

		if (result == true) {
			newGrantRoundEntity = new GrantRound();
			newGrantRoundEntity.setCommunityID(mCommunityID);
			newGrantRoundEntity.setStartDate(startDate);
			newGrantRoundEntity.setEndDate(endDate);

			AddGrantRoundTask task = new AddGrantRoundTask();
			task.execute();
		}
	}

	private void setListView() {
		// Get reference to the listView
		ListView grantRoundListView = (ListView) findViewById(R.id.listViewGrantRounds);

		if (!communityRoundList.isEmpty()) {
			ArrayAdapter<GrantRound> adapter = new GrantRoundListAdapter(this,
					android.R.layout.simple_list_item_1, communityRoundList);
			// Bind the ListView to the above adapter
			grantRoundListView.setAdapter(adapter);
		}
	}

	// Handles the ListView clicks
	public class ListViewUserClickHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> list, View itemview,
				int position, long id) {

			GrantRound selectedRound = (GrantRound) list
					.getItemAtPosition(position);

			Intent intent = new Intent(GrantRoundsActivity.this,
					AdminReports.class);
			intent.putExtra("RoundId", selectedRound.getRoundID());
			intent.putExtra("CommunityName", communityName);
			intent.putExtra("startDate", selectedRound.getStartDate());
			intent.putExtra("endDate", selectedRound.getEndDate());
			startActivity(intent);
		}
	}

	// Once plus symbol clicked opens a dialogue fragment.
	public class AddGrantRoundButton implements OnClickListener {

		@Override
		public void onClick(View v) {
			newGrantRoundDialog = new AdminAddGrantRoundDialog();
			FragmentManager fm = getFragmentManager();
			newGrantRoundDialog.show(fm, "confirm");
		}

	}

	/**
	 * Get list of grant rounds
	 */
	private class GrantRoundTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			communityRoundList.clear();
		}

		@Override
		protected String doInBackground(String... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}

		@Override
		protected void onPostExecute(String result) {

			grantRoundList = GrantRoundJSONParser.parseFeed(result);

			for (GrantRound grantRound : grantRoundList) {
				if (grantRound.getCommunityID() == mCommunityID) {
					communityRoundList.add(grantRound);
				}
			}

			setListView();
		}
	}

	/**
	 * Get list of communities
	 */
	private class CommunityTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			communityList = CommunityJSONParser.parseFeed(result);

			communityName = "";
			for (Community community : communityList) {
				if (community.getCommunityID() == mCommunityID) {
					communityName = community.getCommunityName() + " ";
					break;
				}
			}

			TextView title = (TextView) findViewById(R.id.textViewGrantRounds);
			title.setText(communityName + "Community Grant Round Reports");
		}
	}
	
	/**
	 * Add a grant round (POST)
	 */
	private class AddGrantRoundTask extends AsyncTask<String, String, String> {

		String newGrantRoundString = GrantRoundJSONParser
				.POST(newGrantRoundEntity);

		@Override
		protected String doInBackground(String... params) {
			HttpManager.postData(
					HttpManager.serverURL+"grant_rounds",
					newGrantRoundString);
			String result = "success";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {			
			// refresh
			retrieveGrantRounds();
		}
	}
}
