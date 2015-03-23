package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tyct.thankyoutrust.model.Message;
import com.tyct.thankyoutrust.parsers.MessageJSONParser;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	TextView output;
	ProgressBar pb;
	List<MyTask> tasks;
	List<PostTask> posttasks;
	Message messageEntity;
	
	List<Message> messageList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
		
		tasks = new ArrayList<>();
		
		if (isOnline()) {
			requestData("http://gb3it.pickworth.info:3000/comments");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
		}
		
		//Button to post to comments
		Button postCommentButton = (Button) findViewById(R.id.button_Post_Comments);
		postCommentButton.setOnClickListener(new postCommentHandler());
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == R.id.action_get_data) {
//			if (isOnline()) {
//				requestData("http://gb3it.pickworth.info:3000/comments");
//			} else {
//				Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
//			}
//		}
//		return false;
//	}
	
	

	private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}

	protected void updateDisplay() {
		
//		if (messageList != null) {
//			for (Message message : messageList) {
//				output.append(message.getInfoID() + "    "  + message.getComment() + "\n" + "\n");
//			}
//		}
		
		MessageAdapter adapter = new MessageAdapter(this, R.layout.item_message, messageList);
		setListAdapter(adapter);
		
	}
	
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}
	
	private class MyTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
//			updateDisplay("Starting task");
			
			if (tasks.size() == 0) {
				pb.setVisibility(View.VISIBLE);
			}
			tasks.add(this);
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			String content = HttpManager.getData(params[0]);
			return content;
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			messageList = MessageJSONParser.parseFeed(result);
			updateDisplay();

			tasks.remove(this);
			if (tasks.size() == 0) {
				pb.setVisibility(View.INVISIBLE);
			}

		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			//updateDisplay(values[0]);
		}
		
	}
	
	
	//Class to handle posting comments
	public class postCommentHandler implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			//Get Text from editText field
			EditText commentData = (EditText) findViewById(R.id.text_Comments);
			//put Text into string form
			String commentString = commentData.getText().toString();
			
			//Info ID (hard coded for now, but will need to get logged in user id)
			int userID = 01;
			
			//Postal Code info (hard coded for now, but will need to get logged in person postalcode)
			int postalInfo = 9001;

			if(commentData.equals(null))
			{
				Toast.makeText(MainActivity.this, "You will need to add write a message to post", Toast.LENGTH_LONG).show();
			}
			else
			{
			
			//Create new Message Object, the passing data into sets
			messageEntity = new Message();
			messageEntity.setInfoID(userID);
			messageEntity.setPostalCode(postalInfo);
			messageEntity.setComment(commentString);
			
			posttasks = new ArrayList<>();
			PostTask task = new PostTask();
			task.execute();
			commentData.setText("");
			
			}
		//Debugging Activity	
//		String messageEntityString = MessageJSONParser.POSTMessage(messageEntity);
//		Toast.makeText(MainActivity.this, messageEntityString, Toast.LENGTH_LONG).show();
			
		}
		
	}
	
	private class PostTask extends AsyncTask<String, String, String> 
	{
		String messageEntityString = MessageJSONParser.POSTMessage(messageEntity);

		@Override
		protected void onPreExecute() {
//			updateDisplay("Starting task");
			
			if (posttasks.size() == 0) {
				pb.setVisibility(View.VISIBLE);
			}
			posttasks.add(this);
		}
		
		@Override
		protected String doInBackground(String... params) 
		{
			HttpManager.postData("http://gb3it.pickworth.info:3000/comments", messageEntityString);
			String result = "Message Posted";
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			//String messageResult = (result);
			
			posttasks.remove(this);
			if (tasks.size() == 0) 
			{
				pb.setVisibility(View.INVISIBLE);
			}
			

		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			updateDisplay();
		}
		
	}

}