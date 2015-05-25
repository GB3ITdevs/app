package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tyct.thankyoutrust.dialogs.ConfirmProjectRatingDialog;
import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.ProjectNote;
import com.tyct.thankyoutrust.model.ProjectRating;
import com.tyct.thankyoutrust.model.User;
import com.tyct.thankyoutrust.parsers.ProjectRatingsJSONParser;
import com.tyct.thankyoutrust.parsers.UserJSONParser;

public class AdminSingleProjectFragment extends Fragment {
	
	// Declare the class fields
	Project projectDisplayed;

	AdminSingleProjectReport ma;
	
	List<RetrieveProjectRatings> tasks;
	List<RetrieveCommunityUsersTask> communityUserTasks;
	
	List<ProjectRating> projectRatingList;
	List<ProjectNote> noteList;
	
	List<User> usersInCommunity;
	List<User> usersRated;
	List<User> usersUnrated;
	
	ProjectRating projectRating;
	int userID;
	RatingBar ratingBar;
	
	TextView tvPostalCode;
	TextView tvProjectNotes;
	
	Button viewWebsite;
	
	String websiteAddress;

	ProjectNote noteEntity;
	EditText noteData;
	
	ListView notRatedBy;
	
	AdministrativeCalculations calculate;

	ConfirmProjectRatingDialog confirmDialog;

	public static String currProjectName;
	public static String currProjectID;
	public static String currCommunityID;
	public static String currApplicantName;
	public static String currProjectBlurb;
	public static String currFundsRequested;
	public static String currUseOfFunds;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		projectDisplayed = new Project();

		View v = inflater.inflate(R.layout.fragment_admin_project_report, container,
				false);
		// Retrieve the project selected from the activity
		ma = (AdminSingleProjectReport) getActivity();
		projectDisplayed = ma.getSelectProject();	
		
		getRatings();
		
		// Initialize the layout vies
		TextView tvProjectTitle = (TextView) v.findViewById(R.id.projectName);

		TextView tvApplicantName = (TextView) v.findViewById(R.id.applicantName);
		TextView tvProjectBlurb = (TextView) v.findViewById(R.id.projectBlurb);
		ratingBar = (RatingBar) v.findViewById(R.id.projectRatingBar);
		
		notRatedBy = (ListView) v.findViewById(R.id.notYetRatedList);
		
		usersInCommunity = new ArrayList<User>();
		usersRated = new ArrayList<User>();
		usersUnrated = new ArrayList<User>();	
				


		// Set the text views to display the project information
		tvProjectTitle.setText(projectDisplayed.getProjectName());
		
		tvApplicantName.setText(projectDisplayed.getApplicantName());
		tvProjectBlurb.setText(projectDisplayed.getProjectBlurb());

		// Get the logged in user info id from the details activity
		userID = ma.userID;
		
		//Method to retrieve the postal code for the community the project is listed in
		getUsers();

		// Return the view
		return v;
	}

	// Connect to database
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) ma
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	// Method to populate rating bar if there is already a rating in the
	// database
	public void getRatings() {
		if (isOnline()) {
			tasks = new ArrayList<>();
			RetrieveProjectRatings task = new RetrieveProjectRatings();
			task.execute("http://gb3it.pickworth.info:3000/ratings");
		}

	}

