package com.tyct.thankyoutrust;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.tyct.thankyoutrust.model.Message;
import com.tyct.thankyoutrust.model.Users;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<Message> {

	private Context context;
	private List<Message> messageList;
	private List<Users> userList;

	public MessageAdapter(Context context, int resource, List<Message> objects, List<Users> o1) {
		super(context, resource, objects);
		this.context = context;
		this.messageList = objects;
		this.userList = o1;
	}
	
	//Get Item and reverse order it
	@Override
	public Message getItem(int position)
	{
	    return super.getItem(getCount() - 1 - position);
	}
	
	@SuppressLint("ViewHolder")
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
		
		//Text View that displays messages
		TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
		tvDate.setText(message.getDate());
		
		///===========================================================
		//
		//Temporary just display the infoID instead of name of person.
		//
		//*********NEED TO REPLACE INFOID WITH NAME******************
		String displayName = null;
		
		for(Users user : userList)
		{
			if(user.getInfoID()==message.getInfoID())
			{
				
				String lastName = user.getLastName();
				String firstName = user.getFirstName();
				displayName = firstName + " " + lastName;
			}
		}
		TextView tvId = (TextView) view.findViewById(R.id.tvName);
		tvId.setText(displayName);
		
		
		return view;
	}
		

}