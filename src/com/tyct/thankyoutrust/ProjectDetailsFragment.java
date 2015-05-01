package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tyct.thankyoutrust.dialogs.ConfirmProjectRatingDialog;
import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.ProjectNote;
import com.tyct.thankyoutrust.model.ProjectRating;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;
import com.tyct.thankyoutrust.parsers.ProjectNoteJSONParser;
import com.tyct.thankyoutrust.parsers.ProjectRatingsJSONParser;

public class ProjectDetailsFragment extends Fragment {
	
	private static final int REQ_CODE = 1;
	
	// Declare the class fields
	Project projectDisplayed;

	ProjectDetailsActivity ma;
	
	List<PostTask> posttasks;
	List<RetrieveProjectRatings> tasks;
	List<RetrieveProjectNotes> noteTasks;
	List<RetrieveCommunityTask> communityTasks;
	
	List<ProjectRating> projectRatingList;
	List<ProjectNote> noteList;
	ProjectRating projectRating;
	int userID;
	RatingBar ratingBar;
	
	TextView tvPostalCode;

	SharedPreferences prefs;

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

		View v = inflater.inflate(R.layout.fragment_project_details, container,
				false);
		// Initialize the layout vies
		TextView tvProjectTitle = (TextView) v.findViewById(R.id.projectName);
		tvPostalCode = (TextView) v.findViewById(R.id.postalCode);
		TextView tvApplicantName = (TextView) v
				.findViewById(R.id.applicantName);
		TextView tvFundsRequested = (TextView) v
				.findViewById(R.id.fundsRequested);
		TextView tvUseOfFunds = (TextView) v.findViewById(R.id.useOfFunds);
		TextView tvProjectBlurb = (TextView) v.findViewById(R.id.projectBlurb);
		ratingBar = (RatingBar) v.findViewById(R.id.projectRatingBar);

		// Retrieve the project selected from the activity
		ma = (ProjectDetailsActivity) getActivity();
		projectDisplayed = ma.getSelectProject();
		OnRatingBarChangeListener ratingChangeListener = new ratingChange();

		// Set the text views to display the project information
		tvProjectTitle.setText(projectDisplayed.getProjectName());
		
		tvApplicantName.setText(projectDisplayed.getApplicantName());
		tvFundsRequested.setText(Integer.toString(projectDisplayed
				.getFundsRequested()));
		tvUseOfFunds.setText(projectDisplayed.getUseOfFunds());
		tvProjectBlurb.setText(projectDisplayed.getProjectBlurb());
		ratingBar.setOnRatingBarChangeListener(ratingChangeListener);

		// Get the logged in user info id from the shared preferences
		userID = ma.userID;

		// Check if the project has already been rated and if so make it
		// unavailable to rate
		checkForExistingRating();
		
		//Method to retrieve the postal code for the community the project is listed in
		getCommunities();

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
	public void checkForExistingRating() {
		if (isOnline()) {
			tasks = new ArrayList<>();
			RetrieveProjectRatings task = new RetrieveProjectRatings();
			task.execute("http://gb3it.pickworth.info:3000/ratings");
		}

	}

	public void checkRetrievedRatings() {
		for (int i = 0; i < projectRatingList.size(); i++) {
			if ((projectRatingList.get(i).getUserID() == userID)
					&& (projectRatingList.get(i).getProjectID() == projectDisplayed
							.getProjectID())) {
				ratingBar.setRating(projectRatingList.get(i).getRating());
				ratingBar.setIsIndicator(true);
			}
		}
	}
	
	// Method to populate project notes if there is already any in the
	// database
	public void checkForExistingNotes() {
		if (isOnline()) {
			noteTasks = new ArrayList<>();
			RetrieveProjectRatings noteTask = new RetrieveProjectRatings();
			noteTask.execute("http://gb3it.pickworth.info:3000/ratings");
		}

	}

	public void checkRetrievedNotes() {
		for (int i = 0; i < noteList.size(); i++) {
			if ((noteList.get(i).getUserID() == userID)
					&& (noteList.get(i).getProjectID() == projectDisplayed
							.getProjectID())) {
//Need to set the notes to a textview here
			}
		}
	}

