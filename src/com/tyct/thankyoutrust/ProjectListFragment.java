package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.ProjectRating;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;

public class ProjectListFragment extends Fragment {
	// Declare the class fields

	List<Project> projectListUnsorted;
	List<Project> projectList;
	List<ProjectRating> ratings;
	List<Community> communities;
	//String[] projectNames;
	Projects ma;
	ListView projectListView;
	int roundID;
	TextView tvCommunityName;
	
	private Callbacks mCallbacks = sCallbacks;

	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	private static Callbacks sCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_project_list, container,
				false);
		
		requestData();

		ma = (Projects) getActivity();
		
		projectListUnsorted = ma.getProjectList();
		projectList = new ArrayList<>();
		
		ratings = ma.getProjectRatingList();
		
		int userID = ma.userID;
		roundID = ma.userCommunityID;//**********************************************************************************************
		
		for (Project project : projectListUnsorted) 
		{
			if(project.getRoundID() == roundID)
			{
				projectList.add(project);
			}
		}

		tvCommunityName = (TextView) v.findViewById(R.id.projectListCommunityTitle);
		
		projectListView = (ListView) v.findViewById(R.id.projectListView);

		ProjectItemAdapter projectAdapter = new ProjectItemAdapter(getActivity(), R.layout.item_project, projectList ,ratings, userID);
		
		
		OnItemClickListener listItemClick = new onListItemClick();

		projectListView.setAdapter(projectAdapter);

		projectListView.setOnItemClickListener(listItemClick);

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

	public class onListItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Project clickedItem = (Project) projectListView.getItemAtPosition(
					position);
			
			// Project selectedProject = projectList.get(position);
			ma.changeProjectDetails(clickedItem.getProjectID());

			// mCallbacks.onItemSelected(clickedItem);

		}

	}
	
	public void setCommunityTitle()
	{
		for (Community com : communities)
		{
			if(com.getCommunityID() == roundID)
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