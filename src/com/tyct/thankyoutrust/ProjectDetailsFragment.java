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
import com.tyct.thankyourtrust.dialogs.ConfirmProjectRatingDialog;
import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.ProjectRating;
import com.tyct.thankyoutrust.parsers.ProjectRatingsJSONParser;

public class ProjectDetailsFragment extends Fragment
{
	public static String currProjectName;
		//Declare the class fields
		Project projectDisplayed;
		ProjectDetailsActivity ma;
		List<PostTask> posttasks;
		List<MyTask> tasks;
		List<ProjectRating> projectRatingList;
		ProjectRating projectRating;
		int infoID;
		RatingBar ratingBar;
		
		SharedPreferences prefs;
		
		ConfirmProjectRatingDialog confirmDialog;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			
			
			
			projectDisplayed = new Project();
			
			View v = inflater.inflate(R.layout.fragment_project_details,container,false);
			//Initialize the layout vies
			TextView tvProjectTitle = (TextView) v.findViewById(R.id.projectName);
			TextView tvPostalCode = (TextView) v.findViewById(R.id.postalCode);
			TextView tvApplicantName = (TextView) v.findViewById(R.id.applicantName);
			TextView tvFundsRequested = (TextView) v.findViewById(R.id.fundsRequested);
			TextView tvUseOfFunds = (TextView) v.findViewById(R.id.useOfFunds);
			TextView tvProjectBlurb = (TextView) v.findViewById(R.id.projectBlurb);
			ratingBar = (RatingBar) v.findViewById(R.id.projectRatingBar);
			
			//Retrieve the project selected from the activity
			ma = (ProjectDetailsActivity) getActivity();
			projectDisplayed = ma.getSelectProject();
			OnRatingBarChangeListener ratingChangeListener = new ratingChange();
			
			//Set the text views to display the project information
			tvProjectTitle.setText(projectDisplayed.getProjectName());
			tvPostalCode .setText(Integer.toString(projectDisplayed.getPostalCode()));
			tvApplicantName.setText(projectDisplayed.getApplicantName());
			tvFundsRequested.setText(Integer.toString(projectDisplayed.getFundsRequested()));
			tvUseOfFunds .setText(projectDisplayed.getUseOfFunds());
			tvProjectBlurb.setText(projectDisplayed.getProjectBlurb());
			ratingBar.setOnRatingBarChangeListener(ratingChangeListener);
			
			prefs = ma.prefs;
			
			//Get the logged in user info id from the shared preferences
			infoID = prefs.getInt("UserInfoId", 0);

			
			//Check if the project has already been rated and if so make it unavailable to rate
			checkForExistingRating();
			
			//Return the view
			return v;
		}
		
		
		// Connect to database
		protected boolean isOnline() {
			ConnectivityManager cm = (ConnectivityManager) ma.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				return true;
			} else {
				return false;
			}
		}
		
		

		
		
		//Method to populate rating bar if there is already a rating in the database
		public void checkForExistingRating()
		{
			if(isOnline())
			{
				tasks = new ArrayList<>();
				MyTask task = new MyTask();
				task.execute("http://gb3it.pickworth.info:3000/ratings");
			}
			

		}
		
		
		public void checkRetrievedRatings()
		{
			for (int i = 0; i < projectRatingList.size(); i++) 
			{
				if ((projectRatingList.get(i).getInfoID() == infoID)&&(projectRatingList.get(i).getProjectID()==projectDisplayed.getProjectID()))
				{
					ratingBar.setRating(projectRatingList.get(i).getRating());
					ratingBar.setIsIndicator(true);
				}
			}
		}
		
		
		public void setProjectRating()
		{
			ma.setProjectRating(projectRating.getRating());
		}
		
		//Inner Classes**********************************************************************************************
		
		//Method to set the new rating for the project
		public class ratingChange implements OnRatingBarChangeListener
		{

			//******************Need to check on parsing a float to int here*****************************************
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) 
			{
				if(fromUser == true)
				{		
					projectRating = new ProjectRating();
					
					int roundedRating = (int) rating;
					
					projectRating.setInfoID(infoID);
					
					projectRating.setProjectID(projectDisplayed.getProjectID());
					projectRating.setRating(roundedRating);
					
					setProjectRating();
					
					confirmDialog = new ConfirmProjectRatingDialog();
					FragmentManager fm = getFragmentManager();
					confirmDialog.show(fm, "confirm");
					
					if (ma.getDialogResult() == true)
					{
											
						if (isOnline()) 
						{
							posttasks = new ArrayList<>();
							PostTask task = new PostTask();
							task.execute();
						} 
						else 
						{
							Toast.makeText(ma, "Network isn't available.", Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						
						Toast.makeText(ma, "Please choose your new rating.", Toast.LENGTH_LONG).show();
					}
					
				}
			}
			
		}
		
	
		//ASync Tasks*************************************************************************************************
		
		// ASync task to post the new rating to the database
		private class PostTask extends AsyncTask<String, String, String> 
		{
			String ratingString = ProjectRatingsJSONParser.POST(projectRating);

			@Override
			protected void onPreExecute() 
			{
//				if (posttasks.size() == 0) 
//				{
//					pb.setVisibility(View.VISIBLE);
//				}
				posttasks.add(this);
			}

			@Override
			protected String doInBackground(String... params) 
			{
				HttpManager.postData("http://gb3it.pickworth.info:3000/ratings", ratingString);
				String result = "Rating Posted";
				return result;
			}

			@Override
			protected void onPostExecute(String result) {

				// String messageResult = (result);
				Toast.makeText(ma, result, Toast.LENGTH_LONG).show();
				posttasks.remove(this);
//				if (posttasks.size() == 0) {
//					pb.setVisibility(View.INVISIBLE);
//				}
				ratingBar.setIsIndicator(true);
			}

			@Override
			protected void onProgressUpdate(String... values) 
			{

			}

		}
		
		
		
		
		//ASync task to populate the rating bar if there is an existing rating stored
		private class MyTask extends AsyncTask<String, String, String> 
		{
			
			//Tasks pre-execute method
			@Override
			protected void onPreExecute() 
			{

				tasks.add(this);
			}
			
			//Tasks do in background method
			@Override
			protected String doInBackground(String... params) 
			{
				//Create a new string from the http managers get data method and return it
				String content = HttpManager.getData(params[0]);
				return content;
			}
			
			//Tasks post-execute method
			@Override
			protected void onPostExecute(String result) 
			{
				//Create a new list of project ratings from the JSON parser using the passed in string from the http manager
				projectRatingList = ProjectRatingsJSONParser.parseFeed(result);
				checkRetrievedRatings();
				//Remove the current task and set the progress bar to be invisible again
				tasks.remove(this);

			}
			
			@Override
			protected void onProgressUpdate(String... values) 
			{
//				updateDisplay(values[0]);
			}
			
		}
}
