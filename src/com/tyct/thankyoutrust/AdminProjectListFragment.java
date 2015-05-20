package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.ProjectRating;

public class AdminProjectListFragment extends Fragment {
	// Declare the class fields

	List<Project> projectListUnsorted;
	List<Project> projectList;
	List<ProjectRating> ratings;
	//String[] projectNames;
	AdminReports ma;
	ListView projectListView;
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
		View v = inflater.inflate(R.layout.fragment_admin_project_list, container,
				false);

		ma = (AdminReports) getActivity();
		
		//get all data from passed in intent
		Intent intent = ma.getIntent();
		Bundle allData = intent.getExtras();

		//get data
		int roundID = allData.getInt("CommunityId");//*********************************************************************Replace with round ID
		//String communityName = allData.getString("CommunityName");
		
		projectListUnsorted = ma.getProjectList();
		projectList = new ArrayList<>();
		
		ratings = ma.getProjectRatingList();
		
		for (Project project : projectListUnsorted) 
		{
			if(project.getRoundID() == roundID)
			{
				projectList.add(project);
			}
		}

		projectListView = (ListView) v.findViewById(R.id.projectListView);

		AdminProjectItemAdapter projectAdapter = new AdminProjectItemAdapter(getActivity(), R.layout.item_project, projectList ,ratings);
		
		
		OnItemClickListener listItemClick = new onListItemClick();

		projectListView.setAdapter(projectAdapter);

		projectListView.setOnItemClickListener(listItemClick);

		return v;
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
}