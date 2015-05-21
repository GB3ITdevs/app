package com.tyct.thankyoutrust.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;

import com.tyct.thankyoutrust.AdminAllCommunities;

public class AdminCommunityDialog extends android.app.DialogFragment {
	AdminAllCommunities myActivity;
	String[] optionsArray = { "Post a message in community", "View Grant Round History", "Edit Community"};
	String selectedItem;
	boolean result;
	String communityName;
	int communityId;
	
	
	public AdminCommunityDialog(String comName, int comId) {
		communityName = comName;
		communityId = comId;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		myActivity = (AdminAllCommunities) getActivity();
		
		Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("Select an Option for " + communityName);
	
		builder.setSingleChoiceItems(optionsArray, -1, new OnMultiChoiceClickListener());
		builder.setPositiveButton("Ok", new positiveListener());
		builder.setNegativeButton("Cancel", new negativeListener());
		
		Dialog dialog = builder.create();
		
		return dialog;
		
	}
	
	public class positiveListener implements DialogInterface.OnClickListener
	{

		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			result = true;
			returnToTarget(result, selectedItem);
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
	selectedItem = optionsArray[which];
}

}
	
	private void returnToTarget(boolean result, String option)
	{
		myActivity = (AdminAllCommunities) getActivity();
		
        if (myActivity != null) 
        {
            myActivity.setDialogResults(result, option, communityId, communityName);
        }
	}
}
