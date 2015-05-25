package com.tyct.thankyoutrust.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.tyct.thankyoutrust.AdminUsersProfile;

public class AdminOptionsDialog extends android.app.DialogFragment{
		// Array for Admin Options
		String[] optionsArray = { "Set as admin", "Remove as admin", "Change community"};
	
		AdminUsersProfile myActivity;
		// selected user
		String selectedUser;
		
		String selectedItem = null;
		
		public AdminOptionsDialog() {}
		
		public AdminOptionsDialog(String user) {
			selectedUser = user;
		}
		
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			myActivity = (AdminUsersProfile) getActivity();

			
			Builder builder = new AlertDialog.Builder(getActivity());
			
			builder.setTitle("Select Option for " + selectedUser);
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
				
				myActivity.setDialogResults(true, selectedItem);
				
			
			}
			
		}
		
		public class negativeListener implements DialogInterface.OnClickListener
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				myActivity.setDialogResults(false, null);
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
