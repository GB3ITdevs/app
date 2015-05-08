package com.tyct.thankyoutrust.dialogs;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;

import com.tyct.thankyoutrust.AdminUsersProfile;
import com.tyct.thankyoutrust.model.Community;


public class AdminChangeCommunitiesDialog extends android.app.DialogFragment{

	AdminUsersProfile myActivity;
	String selectedCommunity;
	String[] communityNames;
	
	boolean result;

	
	public AdminChangeCommunitiesDialog() {}
	
	public AdminChangeCommunitiesDialog(List<Community> communityList) {
		
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		myActivity = (AdminUsersProfile) getActivity();

		communityNames = myActivity.getCommunityNames();
		
		Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("Choose a Community to change to:");
		// Set single choice options (radio buttons)
		builder.setSingleChoiceItems(communityNames, -1, new OnMultiChoiceClickListener());
		builder.setPositiveButton("Submit", new positiveListener());
		builder.setNegativeButton("No", new negativeListener());
		
		Dialog dialog = builder.create();
		
		return dialog;
		
	}
	
	public class positiveListener implements DialogInterface.OnClickListener
	{

		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			result = true;
			returnToTarget(result, selectedCommunity);
		}
		
	}
	
	public class negativeListener implements DialogInterface.OnClickListener
	{

		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			result = false;
			returnToTarget(result, null);
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
	selectedCommunity = communityNames[which];
}

}
	
	private void returnToTarget(boolean result, String community)
	{
		myActivity = (AdminUsersProfile) getActivity();
		
        if (myActivity != null) 
        {
            myActivity.setNewCommunity(result, community);
        }
	}
}
