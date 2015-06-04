package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.model.GrantRound;
import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.ProjectRating;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;
import com.tyct.thankyoutrust.parsers.GrantRoundJSONParser;

public class ProjectListFragment extends Fragment {
	// Declare the class fields

	List<Project> projectListUnsorted;
	List<Project> projectList;
	List<ProjectRating> ratings;
	List<Community> communities;

	Projects ma;
	ListView projectListView;
	int commID;
	TextView tvCommunityName;

	Animation anim;

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

		projectList = ma.getProjectList();

		ratings = ma.getProjectRatingList();

		int userID = ma.userID;
		commID = ma.userCommunityID;

		// Load animation
		anim = AnimationUtils.loadAnimation(ma, R.anim.scale_anim);

		tvCommunityName = (TextView) v
				.findViewById(R.id.projectListCommunityTitle);

		projectListView = (ListView) v.findViewById(R.id.projectListView);

		ProjectItemAdapter projectAdapter = new ProjectItemAdapter(
				getActivity(), R.layout.item_project, projectList, ratings,
				userID);

		OnItemClickListener listItemClick = new onListItemClick();

		projectListView.setAdapter(projectAdapter);

		projectListView.setOnItemClickListener(listItemClick);

		return v;
	}

	// Method to create a and start a new task
	private void requestData() {
		// Set the uri string
		String uri = HttpManager.serverURL+"communities";
		// Create the new async task
		GetCommunitiesTask ratingTask = new GetCommunitiesTask();
		// Start it using the url that has been passed into the method
		ratingTask.execute(uri);
	}

	public class onListItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			view.startAnimation(anim);

			Project clickedItem = (Project) projectListView
					.getItemAtPosition(position);

			// Project selectedProject = projectList.get(position);
			ma.changeProjectDetails(clickedItem.getProjectID());

			// mCallbacks.onItemSelected(clickedItem);
		}
	}

	public void setCommunityTitle() {
		for (Community com : communities) {
			if (com.getCommunityID() == commID) {
				tvCommunityName.setText(com.getCommunityName() + " Projects");
			}
		}
	}

	// Inner class for performing network activity - getting and setting project
	// list from the database
	private class GetCommunitiesTask extends AsyncTask<String, String, String> {

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
			communities = CommunityJSONParser.parseFeed(result);
			setCommunityTitle();
		}
	}
}