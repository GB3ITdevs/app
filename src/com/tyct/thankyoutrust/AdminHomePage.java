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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_home_page);
		
		setUpAdminOptionsList();
		
		ListView selectedOption = (ListView) findViewById(R.id.lvDrawer);
		selectedOption.setOnItemClickListener(new ListViewIntentHandler());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent goTo = new Intent();
		if (item.getItemId() == R.id.action_projects) 
		{
			goTo = new Intent(AdminHomePage.this, Projects.class);
			
		}
		if (item.getItemId() == R.id.action_home) 
		{
			goTo = new Intent(AdminHomePage.this, MainActivity.class);
		}
		if (item.getItemId() == R.id.action_about_us) 
		{
			goTo = new Intent(AdminHomePage.this, AboutUs.class);
		}
		if (item.getItemId() == R.id.admin) 
		{
			goTo = new Intent(AdminHomePage.this, AdminHomePage.class);
		}
		startActivity(goTo);
		return false;
	}
	
	public void setUpAdminOptionsList()
	{
		String [] adminListOptions = {"View All Users", "View All Communities", "Reports"};
		
		ArrayAdapter<String> adminOptionsAdapter = new ArrayAdapter<String>(AdminHomePage.this, android.R.layout.simple_list_item_1, adminListOptions);
		
		ListView optionsListView = (ListView) findViewById(R.id.lvDrawer);
		
		optionsListView.setAdapter(adminOptionsAdapter);
		
	}
	
	public class ListViewIntentHandler implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> 
list, View itemview, int position,
				long id) {
			String clickedItemString = (String) list.getItemAtPosition(position).toString();
			
			Intent goToIntent;
			
			switch(clickedItemString){
			case "View All Users":
				goToIntent = new Intent(AdminHomePage.this, AdminAllUsers.class);
				break;
			case "View All Communities":
				goToIntent = new Intent(AdminHomePage.this, AdminAllCommunities.class);
				break;
			case "Reports":
				goToIntent = new Intent(AdminHomePage.this, AdminReports.class);
				break;
			default:
				goToIntent = null;
			}
			
			if(goToIntent != null)
				startActivity(goToIntent);
			
			
		}

		
	}
}
