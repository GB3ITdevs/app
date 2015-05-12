package com.tyct.thankyoutrust.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;

import com.tyct.thankyoutrust.AdminAllCommunities;
import com.tyct.thankyoutrust.AdminUsersProfile;
import com.tyct.thankyoutrust.ProjectDetailsActivity;
import com.tyct.thankyoutrust.ProjectDetailsFragment;
import com.tyct.thankyoutrust.dialogs.AdminChangeCommunitiesDialog.OnMultiChoiceClickListener;
import com.tyct.thankyoutrust.dialogs.AdminChangeCommunitiesDialog.negativeListener;
import com.tyct.thankyoutrust.dialogs.AdminChangeCommunitiesDialog.positiveListener;

public class AdminCommunityDialog extends android.app.DialogFragment {
	AdminAllCommunities myActivity;
	String[] optionsArray = { "Post a message in community"};
	String selectedItem;
	boolean result;
	
	
	public AdminCommunityDialog() {}
	
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		myActivity = (AdminAllCommunities) getActivity();

		
		Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("Select an Option");
	
		builder.setSingleChoiceItems(optionsArray, -1, new OnMultiChoiceClickListener());
		builder.setPositiveButton("Submit", new positiveListener());
		builder.setNegativeButton("No", new negativeListener());
		
		builder.setPositiveButton("Yes", new positiveListener());
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
	
	private void returnToTarget(boolean result, String community)
	{
		myActivity = (AdminAllCommunities) getActivity();
		
        if (myActivity != null) 
        {
            myActivity.setDialogResults(result, community);
        }
	}
}