//**************************************************************************************************************user community from grant round not project
	//Method to organise whether user have rated the current project or not
	public void organiseUsers(List<User> allUsers)
	{
		//Loop for the list of all users passed in to the method
		for (User user : allUsers)
		{
			//If the current users community ID is the same as the displayed project...
			if (user.getCommunityID() == projectDisplayed.getRoundID())
			{
				//Add the user to the list of users in the community
				usersInCommunity.add(user);
				//Then loop through each rating in the list of all ratings
				for (ProjectRating rating : projectRatingList)
				{
					//If the ratings user ID matches the users ID and the rating project ID is the same as the project being displayed...
					if((rating.getUserID() == user.getUserID())&&(rating.getProjectID() == projectDisplayed.getProjectID()))
					{
						//Add the user to the list of users that have rated the project
						usersRated.add(user);
					}
				}
			}
		}
		
		//Then for each user that is a part of the community that the project belongs to...
		for (User user : usersInCommunity)
		{
			//Start a count at 0
			int count = 0;
			//Then for each user in the list of users who have rated the project...
			for(User u : usersRated)
			{
				//If the user in the community has an ID that does not match the user who has rated the project...
				if (user.getUserID() == u.getUserID())
				{
					//Increase the count by 1
					count++;
				}
			}
			
			//If the total count for the user in the community equals the total number of users who have rated the project...
			if(count != 1)
			{
				//Then the user has not yet rated the project and is added to the list of users who have not rated it
				usersUnrated.add(user);
			}
		}
		
		//Create a new string array the same size as the list of unrated users
		String[] userNames = new String[usersUnrated.size()];
		
		//If the list of unrated users is not empty...
		if(usersUnrated != null)
		{
			//Loop through the list of users who have not rated the project...
			 for(int i = 0; i < usersUnrated.size(); i++)
			 {
				 //And add the first and last name of the user to the array of strings
				 userNames[i] = (usersUnrated.get(i).getFirstName() + " " + usersUnrated.get(i).getLastName());
			 }
		}
		
		//If the list of unrated users is empty as all users have rated the project...
		if(usersUnrated.isEmpty() == true)
		{
			//Re-initialize the string array at a size of one
			userNames = new String[1];
			//Then set the only entry to show a string stating all users have rated the project
			userNames[0] = "Everyone has rated this project.";
		}
		
		//Create a new array adapter for the array of user names
		ArrayAdapter<String> adapter = new ArrayAdapter<>(ma, android.R.layout.simple_list_item_1, userNames);
		//Set the adapter to be displayed by the listview
		notRatedBy.setAdapter(adapter);
	}

	public void setProjectRating() {
		calculate = new AdministrativeCalculations();
		int averageRating = calculate.CalculateAverageRating(projectRatingList, projectDisplayed.getProjectID());
		//ma.setProjectRating(averageRating);
		ratingBar.setRating(averageRating);
		ratingBar.setIsIndicator(true);
	}
	
	
	public void getUsers()
	{
		if(isOnline())
		{
			communityUserTasks = new ArrayList<>();
			RetrieveCommunityUsersTask communityUsersTask = new RetrieveCommunityUsersTask();
			communityUsersTask.execute("http://gb3it.pickworth.info:3000/users");
		}		
	}
	

	// Inner
	// Classes**********************************************************************************************
	
	public class ViewWebsiteListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v) 
		{
			Uri goToProjectSite = Uri.parse(websiteAddress);
			
			Intent websiteIntent = new Intent(Intent.ACTION_VIEW, goToProjectSite);
			
			startActivity(websiteIntent);
			
		}

		
	}


	// ASync
	// Tasks*************************************************************************************************

	// ASync task to populate the rating bar if there is an existing rating
	// stored
	private class RetrieveProjectRatings extends AsyncTask<String, String, String> {

		// Tasks pre-execute method
		@Override
		protected void onPreExecute() {

			tasks.add(this);
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
			// Create a new list of project ratings from the JSON parser using
			// the passed in string from the http manager
			projectRatingList = ProjectRatingsJSONParser.parseFeed(result);
			
			setProjectRating();
			// Remove the current task and set the progress bar to be invisible
			// again
			tasks.remove(this);

		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}
	
				
				// ASync task to retrieve a list of all users from with the community
				private class RetrieveCommunityUsersTask extends AsyncTask<String, String, String> {

					// Tasks pre-execute method
					@Override
					protected void onPreExecute() {

						communityUserTasks.add(this);
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
						// Create a new list of users from the JSON parser using
						// the passed in string from the http manager
						List<User> userList = UserJSONParser.parseFeed(result);
						
						organiseUsers(userList);
						// Remove the current task and set the progress bar to be invisible
						// again
						communityUserTasks.remove(this);

					}

				}
						
}
