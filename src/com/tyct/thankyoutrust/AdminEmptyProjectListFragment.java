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

public class AdminEmptyProjectListFragment extends Fragment {
	
	//String[] projectNames;
	AdminReports ma;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_admin_empty_projects_list, container,
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
 		
 		TextView communityTitle = (TextView) v.findViewById(R.id.adminEmptyProjectListCommunityTitle);
 		
 		communityTitle.setText(communityName + "Projects For " + formattedStartDate + " - " + formattedEndDate);

		return v;
	}

}
