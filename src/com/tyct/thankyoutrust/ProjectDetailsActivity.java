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

public class ProjectDetailsActivity extends Activity 
{

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
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_project);
		
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
		
        
		
		//Create new Fragments
		Fragment projectDetailsFrag = new ProjectDetailsFragment();
		
		//projectDetailsFrag.setArguments(argument);
		
		//Create a fragment manager
		FragmentManager fm = getFragmentManager();
		
		//Create a new fragment transaction
		FragmentTransaction ft = fm.beginTransaction();
		
		//Replace the list container with the new details fragment
		ft.replace(R.id.fragment_container2, projectDetailsFrag);
		
		//Commit the transaction changes
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		if (id == android.R.id.home) 
		{
			NavUtils.navigateUpTo(this, new Intent(this, ProjectDetailsActivity.class));
			return true;
		}
		
		Intent goTo = new Intent();
		
		if (id == R.id.action_back) 
		{
			goTo = new Intent(ProjectDetailsActivity.this, Projects.class);
			finish();
			return true;
		}
		
		startActivity(goTo);
		return false;
	}
	
	public void setProjectRating(int returnedProjectRating)
	{
		projectRating = returnedProjectRating;
	}
	
	public int getProjectRating()
	{
		return projectRating;
	}
	
	public void setDialogResults(boolean returnedDialogResult)
	{
		dialogResult = returnedDialogResult;
	}
	
	public boolean getDialogResult()
	{
		return dialogResult;
	}
	
	
	//Method to retrieve the array of project names for use in the fragments
	public Project getSelectProject()
	{
		
		return projectDisplayed;
	}
}
