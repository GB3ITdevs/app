package com.tyct.thankyoutrust;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AdminHomePage extends Activity {

	// Session Manager Class
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_home_page);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		setUpAdminOptionsList();

		ListView selectedOption = (ListView) findViewById(R.id.lvDrawer);
		selectedOption.setOnItemClickListener(new ListViewIntentHandler());

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
			goTo = new Intent(AdminHomePage.this, Projects.class);
			startActivity(goTo);
			return true;
		case R.id.action_home:
			goTo = new Intent(AdminHomePage.this, HomeActivity.class);
			startActivity(goTo);
			finish();
			return true;
		case R.id.action_profile:
			goTo = new Intent(AdminHomePage.this, ProfileActivity.class);
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

	public void setUpAdminOptionsList() {
		String[] adminListOptions = { "View All Users", "View All Communities"};

		ArrayAdapter<String> adminOptionsAdapter = new ArrayAdapter<String>(
				AdminHomePage.this, android.R.layout.simple_list_item_1,
				adminListOptions);

		ListView optionsListView = (ListView) findViewById(R.id.lvDrawer);

		optionsListView.setAdapter(adminOptionsAdapter);

	}

	public class ListViewIntentHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> list, View itemview,
				int position, long id) {
			String clickedItemString = (String) list
					.getItemAtPosition(position).toString();

			Intent goToIntent;

			switch (clickedItemString) {
			case "View All Users":
				goToIntent = new Intent(AdminHomePage.this, AdminAllUsers.class);
				break;
			case "View All Communities":
				goToIntent = new Intent(AdminHomePage.this,
						AdminAllCommunities.class);
				break;
			default:
				goToIntent = null;
			}

			if (goToIntent != null)
				startActivity(goToIntent);

		}

	}
}
