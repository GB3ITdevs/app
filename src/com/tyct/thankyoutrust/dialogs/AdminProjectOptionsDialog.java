package com.tyct.thankyoutrust.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.tyct.thankyoutrust.AdminReports;
import com.tyct.thankyoutrust.model.Project;

public class AdminProjectOptionsDialog extends android.app.DialogFragment{
		// Array for Admin Options
		String[] optionsArray = { "View Report", "Set Project Outcome"};
	
		AdminReports myActivity;
		
		String selectedItem = null;
		Project selected;
		
		public AdminProjectOptionsDialog() {}
		
		public AdminProjectOptionsDialog(Project selectedProject) {
			selected = selectedProject;
		}
		
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			myActivity = (AdminReports) getActivity();

			
			Builder builder = new AlertDialog.Builder(getActivity());
			
			builder.setTitle("Project Options");
			// Set single choice options (radio buttons)
			builder.setSingleChoiceItems(optionsArray, -1, new OnMultiChoiceClickListener());
			// set Submit Button
			builder.setPositiveButton("Ok", new positiveListener());
			// set Cancel Button
			builder.setNegativeButton("Cancel", new negativeListener());

			Dialog dialog = builder.create();
			
			return dialog;
			
		}
		
		public class positiveListener implements DialogInterface.OnClickListener
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				
				myActivity.setDialogResults(true, selectedItem, selected);
				
			
			}
			
		}
		
		public class negativeListener implements DialogInterface.OnClickListener
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				myActivity.setDialogResults(false, null, selected);
			}
			
		}
		
		public class OnMultiChoiceClickListener implements
		DialogInterface.OnClickListener,
		android.content.DialogInterface.OnMultiChoiceClickListener {

	@Override
	public void onClick(DialogInterface dialog, int which,
			boolean isChecked) {
		// use this method for checkboxes
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	
			// Get selected Item from radio buttons
			selectedItem = optionsArray[which];
		
	}

}
	}
