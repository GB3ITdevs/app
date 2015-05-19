package com.tyct.thankyoutrust;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tyct.thankyoutrust.model.Community;

public class CommunityListAdapter extends ArrayAdapter<Community>{
	Context context;
	List<Community> communities;


	public CommunityListAdapter(Context context, int resource, List<Community> communityList) {
super(context, resource, communityList);
		
		this.communities = communityList;	
		this.context = context;	
}
	
	@Override
	public Community getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup container) {

		Community currentCommunity = communities.get(position);
		// Get a LayoutInflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		// Inflate custom_list_view and store the returned View in a
		// variable
		View customView = inflater.inflate(R.layout.admin_allusers_layout, container, false);

		// Get references to the controls in two_column_list.
		TextView tvCommunityName = (TextView) customView.findViewById(R.id.textViewaName);
		TextView tvCommunityPostalCode = (TextView) customView.findViewById(R.id.textViewaPostCode);
		
		tvCommunityName.setText(currentCommunity.getCommunityName());
		tvCommunityPostalCode.setText(Integer.toString(currentCommunity.getPostalCode()));
		
		// Return customview
		return customView;
	}
}
