package com.tyct.thankyoutrust;

import java.util.HashMap;

import com.tyct.thankyoutrust.model.Project;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AdminSingleProjectReport extends Activity {

	// Session Manager Class
	SessionManager session;
	
	public static String currApplicantEmail;
	public static String currProjectBlurb;
	public static String currApplicantName;
	public static String currOrganizationBlurb;
	public static String currFundsBlurb;
	public static String currCommunityID;
	public static String currProjectID;

	int projectRating;

	Project projectDisplayed;

	SharedPreferences prefs;

	boolean dialogResult;
	
	boolean admin = false;
	
	int userID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_project);

		// Session class instance
		session = new SessionManager(getApplicationContext());
		HashMap<String, String> userStored = session.getUserDetails();

		prefs = getSharedPreferences("UserDetails", MODE_PRIVATE);

		projectDisplayed = new Project();
		Intent intent = getIntent();
		Bundle allData = intent.getExtras();

		String currApplicantEmail = allData.getString("currApplicantEmail");
		int currProjectID = allData.getInt("currProjectID");
		int currCommunityID = allData.getInt("currCommunityID");
		String currApplicantName = allData.getString("currApplicantName");
		String currProjectBlurb = allData.getString("currProjectBlurb");
		String currFundsBlurb = allData.getString("currFundsBlurb");
		String currOrganizationBlurb = allData.getString("currOrganizationBlurb");

		projectDisplayed.setApplicantEmail(currApplicantEmail);
		projectDisplayed.setProjectID(currProjectID);
		projectDisplayed.setProjectBlurb(currProjectBlurb);
		projectDisplayed.setApplicantName(currApplicantName);
		projectDisplayed.setFundsBlurb(currFundsBlurb);
		projectDisplayed.setRoundID(currCommunityID);
		projectDisplayed.setOrganizationBlurb(currOrganizationBlurb);

		// Create new Fragments
		Fragment projectReportFrag = new AdminSingleProjectFragment();

		// projectDetailsFrag.setArguments(argument);

		// Create a fragment manager
		FragmentManager fm = getFragmentManager();

		// Create a new fragment transaction
		FragmentTransaction ft = fm.beginTransaction();

		// Replace the list container with the new details fragment
		ft.replace(R.id.fragment_container2, projectReportFrag);

		// Commit the transaction changes
		ft.commit();
		
		// get admin 
        int adminStatus = Integer.parseInt(userStored.get("admin"));
        
        //set admin 
        if(adminStatus == 1)
        {
        	admin = true;
        }
        
        userID = Integer.parseInt(userStored.get("id"));
		
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
			goTo = new Intent(this, Projects.class);
			startActivity(goTo);
			return true;
		case R.id.action_home:
			goTo = new Intent(this, HomeActivity.class);
			startActivity(goTo);
			finish();
			return true;
		case R.id.admin:
			goTo = new Intent(this, AdminHomePage.class);
			startActivity(goTo);
			return true;
		case R.id.action_profile:
			goTo = new Intent(this, ProfileActivity.class);
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

	public void setProjectRating(int returnedProjectRating) {
		projectRating = returnedProjectRating;
	}

	public int getProjectRating() {
		return projectRating;
	}


	// Method to retrieve the array of project names for use in the fragments
	public Project getSelectProject() {

		return projectDisplayed;
	}
}
