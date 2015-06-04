package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tyct.thankyoutrust.dialogs.ConfirmProjectRatingDialog;
import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.ProjectNote;
import com.tyct.thankyoutrust.model.ProjectRating;
import com.tyct.thankyoutrust.model.ProjectWebsite;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;
import com.tyct.thankyoutrust.parsers.ProjectNoteJSONParser;
import com.tyct.thankyoutrust.parsers.ProjectRatingsJSONParser;
import com.tyct.thankyoutrust.parsers.ProjectWebsiteJSONParser;

public class ProjectDetailsFragment extends Fragment {
	
	private static final int REQ_CODE = 1;
	
	// Declare the class fields
	Project projectDisplayed;

	ProjectDetailsActivity ma;
	
	List<PostTask> posttasks;
	List<RetrieveProjectRatings> tasks;
	List<RetrieveProjectNotes> noteTasks;
	List<RetrieveCommunityTask> communityTasks;
	List<PostProjectNotes> postProjectTasks;
	
	List<ProjectRating> projectRatingList;
	List<ProjectNote> noteList;
	ProjectRating projectRating;
	int userID;
	RatingBar ratingBar;
	
	ListView tvProjectNotes;
	
	String websiteAddress;

	ProjectNote noteEntity;
	EditText noteData;
	

	ConfirmProjectRatingDialog confirmDialog;

	public static String currApplicantEmail;
	public static String currProjectID;
	public static String currCommunityID;
	public static String currApplicantName;
	public static String currProjectBlurb;
	public static String currFundsBlurb;
	public static String currOrganizationBlurb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		projectDisplayed = new Project();

		View v = inflater.inflate(R.layout.fragment_project_details, container,
				false);
		// Initialize the layout vies
		TextView tvOrganizationBlurb = (TextView) v.findViewById(R.id.organizationBlurb);
		TextView tvApplicantName = (TextView) v
				.findViewById(R.id.applicantName);
		TextView tvFundsRequested = (TextView) v
				.findViewById(R.id.fundsRequested);
		TextView tvProjectBlurb = (TextView) v.findViewById(R.id.projectBlurb);
		ratingBar = (RatingBar) v.findViewById(R.id.projectRatingBar);
		Button postNote = (Button) v.findViewById(R.id.btnPostNotes);
		// Get Text from editText field
		noteData = (EditText) v.findViewById(R.id.textProjectNotes);
		noteData.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.setFocusable(true);
	            v.setFocusableInTouchMode(true);
	            return false;
			}
		});
		
		//ProjectNotes textview
		tvProjectNotes = (ListView) v.findViewById(R.id.projectDetailsNotes);
				
		// Retrieve the project selected from the activity
		ma = (ProjectDetailsActivity) getActivity();
		projectDisplayed = ma.getSelectProject();
		OnRatingBarChangeListener ratingChangeListener = new ratingChange();

		// Set the text views to display the project information
		
		tvApplicantName.setText(projectDisplayed.getApplicantName());
		tvFundsRequested.setText(projectDisplayed.getFundsBlurb());
		tvProjectBlurb.setText(projectDisplayed.getProjectBlurb());
		tvOrganizationBlurb.setText(projectDisplayed.getOrganizationBlurb());
		ratingBar.setOnRatingBarChangeListener(ratingChangeListener);
		
		OnClickListener postNoteListener = new postNoteHandler();
		postNote.setOnClickListener(postNoteListener);


		// Get the logged in user info id from the details activity
		userID = ma.userID;

		// Check if the project has already been rated and if so make it
		// unavailable to rate
		checkForExistingRating();
		
		//Method to retrieve the postal code for the community the project is listed in
		getCommunities();
		
		//Method to check for existing notes that the user has entered about the current project
		checkForExistingNotes();

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
			RetrieveProjectNotes noteTask = new RetrieveProjectNotes();
			noteTask.execute("http://gb3it.pickworth.info:3000/project_notes");
		}

	}

	public void checkRetrievedNotes() {
		List<ProjectNote> sortedNotes = new ArrayList<ProjectNote>();
		
		for(ProjectNote note : noteList)
		{
			if ((note.getUserID() == userID)&& (note.getProjectID() == projectDisplayed.getProjectID())) 
			{
				sortedNotes.add(note);
			}
		}
		
		String[] noteArray = new String[sortedNotes.size()];
		int count = 0;
		
		for(ProjectNote note : sortedNotes)
		{
				noteArray[count] = note.getNote();
				count++;
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<>(ma, android.R.layout.simple_list_item_1, noteArray);
		
		tvProjectNotes.setAdapter(adapter);

		
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
			if(projectDisplayed.getRoundID() == community.getCommunityID())//***********************************************************************
			{
				//tvPostalCode.setText(Integer.toString(community.getPostalCode()));
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
	
	public class postNoteHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			// Put Text into string form
			String noteString = noteData.getText().toString();

			// If statement, checks to make sure that user has put something
			// into the edit text field
			if (noteString.equals("")) {
				// If Edit Text is empty it will show a toast
				Toast.makeText(ma, "Please enter a note about this project", Toast.LENGTH_LONG).show();

			} else {
				// If Edit Text is not empty it will Post to the new project note to
				// the database

				// Create new Note Object, then pass data into sets
				noteEntity = new ProjectNote();
				// Passes the infoID
				noteEntity.setUserID(userID);
				
				noteEntity.setProjectID(projectDisplayed.getProjectID());
				
				noteEntity.setNote(noteString);

				// Calls the PostTask Method, and posts the commentEntity
				postProjectTasks = new ArrayList<>();
				PostProjectNotes task = new PostProjectNotes();
				task.execute();
				// Sets the Edit Text field to empty
				noteData.setText("");

				// refresh the display for the list view.

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
			//Toast.makeText(ma, result, Toast.LENGTH_LONG).show();
			posttasks.remove(this);
			// if (posttasks.size() == 0) {
			// pb.setVisibility(View.INVISIBLE);
			// }
			ratingBar.setIsIndicator(true);
			
			Intent intent = new Intent(ma, Projects.class);
			startActivity(intent);
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
				
				
				// ASync task to post enter project notes
				private class PostProjectNotes extends AsyncTask<String, String, String> {
					String noteEntityString = ProjectNoteJSONParser.POST(noteEntity);

					@Override
					protected void onPreExecute() {
						// updateDisplay("Starting task");

						if (postProjectTasks.size() == 0) {
							
						}
						postProjectTasks.add(this);
					}

					@Override
					protected String doInBackground(String... params) {
						HttpManager.postData("http://gb3it.pickworth.info:3000/project_notes", noteEntityString);
						String result = "Note Posted";
						return result;
					}

					@Override
					protected void onPostExecute(String result) {

						// String commentResult = (result);
						Toast.makeText(ma, result, Toast.LENGTH_LONG).show();
						postProjectTasks.remove(this);
						if (postProjectTasks.size() == 0) {
							
						}
						
						checkForExistingNotes();
					}

					@Override
					protected void onProgressUpdate(String... values) {
						
					}

				}

		
}
