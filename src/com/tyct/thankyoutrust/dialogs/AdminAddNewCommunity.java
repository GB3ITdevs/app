package com.tyct.thankyoutrust.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tyct.thankyoutrust.AdminAllCommunities;
import com.tyct.thankyoutrust.R;

public class AdminAddNewCommunity extends android.app.DialogFragment{
	boolean result;
	AdminAllCommunities myActivity;

	public AdminAddNewCommunity() {
		// TODO Auto-generated constructor stub
	}

	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		myActivity = (AdminAllCommunities) getActivity();
		
		Builder builder = new AlertDialog.Builder(getActivity());
		
		// Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
   
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.add_community_dialog, null));
		builder.setTitle("Add New Community");
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
			// Get Text from editText field
			EditText pcEditTextField = (EditText) getDialog().findViewById(R.id.editTextPostCodeEdit);
			
			if(communityNamedEditTextField.getText().toString().equals("") || pcEditTextField.getText().toString().equals(""))
			{
				Toast.makeText(myActivity, "All fields must be entered", Toast.LENGTH_LONG).show();
			}
			else
			{
			// Put Text into string form
			String communityName = communityNamedEditTextField .getText().toString();
			// Put Text into string form
			String postCodeString = pcEditTextField.getText().toString();
			//convert post code to int
			int postCode = Integer.parseInt(postCodeString);

			returnToTarget(result, communityName, postCode);
			}
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
            myActivity.addNewCommunity(result, communityName, postCode);
        }
	}
}