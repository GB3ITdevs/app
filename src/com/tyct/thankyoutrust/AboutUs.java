package com.tyct.thankyoutrust;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutUs extends Activity {

<<<<<<< HEAD
	// User Session Manager Class
    SessionManager session;
    
    boolean userIsLoggedIn;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);	
		
		 // Session class instance
        session = new SessionManager(getApplicationContext());
        userIsLoggedIn = session.isUserLoggedIn();
         
=======
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
>>>>>>> BlairDev
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
<<<<<<< HEAD
		
		if(userIsLoggedIn == false)
		{
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
		else
		{
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.about_us, menu);
			return true;
		}
=======
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_us, menu);
		return true;
>>>>>>> BlairDev
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent goTo = new Intent();
<<<<<<< HEAD
		switch(item.getItemId()) {
			case R.id.action_projects:
				goTo = new Intent(this, Projects.class);
				startActivity(goTo);
				return true;
			case R.id.action_home:
				goTo = new Intent(this, MainActivity.class);
				startActivity(goTo);
				finish();
				return true;
			case R.id.action_profile:
				goTo = new Intent(this, ProfileActivity.class);
				startActivity(goTo);
				return true;
			case R.id.action_about_us:
				goTo = new Intent(this, AboutUs.class);
				startActivity(goTo);
				return true;
			case R.id.action_logout:
				session.logoutUser();
				finish();
				return true;
			case R.id.action_back:
				goTo = new Intent(this, WelcomeScreen.class);
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
=======
		if (item.getItemId() == R.id.action_projects) 
		{
			goTo = new Intent(AboutUs.this, Projects.class);
			
		}
		if (item.getItemId() == R.id.action_home) 
		{
			goTo = new Intent(AboutUs.this, MainActivity.class);
		}
		if (item.getItemId() == R.id.action_about_us) 
		{
			goTo = new Intent(AboutUs.this, AboutUs.class);
		}
		startActivity(goTo);
		return false;
>>>>>>> BlairDev
	}
}
