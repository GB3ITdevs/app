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

import com.tyct.thankyoutrust.model.Message;
import com.tyct.thankyoutrust.model.Users;

public class MessageAdapter extends ArrayAdapter<Message> {

	Context context;
	List<Users> userList;

	public MessageAdapter(Context contextPassed, int resource, List<Message> objects, List<Users> o1) {
		super(contextPassed, resource, objects);
		
		context = contextPassed;
		userList = o1;
	}
	
	//Get Item and reverse order it
	@Override
	public Message getItem(int position)
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
		Message message = getItem(position);
		//Old Code that worked fine but did not reverse order anything
		//Message message = messageList.get(position);
		
		//Text View that displays the comments
		TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
		tvComment.setText(message.getComment());
		
		String dateString = message.getDate();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("EEE dd, MMM ''yy");
		dateString = sdf1.format(date);
		
		TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
		tvDate.setText(dateString);
		
		//String to hold the users display Name
		String displayName = "";
		
		//for users in the userlist where user info id equals message get info id
		for(Users user : userList)
		{
			if(user.getInfoID()==message.getInfoID())
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