package com.tyct.thankyoutrust;

import java.util.List;

import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.model.User;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class UserListAdapter extends ArrayAdapter<User> {
	Context context;
	List<User> users;
	List<Community> communities;
	
	public UserListAdapter(Context context, int resource, List<User> userList, List<Community> communityList) {
		
		super(context, resource, userList);
		
		this.users = userList;
		this.communities = communityList;	
		this.context = context;
	}
	
	@Override
	public User getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup container) {

		User currentUser = users.get(position);
		// Get a LayoutInflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		// Inflate custom_list_view and store the returned View in a
		// variable
		View customView = inflater.inflate(R.layout.admin_allusers_layout, container, false);

		// Get references to the controls in two_column_list.
		TextView nameTxtView = (TextView) customView.findViewById(R.id.textViewaName);
		TextView postalCodeTxtView = (TextView) customView.findViewById(R.id.textViewaPostCode);
		
		for (Community com : communities) {
			if (com.getCommunityID()==currentUser.getCommunityID())
			{
				postalCodeTxtView.setText(Integer.toString(com.getPostalCode()));
			}
			
		}

		nameTxtView.setText(currentUser.getFirstName() + " " + currentUser.getLastName());

		// Return customview
		return customView;
	}
	
}