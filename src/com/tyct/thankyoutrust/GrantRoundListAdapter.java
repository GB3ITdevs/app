package com.tyct.thankyoutrust;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tyct.thankyoutrust.model.GrantRound;

public class GrantRoundListAdapter extends ArrayAdapter<GrantRound> {
	Context context;
	List<GrantRound> grantRounds;

	public GrantRoundListAdapter(Context context, int resource,
			List<GrantRound> grantRoundList) {
		super(context, resource, grantRoundList);

		this.grantRounds = grantRoundList;
		this.context = context;
	}

	@Override
	public GrantRound getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {

		GrantRound currentRound = grantRounds.get(position);
		// Get a LayoutInflater
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		// Inflate custom_list_view and store the returned View in a
		// variable
		View customView = inflater.inflate(android.R.layout.simple_list_item_1,
				container, false);

		String startDate = currentRound.getStartDate();
		String endDate = currentRound.getEndDate();

		// Set up date formatter
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		// Variables to hold new date
		Date formatStartDate = null;
		Date formatEndDate = null;

		// Parse String date to Date
		try {
			formatEndDate = (Date) formatter.parse(endDate);
			formatStartDate = (Date) formatter.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Set up new format
		SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		String formattedEndDate = endDate;
		if (formatEndDate != null) {
			// reformat date and put back into a string
			formattedEndDate = newFormat.format(formatEndDate);
		}
		
		String formattedStartDate = startDate;
		if (formatStartDate != null) {
			// reformat date and put back into a string
			formattedStartDate = newFormat.format(formatStartDate);
		}

		// Get references to the controls in two_column_list.
		TextView tvDate = (TextView) customView
				.findViewById(android.R.id.text1);

		tvDate.setText("Round: " + formattedStartDate + " - " + formattedEndDate);

		// Return customview
		return customView;
	}
}