	public void setProjectRating() {
		ma.setProjectRating(projectRating.getRating());
	}
	
	public void getCommunities()
	{
		if(isOnline())
		{
			communityTasks = new ArrayList<>();
			RetrieveCommunityTask communityTask = new RetrieveCommunityTask();
			communityTask.execute("http://gb3it.pickworth.info:3000/communities");
		}		
	}
	
	public void getPostalCode(List<Community> communities)
	{
		
		for (Community community : communities) 
		{
			if(projectDisplayed.getCommunityID() == community.getCommunityID())
			{
				tvPostalCode.setText(Integer.toString(community.getPostalCode()));
			}
		}
	}
	
	//Method to handle the return from the dialog fragment
	public void setDialogResults(boolean result)
	{
		confirmDialog.dismiss();
		
		if (result == true) 
		{
			if (isOnline()) 
			{
				posttasks = new ArrayList<>();
				PostTask task = new PostTask();
				task.execute();
			} 
			else 
			{
				Toast.makeText(ma, "Network isn't available.",
						Toast.LENGTH_LONG).show();
			}
		} 
		else 
		{

			Toast.makeText(ma, "Rating not submitted.",
					Toast.LENGTH_LONG).show();
		}
	}

	// Inner
	// Classes**********************************************************************************************

	// Method to set the new rating for the project
	public class ratingChange implements OnRatingBarChangeListener {

		// ******************Need to check on parsing a float to int
		// here*****************************************
		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
			if (fromUser == true) {
				projectRating = new ProjectRating();

				int roundedRating = (int) rating;

				projectRating.setUserID(userID);

				projectRating.setProjectID(projectDisplayed.getProjectID());
				projectRating.setRating(roundedRating);

				setProjectRating();

				confirmDialog = new ConfirmProjectRatingDialog();
				FragmentManager fm = getFragmentManager();
				confirmDialog.setTargetFragment(ProjectDetailsFragment.this, REQ_CODE);
				confirmDialog.show(fm, "confirm");

			}
		}

	}

	// ASync
	// Tasks*************************************************************************************************

	// ASync task to post the new rating to the database
	private class PostTask extends AsyncTask<String, String, String> {
		String ratingString = ProjectRatingsJSONParser.POST(projectRating);

		@Override
		protected void onPreExecute() {
			// if (posttasks.size() == 0)
			// {
			// pb.setVisibility(View.VISIBLE);
			// }
			posttasks.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			HttpManager.postData("http://gb3it.pickworth.info:3000/ratings",
					ratingString);
			String result = "Rating Posted";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			// String messageResult = (result);
			Toast.makeText(ma, result, Toast.LENGTH_LONG).show();
			posttasks.remove(this);
			// if (posttasks.size() == 0) {
			// pb.setVisibility(View.INVISIBLE);
			// }
			ratingBar.setIsIndicator(true);
		}

		@Override
		protected void onProgressUpdate(String... values) {

		}

	}

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
			checkRetrievedRatings();
			// Remove the current task and set the progress bar to be invisible
			// again
			tasks.remove(this);

		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}
	
	// ASync task to populate the project notes if there is an existing note
		// stored
		private class RetrieveProjectNotes extends AsyncTask<String, String, String> {

			// Tasks pre-execute method
			@Override
			protected void onPreExecute() {

				noteTasks.add(this);
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
				noteList = ProjectNoteJSONParser.parseFeed(result);
				checkRetrievedNotes();
				// Remove the current task and set the progress bar to be invisible
				// again
				noteTasks.remove(this);

			}

			@Override
			protected void onProgressUpdate(String... values) {
				// updateDisplay(values[0]);
			}

		}
		
		
		// ASync task to populate the rating bar if there is an existing rating
		// stored
		private class RetrieveCommunityTask extends AsyncTask<String, String, String> {

			// Tasks pre-execute method
			@Override
			protected void onPreExecute() {

				communityTasks.add(this);
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
				List<Community> communityList = CommunityJSONParser.parseFeed(result);
				getPostalCode(communityList);
				// Remove the current task and set the progress bar to be invisible
				// again
				communityTasks.remove(this);

			}

			@Override
			protected void onProgressUpdate(String... values) {
				// updateDisplay(values[0]);
			}

		}
		
}
