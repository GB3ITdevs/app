package com.tyct.thankyoutrust;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tyct.thankyoutrust.model.GrantRound;
import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.ProjectRating;
import com.tyct.thankyoutrust.parsers.GrantRoundJSONParser;
import com.tyct.thankyoutrust.parsers.ProjectRatingsJSONParser;
import com.tyct.thankyoutrust.parsers.ProjectsJSONParser;

public class Projects extends Activity implements ProjectListFragment.Callbacks {
	List<GetProjectsTask> projectTasks;
	List<GetProjectRatingsTask> ratingTasks;

	List<Project> projectList;
	List<ProjectRating> projectRatingList;

	List<GrantRound> grantRounds;

	String[] projectNames;
	ProgressBar pb;
	Project selectedProject;

	// Session Manager Class
	SessionManager session;

	boolean admin = false;

	int userCommunityID;
	int userID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projects);

		// Create a new list of tasks
		projectTasks = new ArrayList<>();
		ratingTasks = new ArrayList<>();
		grantRounds = new ArrayList<>();

		// Initialize the progress bar and set it to not be visible
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);

		// Session class instance
		session = new SessionManager(getApplicationContext());
		HashMap<String, String> userStored = session.getUserDetails();

		// If the phone is online retrieve the projects info from the url
		if (isOnline()) {
			// Begin the request data method below
			requestData();

		}
		// Otherwise make a toast message to state the network is not available
		else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
		selectedProject = new Project();

		// Retrieve the logged in users communityID
		userCommunityID = Integer.parseInt(userStored.get("communityID"));

		// Retrieve the logged in users userID
		userID = Integer.parseInt(userStored.get("id"));

		// Retrieve whether the logged in user is an administrator
		int adminStatus = Integer.parseInt(userStored.get("admin"));

		// set admin
		if (adminStatus == 1) {
			admin = true;
		}

	}

	// TODO create the top menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!admin) {
			getMenuInflater().inflate(R.menu.main, menu);
		}

		if (admin) {
			getMenuInflater().inflate(R.menu.main_admin, menu);
		}
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

	// Method to create a and start a new task
	private void requestData() {

		String uri = HttpManager.serverURL+"grant_rounds";
		GetRoundsTask roundTask = new GetRoundsTask();
		roundTask.execute(uri);

		// Set the uri string
		uri = HttpManager.serverURL+"ratings";
		// Create the new async task
		GetProjectRatingsTask ratingTask = new GetProjectRatingsTask();
		// Start it using the url that has been passed into the method
		ratingTask.execute(uri);

		// Set the new uri string
		uri = HttpManager.serverURL+"projects";
		// Create the new async task
		GetProjectsTask task = new GetProjectsTask();
		// Start it using the url that has been passed into the method
		task.execute(uri);
	}

	// Method to replace the list fragment with the details fragment
	public void changeProjectDetails(int projectID) {
		for (int i = 0; i < projectList.size(); i++) {
			if (projectID == projectList.get(i).getProjectID()) {
				selectedProject = projectList.get(i);
			}
		}

		Intent detailIntent = new Intent(this, ProjectDetailsActivity.class);
		detailIntent.putExtra("currApplicantEmail",
				selectedProject.getApplicantEmail());
		detailIntent.putExtra("currProjectBlurb",
				selectedProject.getProjectBlurb());
		detailIntent.putExtra("currApplicantName",
				selectedProject.getApplicantName());
		detailIntent
				.putExtra("currOrganizationBlurb", selectedProject.getOrganizationBlurb());
		detailIntent.putExtra("currFundsBlurb",
				selectedProject.getFundsBlurb());
		detailIntent.putExtra("currCommunityID", selectedProject.getRoundID());
		detailIntent.putExtra("currProjectID", selectedProject.getProjectID());
		startActivity(detailIntent);

	}

	// Method to setup the project name list and display the project list
	// fragment
	public void setProjectList(List<Project> tempProjectList) {
		projectList = new ArrayList<>();

		// Create a new grant round
		GrantRound currentRound = new GrantRound();
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
		String todayString = timeFormat.format(new Date());

		// Loop through each grant round in the retrieved list
		for (GrantRound gR : grantRounds) {
			// if the community ID of the grant round matches the users
			// community ID
			if (gR.getCommunityID() == userCommunityID) {

				String startDate = gR.getStartDate();
				String endDate = gR.getEndDate();

				Date today = new Date();
				Date roundStart = new Date();
				Date roundEnd = new Date();

				try {
					roundStart = (Date) timeFormat.parse(startDate);
					roundEnd = (Date) timeFormat.parse(endDate);

				} catch (ParseException e) {
					e.printStackTrace();
				}

				if ((today.before(roundEnd)) && (today.after(roundStart))) {
					currentRound = gR;
				}
			}
		}

		String startDate = currentRound.getStartDate();
		String endDate = currentRound.getEndDate();

		Date today = new Date();
		Date roundStart = new Date();
		Date roundEnd = new Date();

		if ((currentRound.getRoundID() != 0)
				&& (currentRound.getCommunityID() != 0)) {
			try {
				roundStart = (Date) timeFormat.parse(startDate);
				roundEnd = (Date) timeFormat.parse(endDate);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		// Add each project name from the project list to the array of strings
		for (Project project : tempProjectList) {
			// If the project belongs to the same community as the logged in
			// user, add it to the list to display
			if (project.getRoundID() == currentRound.getRoundID()) {
				if ((today.before(roundEnd)) && (today.after(roundStart))) {
					projectList.add(project);
				}
			}
		}

		if ((projectList != null) && !(projectList.isEmpty())) {
			// Create new Fragments
			Fragment projectListFrag = new ProjectListFragment();

			// Create a fragment manager
			FragmentManager fm = getFragmentManager();

			// Create a new fragment transaction
			FragmentTransaction ft = fm.beginTransaction();

			// Replace the empty container with the fragment
			ft.replace(R.id.fragment_container1, projectListFrag);

			// Commit the transaction changes
			ft.commit();
		} else {
			emptyProjectListDisplay();
		}
	}

	// Method to show the empty project list fragment when there are no projects
	// to display
	public void emptyProjectListDisplay() {
		// Create new Fragments
		Fragment emptyProjectListFrag = new EmptyProjectListFragment();

		// Create a fragment manager
		FragmentManager fm = getFragmentManager();

		// Create a new fragment transaction
		FragmentTransaction ft = fm.beginTransaction();

		// Replace the empty container with the fragment
		ft.replace(R.id.fragment_container1, emptyProjectListFrag);

		// Commit the transaction changes
		ft.commit();
	}

	// Method to retrieve the array of projects for use in the fragments
	public List<Project> getProjectList() {

		return projectList;
	}

	// Method to retrieve the array of ratings for use in the fragments
	public List<ProjectRating> getProjectRatingList() {

		return projectRatingList;
	}

	// Method to retrieve the array of project names for use in the fragments
	// public String[] getProjects()
	// {
	//
	// return projectNames;
	// }

	// Method to retrieve the array of project names for use in the fragments
	public Project getSelectProject() {

		return selectedProject;
	}

	// Method to check if the phone is connected to a network and online
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onItemSelected(String id) {
		Intent detailIntent = new Intent(this, ProjectDetailsActivity.class);
		detailIntent.putExtra(ProjectDetailsFragment.currApplicantEmail,
				selectedProject.getApplicantEmail());
		detailIntent.putExtra(ProjectDetailsFragment.currProjectBlurb,
				selectedProject.getProjectBlurb());
		detailIntent.putExtra(ProjectDetailsFragment.currApplicantName,
				selectedProject.getApplicantName());
		detailIntent.putExtra(ProjectDetailsFragment.currOrganizationBlurb,
				selectedProject.getOrganizationBlurb());
		detailIntent.putExtra(ProjectDetailsFragment.currFundsBlurb,
				selectedProject.getFundsBlurb());
		detailIntent.putExtra(ProjectDetailsFragment.currCommunityID,
				selectedProject.getRoundID());
		detailIntent.putExtra(ProjectDetailsFragment.currProjectID,
				selectedProject.getProjectID());
		startActivity(detailIntent);
	}

	// Inner class for performing network activity - getting and setting project
	// list from the database
	private class GetProjectsTask extends AsyncTask<String, String, String> {
		List<Project> tempProjectList;

		// Tasks pre-execute method
		@Override
		protected void onPreExecute() {
			// If the task has been started set the progress bar to visible
			if (projectTasks.size() == 0) {
				pb.setVisibility(View.VISIBLE);
			}
			projectTasks.add(this);
		}

		// Tasks do in background method
		@Override
		protected String doInBackground(String... params) {
			// Create a new string from the http managers get data method and
			// return it
			String content = HttpManager.getData(params[0]);
			return content;
		}

		// Tasks post-execute method
		@Override
		protected void onPostExecute(String result) {
			if ((!result.isEmpty()) && !(result == null)) {
				// Create a new list of projects from the JSON parser using the
				// passed in string from the http manager
				tempProjectList = ProjectsJSONParser.parseFeed(result);
				// Populate the list fragment using the set project list method
				setProjectList(tempProjectList);
			} else {
				emptyProjectListDisplay();
			}
			// Remove the current task and set the progress bar to be invisible
			// again
			projectTasks.remove(this);
			if (projectTasks.size() == 0) {
				pb.setVisibility(View.INVISIBLE);
			}

		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}

	// Inner class for performing network activity - getting and setting project
	// list from the database
	private class GetProjectRatingsTask extends
			AsyncTask<String, String, String> {

		// Tasks pre-execute method
		@Override
		protected void onPreExecute() {
			// If the task has been started set the progress bar to visible
			if (ratingTasks.size() == 0) {
				pb.setVisibility(View.VISIBLE);
			}
			ratingTasks.add(this);
		}

		// Tasks do in background method
		@Override
		protected String doInBackground(String... params) {
			// Create a new string from the http managers get data method and
			// return it
			String content = HttpManager.getData(params[0]);
			return content;
		}

		// Tasks post-execute method
		@Override
		protected void onPostExecute(String result) {
			// Create a new list of project websites from the JSON parser using
			// the passed in string from the http manager
			projectRatingList = ProjectRatingsJSONParser.parseFeed(result);

			// Remove the current task and set the progress bar to be invisible
			// again
			ratingTasks.remove(this);
			if (ratingTasks.size() == 0) {
				pb.setVisibility(View.INVISIBLE);
			}

		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}

	// Inner class for performing network activity - getting and setting project
	// list from the database
	private class GetRoundsTask extends AsyncTask<String, String, String> {

		// Tasks pre-execute method
		@Override
		protected void onPreExecute() {

		}

		// Tasks do in background method
		@Override
		protected String doInBackground(String... params) {
			// Create a new string from the http managers get data method and
			// return it
			String content = HttpManager.getData(params[0]);
			return content;
		}

		// Tasks post-execute method
		@Override
		protected void onPostExecute(String result) {
			// Create a new list of communities from the JSON parser using the
			// passed in string from the http manager
			grantRounds = GrantRoundJSONParser.parseFeed(result);

		}

		@Override
		protected void onProgressUpdate(String... values) {

		}

	}
}
