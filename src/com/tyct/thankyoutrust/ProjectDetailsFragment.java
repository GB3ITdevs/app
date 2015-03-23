package com.tyct.thankyoutrust;

import com.tyct.thankyoutrust.model.Project;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProjectDetailsFragment extends Fragment
{
	//Declare the class fields
		Project projectDisplayed;
		Projects ma;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View v = inflater.inflate(R.layout.fragment_project_details,container,false);
			//Initialize the layout vies
			TextView tvProjectTitle = (TextView) v.findViewById(R.id.projectName);
			TextView tvPostalCode = (TextView) v.findViewById(R.id.postalCode);
			TextView tvApplicantName = (TextView) v.findViewById(R.id.applicantName);
			TextView tvFundsRequested = (TextView) v.findViewById(R.id.fundsRequested);
			TextView tvUseOfFunds = (TextView) v.findViewById(R.id.useOfFunds);
			TextView tvProjectBlurb = (TextView) v.findViewById(R.id.projectBlurb);
			
			//Retrieve the project selected from the activity
			ma = (Projects) getActivity();
			projectDisplayed = ma.getSelectProject();
			
			//Set the text views to display the project information
			tvProjectTitle.setText(projectDisplayed.getProjectName());
			tvPostalCode .setText(Integer.toString(projectDisplayed.getPostalCode()));
			tvApplicantName.setText(projectDisplayed.getApplicantName());
			tvFundsRequested.setText(Integer.toString(projectDisplayed.getFundsRequested()));
			tvUseOfFunds .setText(projectDisplayed.getUseOfFunds());
			tvProjectBlurb.setText(projectDisplayed.getProjectBlurb());
			
			//Return the view
			return v;
		}
}