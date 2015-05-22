package com.tyct.thankyoutrust.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tyct.thankyoutrust.AdminAllCommunities;
import com.tyct.thankyoutrust.R;

public class AdminAddNewCommunity extends android.app.DialogFragment {
	boolean result;
	AdminAllCommunities myActivity;

	public AdminAddNewCommunity() {
		// TODO Auto-generated constructor stub
	}

	public AlertDialog onCreateDialog(Bundle savedInstanceState) {
		myActivity = (AdminAllCommunities) getActivity();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.add_community_dialog, null));
		builder.setTitle("Add New Community");
		builder.setPositiveButton("Ok", null);
		builder.setNegativeButton("Cancel", null);

		final AlertDialog dialog = builder.create();

		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				Button positive = ((AlertDialog) dialog)
						.getButton(AlertDialog.BUTTON_POSITIVE);
				positive.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						result = true;
						boolean cancel = false; // Deals with input errors
						View focusView = null;

						// Get Text from editText field
						EditText communityNamedEditTextField = (EditText) getDialog()
								.findViewById(R.id.editTextEditCName);
						// Get Text from editText field
						EditText pcEditTextField = (EditText) getDialog()
								.findViewById(R.id.editTextPostCodeEdit);

						// Check required fields are filled
						if (communityNamedEditTextField.getText().toString()
								.equals("")) {
							communityNamedEditTextField
									.setError(getString(R.string.error_field_required));
							focusView = communityNamedEditTextField;
							cancel = true;
						}

						if (pcEditTextField.getText().toString().equals("")) {
							pcEditTextField
									.setError(getString(R.string.error_field_required));
							focusView = pcEditTextField;
							cancel = true;
						}

						if (cancel) {
							// There was an error; don't attempt registration
							// and focus the
							// first
							// form field with an error.
							focusView.requestFocus();
						} else {
							// Put Text into string form
							String communityName = communityNamedEditTextField
									.getText().toString();
							// Put Text into string form
							String postCodeString = pcEditTextField.getText()
									.toString();
							// convert post code to int
							int postCode = Integer.parseInt(postCodeString);
							
							Toast.makeText(myActivity, "Community created",
									Toast.LENGTH_SHORT).show();

							returnToTarget(result, communityName, postCode);
						}
					}
				});
			}
		});

		return dialog;
	}

	public class positiveListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			result = true;
			boolean cancel = false; // Deals with input errors
			View focusView = null;

			// Get Text from editText field
			EditText communityNamedEditTextField = (EditText) getDialog()
					.findViewById(R.id.editTextEditCName);
			// Get Text from editText field
			EditText pcEditTextField = (EditText) getDialog().findViewById(
					R.id.editTextPostCodeEdit);

			// Check required fields are filled
			if (communityNamedEditTextField.getText().toString().equals("")) {
				communityNamedEditTextField
						.setError(getString(R.string.error_field_required));
				focusView = communityNamedEditTextField;
				cancel = true;
			}

			if (pcEditTextField.getText().toString().equals("")) {
				pcEditTextField
						.setError(getString(R.string.error_field_required));
				focusView = pcEditTextField;
				cancel = true;
			}

			if (cancel) {
				// There was an error; don't attempt registration and focus the
				// first
				// form field with an error.
				Toast.makeText(myActivity, "Unsuccessful", Toast.LENGTH_SHORT)
						.show();
				focusView.requestFocus();
			} else {
				// Put Text into string form
				String communityName = communityNamedEditTextField.getText()
						.toString();
				// Put Text into string form
				String postCodeString = pcEditTextField.getText().toString();
				// convert post code to int
				int postCode = Integer.parseInt(postCodeString);

				returnToTarget(result, communityName, postCode);
			}
		}

	}

	public class negativeListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			result = false;
			returnToTarget(result, null, 0);
		}

	}

	private void returnToTarget(boolean result, String communityName,
			int postCode) {
		myActivity = (AdminAllCommunities) getActivity();

		if (myActivity != null) {
			myActivity.addNewCommunity(result, communityName, postCode);
		}
	}
}
