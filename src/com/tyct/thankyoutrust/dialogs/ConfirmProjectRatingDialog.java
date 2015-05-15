package com.tyct.thankyoutrust.dialogs;

import com.tyct.thankyoutrust.ProjectDetailsActivity;
import com.tyct.thankyoutrust.ProjectDetailsFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmProjectRatingDialog extends android.app.DialogFragment
{
	ProjectDetailsActivity myActivity;
	
	boolean result;

	
	public ConfirmProjectRatingDialog() {}
	
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		myActivity = (ProjectDetailsActivity) getActivity();

		
		Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle("Are you sure?");
		builder.setMessage("Is a rating of " + myActivity.getProjectRating() + " stars what you wanted to rate this project? This cannot be changed once submitted.");
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
			returnToTarget(result);
		}
		
	}
	
	public class negativeListener implements DialogInterface.OnClickListener
	{

		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			result = false;
			returnToTarget(result);
		}
		
	}
	
	private void returnToTarget(boolean result)
	{
		ProjectDetailsFragment targetFragment = (ProjectDetailsFragment) getTargetFragment();
		
        if (targetFragment != null) 
        {
            targetFragment.setDialogResults(result);
        }
	}
}
