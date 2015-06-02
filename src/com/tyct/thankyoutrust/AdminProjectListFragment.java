package com.tyct.thankyoutrust;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
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

import com.tyct.thankyoutrust.dialogs.AdminProjectOptionsDialog;
import com.tyct.thankyoutrust.model.GrantRound;
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
	List<GrantRound> grantRounds;
	
	Animation anim;
	
	
	//AdminProjectOptionsDialog optionsDialog;
	boolean dialogResult;
	
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
		int roundID = allData.getInt("RoundId");
		String communityName = allData.getString("CommunityName");
		String startDate = allData.getString("startDate");
		String endDate = allData.getString("endDate");
		
		projectListUnsorted = ma.getProjectList();
		projectList = new ArrayList<>();
		
		ratings = ma.getProjectRatingList();
 		
 		//Add each project name from the project list to the array of strings
 		for(Project project : projectListUnsorted)
 		{
 			//If the project belongs to the same community as the logged in user, add it to the list to display
 			if (project.getRoundID() == roundID)
 			{
					projectList.add(project);
 			}
 		}
 		
 	// Set up date formatter
 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
 			
 			// Variables to hold new date
 			Date formatStartDate = null;
 			Date formatEndDate = null;

 			// Parse String date to Date
 			try {
 				formatEndDate = (Date) formatter.parse(endDate);
 				formatStartDate = (Date) formatter.parse(startDate);
 			} catch (ParseException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			
 			// Set up new format
 			SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
 			
 			String formattedEndDate = endDate;
 			if (formatEndDate != null) {
 				// reformat date and put back into a string
 				formattedEndDate = newFormat.format(formatEndDate);
 			}
 			
 			String formattedStartDate = startDate;
 			if (formatStartDate != null) {
 				// reformat date and put back into a string
 				formattedStartDate = newFormat.format(formatStartDate);
 			}
 		
 		// Load animation
 		anim = AnimationUtils.loadAnimation(ma, R.anim.scale_anim);
 		
 		TextView communityTitle = (TextView) v.findViewById(R.id.adminProjectListCommunityTitle);
 		
 		communityTitle.setText(communityName + " Projects For " + formattedStartDate + " - " + formattedEndDate);

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
			
			view.startAnimation(anim);
			
			Project clickedItem = (Project) projectListView.getItemAtPosition(position);         
			
	   		ma.optionsDialog = new AdminProjectOptionsDialog(clickedItem);
	   		FragmentManager fm = getFragmentManager();
			ma.optionsDialog.show(fm, "projectOptions");			
			
			//ma.changeProjectDetails(clickedItem.getProjectID());

		}

	}
}
