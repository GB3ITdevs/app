package com.tyct.thankyoutrust.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.tyct.thankyoutrust.R;

import com.tyct.thankyoutrust.AdminAllCommunities;

public class AdminEditCommunityDialog extends android.app.DialogFragment{
	boolean result;
	AdminAllCommunities myActivity;
	String communityName;
	int communityId;
	int postalCode;

	public AdminEditCommunityDialog(String comName, int postCode, int communityID) {
		communityName = comName;
		postalCode = postCode;
		communityId = communityID;
		
	}

	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		myActivity = (AdminAllCommunities) getActivity();
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		final View textEntryView = inflater.inflate(R.layout.add_community_dialog, null);

		final EditText et = (EditText) textEntryView.findViewById(R.id.editTextEditCName);
		final EditText etpc = (EditText) textEntryView.findViewById(R.id.editTextPostCodeEdit);
	
		et.setText(communityName);
		etpc.setText(Integer.toString(postalCode));
		
		Builder builder = new AlertDialog.Builder(getActivity());

	    builder.setView(textEntryView);
    
		builder.setTitle("Edit "+ communityName);

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
			
			// Get Text from editText field
			EditText communityNamedEditTextField = (EditText) getDialog().findViewById(R.id.editTextEditCName);
			// Put Text into string form
			String communityName = communityNamedEditTextField .getText().toString();
			
			// Get Text from editText field
			EditText pcEditTextField = (EditText) getDialog().findViewById(R.id.editTextPostCodeEdit);
			// Put Text into string form
			String postCodeString = pcEditTextField.getText().toString();
			int postCode = Integer.parseInt(postCodeString);
		
			returnToTarget(result, communityName, postCode);
		}
		
	}
	
	public class negativeListener implements DialogInterface.OnClickListener
	{

		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			result = false;
			returnToTarget(result, null, 0);
		}
		
	}
	
	private void returnToTarget(boolean result, String communityName, int postCode)
	{
		myActivity = (AdminAllCommunities) getActivity();
		
        if (myActivity != null) 
        {
            myActivity.editCommunity(result, communityName, postCode, communityId);
        }
	}
}
