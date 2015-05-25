package com.tyct.thankyoutrust.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.tyct.thankyoutrust.AdminProjectListFragment;
import com.tyct.thankyoutrust.AdminReports;
import com.tyct.thankyoutrust.AdminUsersProfile;
import com.tyct.thankyoutrust.model.Project;

public class AdminSetProjectStateDialog extends android.app.DialogFragment{
		// Array for Admin Options
		String[] optionsArray = { "In Progress", "Successful", "Unsuccessful"};
	
		AdminReports myActivity;
		
		String selectedItem = null;
		Project selected;
		
		public AdminSetProjectStateDialog() {}
		
		public AdminSetProjectStateDialog(Project selectedProject) {
			selected = selectedProject;
		}
		
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			myActivity = (AdminReports) getActivity();

			
			Builder builder = new AlertDialog.Builder(getActivity());
			
			builder.setTitle("Set Project Status");
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
				
				myActivity.setStatusDialogResults(true, selectedItem, selected);
				
			
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
