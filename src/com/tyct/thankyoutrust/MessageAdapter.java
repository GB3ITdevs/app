package com.tyct.thankyoutrust;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tyct.thankyoutrust.model.Comment;
import com.tyct.thankyoutrust.model.User;

public class MessageAdapter extends ArrayAdapter<Comment> {

	Context context;
	List<User> userList;

	public MessageAdapter(Context contextPassed, int resource, List<Comment> objects, List<User> o1) {
		super(contextPassed, resource, objects);
		
		context = contextPassed;
		userList = o1;
	}
	
	//Get Item and reverse order it
	@Override
	public Comment getItem(int position)
	{
	    return super.getItem(getCount() - 1 - position);
	}
	
	@SuppressLint({ "ViewHolder", "SimpleDateFormat" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_message, parent, false);

		//Reverse order the messageList.
		Comment message = getItem(position);
		//Old Code that worked fine but did not reverse order anything
		//Comment message = messageList.get(position);
		
		//Text View that displays the comments
		TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
		tvComment.setText(message.getComment());
		
		// Get date that is a string
		String date = (message.getDate());
		// Set up date formatter
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// Varible to hold new date
		Date formatInDateFrom = null;

		// Parse String date to Date
		try {
			formatInDateFrom = (Date) formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Set up new format ("WeekDay, Day, Month ' Year Hour:Mintues M")
		SimpleDateFormat newFormat = new SimpleDateFormat(
				"EEE, d MMM, ''yy H:mm aaa");
		// reformat date and put back into a string
		String formattedDate = newFormat.format(formatInDateFrom);

		// Set up TextView that displays dates
		TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
		// Put Formatted Date into TextView
		tvDate.setText(formattedDate);
		
		//String to hold the users display Name
		String displayName = "";
		
		//for users in the userlist where user info id equals message get name
		for(User user : userList)
		{
			if(user.getUserID()==message.getUserID())
			{
				//get last name from user list
				String lastName = user.getLastName();
				//get first name from user list
				String firstName = user.getFirstName();
				//store firstname and last name in a string
				displayName = firstName + " " + lastName;
			}
		}
		
		//Set Textview for Name then Display users display name
		TextView tvId = (TextView) view.findViewById(R.id.tvName);
		tvId.setText(displayName);
		
		//return the view
		return view;
	}
}