package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;
import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AdminAllCommunities extends Activity {
	// Session Manager Class
	SessionManager session;

	List<Community> communityList;
	String[] communityNames;
	List<MyTask> tasks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_all_communities);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		tasks = new ArrayList<>();

		display();

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
			goTo = new Intent(AdminAllCommunities.this, MainActivity.class);
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
}
