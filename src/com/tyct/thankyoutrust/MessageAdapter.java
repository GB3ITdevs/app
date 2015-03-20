package com.tyct.thankyoutrust;


import java.util.List;

import com.tyct.thankyoutrust.model.Message;


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

	public MessageAdapter(Context context, int resource, List<Message> objects) {
		super(context, resource, objects);
		this.context = context;
		this.messageList = objects;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_message, parent, false);

		//Display flower name in the TextView widget
		Message message = messageList.get(position);
		
		//Text View that displays the comments
		TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
		tvComment.setText(message.getComment());
		
		//Text View that displays messages
		TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
		tvDate.setText(message.getDate());
		
		//Text View for Name *************JUST A PLACE HOLDER FOR NOW***************
		//TextView tvName = (TextView) view.findViewById(R.id.tvName);
		
		
		return view;
	}
		

}