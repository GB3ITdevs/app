package com.tyct.thankyoutrust;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.tyct.thankyoutrust.model.Project;

public class ProjectDetailsActivity extends Activity {

	// Session Manager Class
	SessionManager session;
	
	public static String currProjectName;
	public static String currProjectBlurb;
	public static String currApplicantName;
	public static String currUseOfFunds;
	public static String currFundsRequested;
	public static String currPostalCode;
	public static String currProjectID;

	int projectRating;

	Project projectDisplayed;

	SharedPreferences prefs;

	boolean dialogResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_project);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		prefs = getSharedPreferences("UserDetails", MODE_PRIVATE);

		projectDisplayed = new Project();
		Intent intent = getIntent();
		Bundle allData = intent.getExtras();

		String currProjectName = allData.getString("currProjectName");
		int currProjectID = allData.getInt("currProjectID");
		int currPostalCode = allData.getInt("currPostalCode");
		String currApplicantName = allData.getString("currApplicantName");
		String currProjectBlurb = allData.getString("currProjectBlurb");
		int currFundsRequested = allData.getInt("currFundsRequested");
		String currUseOfFunds = allData.getString("currUseOfFunds");

		projectDisplayed.setProjectName(currProjectName);
		projectDisplayed.setProjectID(currProjectID);
		projectDisplayed.setProjectBlurb(currProjectBlurb);
		projectDisplayed.setApplicantName(currApplicantName);
		projectDisplayed.setFundsRequested(currFundsRequested);
		projectDisplayed.setPostalCode(currPostalCode);
		projectDisplayed.setUseOfFunds(currUseOfFunds);

		// Create new Fragments
		Fragment projectDetailsFrag = new ProjectDetailsFragment();

		// projectDetailsFrag.setArguments(argument);

		// Create a fragment manager
		FragmentManager fm = getFragmentManager();

		// Create a new fragment transaction
		FragmentTransaction ft = fm.beginTransaction();

		// Replace the list container with the new details fragment
		ft.replace(R.id.fragment_container2, projectDetailsFrag);

		// Commit the transaction changes
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			goTo = new Intent(this, MainActivity.class);
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
		case R.id.action_about_us:
			goTo = new Intent(this, AboutUs.class);
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

	public void setDialogResults(boolean returnedDialogResult) {
		dialogResult = returnedDialogResult;
	}

	public boolean getDialogResult() {
		return dialogResult;
	}

	// Method to retrieve the array of project names for use in the fragments
	public Project getSelectProject() {

		return projectDisplayed;
	}
}
