package com.tyct.thankyoutrust.dialogs;

import java.util.ArrayList;

import com.tyct.thankyoutrust.GrantRoundsActivity;
import com.tyct.thankyoutrust.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminAddGrantRoundDialog extends android.app.DialogFragment {
	boolean result;
	GrantRoundsActivity myActivity;

	public AdminAddGrantRoundDialog() {
		// TODO Auto-generated constructor stub
	}

	public AlertDialog onCreateDialog(Bundle savedInstanceState) {
		myActivity = (GrantRoundsActivity) getActivity();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.add_grant_round_dialog, null));
		builder.setTitle("Add New Grant Round");
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

						// Get Text from start date editText fields
						final EditText startDay = (EditText) getDialog().findViewById(R.id.editTextStartDay);
						final EditText startMonth = (EditText) getDialog().findViewById(R.id.editTextStartMonth);
						final EditText startYear = (EditText) getDialog().findViewById(R.id.editTextStartYear);
						
						// Set text listeners to auto change to the next edittext
						startDay.addTextChangedListener(new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
								if (startDay.getText().toString().length() == 2) {
									startDay.clearFocus();
									startMonth.requestFocus();
								}
							}

							@Override
							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void afterTextChanged(Editable s) {
								// TODO Auto-generated method stub
								
							}					
						});
						
						// Set text listeners to auto change to the next edittext
						startMonth.addTextChangedListener(new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
								if (startMonth.getText().toString().length() == 2) {
									startMonth.clearFocus();
									startYear.requestFocus();
								}
							}

							@Override
							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void afterTextChanged(Editable s) {
								// TODO Auto-generated method stub
								
							}					
						});
						
						// Get Text from end date editText fields
						final EditText endDay = (EditText) getDialog().findViewById(R.id.editTextEndDay);
						final EditText endMonth = (EditText) getDialog().findViewById(R.id.editTextEndMonth);
						final EditText endYear = (EditText) getDialog().findViewById(R.id.editTextEndYear);
						
						// Set text listeners to auto change to the next edittext
						endDay.addTextChangedListener(new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
								if (endDay.getText().toString().length() == 2) {
									endDay.clearFocus();
									endMonth.requestFocus();
								}
							}

							@Override
							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void afterTextChanged(Editable s) {
								// TODO Auto-generated method stub
								
							}					
						});
						
						// Set text listeners to auto change to the next edittext
						endMonth.addTextChangedListener(new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
								if (endMonth.getText().toString().length() == 2) {
									endMonth.clearFocus();
									endYear.requestFocus();
								}
							}

							@Override
							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void afterTextChanged(Editable s) {
								// TODO Auto-generated method stub
								
							}					
						});
						
						// add edittexts to arraylist for checking
						ArrayList<EditText> textFields = new ArrayList<EditText>();
						textFields.add(startDay);
						textFields.add(startMonth);
						textFields.add(startYear);
						textFields.add(endDay);
						textFields.add(endMonth);
						textFields.add(endYear);
						
						// Reset errors.
						for (EditText editText : textFields) {
							editText.setError(null);
						}
						
						// Check for empty EditText fields.
						for (EditText editText : textFields) {
							if (checkEmptyEditTextFields(editText)) {
								editText.setError(getString(R.string.error_field_required));
								focusView = editText;
								cancel = true;
							}
						}						

						if (cancel) {
							// There was an error; don't attempt registration
							// and focus the
							// first
							// form field with an error.
							focusView.requestFocus();
						} else {
							// Concatenate start date into single string
							String startDateStr = startDay.getText().toString() + "-" + startMonth.getText().toString() + "-" + startYear.getText().toString();
							// Put Text into string form
							String endDateStr = endDay.getText().toString() + "-" + endMonth.getText().toString() + "-" + endYear.getText().toString();
							
							Toast.makeText(myActivity, "Grand Round created",
									Toast.LENGTH_SHORT).show();

							returnToTarget(result, startDateStr, endDateStr);
						}
					}
				});
			}
		});

		return dialog;
	}

	/*public class positiveListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			result = true;
			boolean cancel = false; // Deals with input errors
			View focusView = null;

			// Get Text from editText field
			EditText startDate = (EditText) getDialog()
					.findViewById(R.id.editTextStartDate);
			// Get Text from editText field
			EditText endDate = (EditText) getDialog().findViewById(
					R.id.editTextEndDate);

			// Check required fields are filled
			if (startDate.getText().toString().equals("")) {
				startDate.setError(getString(R.string.error_field_required));
				focusView = startDate;
				cancel = true;
			}

			if (endDate.getText().toString().equals("")) {
				endDate.setError(getString(R.string.error_field_required));
				focusView = endDate;
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
				String startDateString = startDate.getText()
						.toString();
				// Put Text into string form
				String endDateString = endDate.getText().toString();

				returnToTarget(result, startDateString, endDateString);
			}
		}
	}

	public class negativeListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			result = false;
			returnToTarget(result, null, null);
		}
	}*/
	
	private boolean checkEmptyEditTextFields(EditText field) {
		boolean result = false;
		if (field.getText().toString().trim().equals("")) {
			result = true;
		}
		return result;
	}

	private void returnToTarget(boolean result, String start,
			String end) {
		myActivity = (GrantRoundsActivity) getActivity();

		if (myActivity != null) {
			myActivity.addNewGrantRound(result, start, end);
		}
	}
}
