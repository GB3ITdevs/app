package com.tyct.thankyoutrust;

import java.util.List;
import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EmptyProjectListFragment extends Fragment
{
	//Declare the class fields
	TextView tvCommunityName;
	List<Community> communities;
	int commID;
	Projects ma;
	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View v = inflater.inflate(R.layout.fragment_empty_projects_list,container,false);
			
			requestData();
			
			tvCommunityName = (TextView) v.findViewById(R.id.projectListCommunityTitle);
			
			ma = (Projects) getActivity();

			commID = ma.userCommunityID;

			return v;
		}
		
	 	//Method to create a and start a new task
	 	private void requestData() 
	 	{
	 		//Set the uri string
	 		String uri = "http://gb3it.pickworth.info:3000/communities";
	 		//Create the new async task
	 		GetCommunitiesTask ratingTask = new GetCommunitiesTask();
	 		//Start it using the url that has been passed into the method
	 		ratingTask.execute(uri);
	 	}
		
		public void setCommunityTitle()
		{
			for (Community com : communities)
			{
				if(com.getCommunityID() == commID)
				{
					tvCommunityName.setText(com.getCommunityName() + " Projects");
				}
			}
		}
		
	 	//Inner class for performing network activity - getting and setting project list from the database
		 	private class GetCommunitiesTask extends AsyncTask<String, String, String> 
		 	{
		 		
		 		//Tasks pre-execute method
		 		@Override
		 		protected void onPreExecute() 
		 		{

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
					//Create a new list of communities from the JSON parser using the passed in string from the http manager
					communities = CommunityJSONParser.parseFeed(result);
					setCommunityTitle();
		 		}
		 		
		 		@Override
		 		protected void onProgressUpdate(String... values) 
		 		{

		 		}
		 		
		 	}
}
