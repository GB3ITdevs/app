//Home Screen/Message Board
package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tyct.thankyoutrust.model.Message;
import com.tyct.thankyoutrust.model.Users;
import com.tyct.thankyoutrust.parsers.MessageJSONParser;
import com.tyct.thankyoutrust.parsers.UsersJSONParser;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	TextView output;
	ProgressBar pb;
	List<MyTask> tasks;
	List<damnUserTask> userTask;
	List<PostTask> posttasks;
	Message messageEntity;
	
	List<Message> messageList;
	List<Users> userList;
	
	// User Session Manager Class
    SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 // Session class instance
        session = new SessionManager(getApplicationContext());
		
		// Progress Bar
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);

		tasks = new ArrayList<>();

		display();
		userDisplay();

		// Button to post to comments
		Button postCommentButton = (Button) findViewById(R.id.button_Post_Comments);
		postCommentButton.setOnClickListener(new postCommentHandler());
		
		// Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if(session.checkLogin())
            finish();
         
        // get user data from session
        HashMap<String, String> userStored = session.getUserDetails();
        
        // get email
        String userEmail = userStored.get("email");
        
        // get user id
        int userId = Integer.parseInt(userStored.get("id"));
        Toast.makeText(this, userEmail + ", usid: " + userId, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent goTo = new Intent();
		if (item.getItemId() == R.id.action_projects) 
		{
			goTo = new Intent(MainActivity.this, Projects.class);
			
		}
		if (item.getItemId() == R.id.action_home) 
		{
			goTo = new Intent(MainActivity.this, MainActivity.class);
		}
		if (item.getItemId() == R.id.action_profile) 
		{
			goTo = new Intent(MainActivity.this, ProfileActivity.class);
		}
		if (item.getItemId() == R.id.action_about_us) 
		{
			goTo = new Intent(MainActivity.this, AboutUs.class);
		}
		startActivity(goTo);
		return false;
	}

	private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}

	private void requestUserData(String uri) {
		damnUserTask task = new damnUserTask();
		task.execute(uri);
	}
	
	protected void updateDisplay() {
		// get list view from MessageAdapter
		MessageAdapter adapter = new MessageAdapter(this,
				R.layout.item_message, messageList, userList);
		setListAdapter(adapter);

	}
	
	public void display(){
		if (isOnline()) {
			requestData("http://gb3it.pickworth.info:3000/comments");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}
	
	public void userDisplay(){
		if (isOnline()) {
			requestUserData("http://gb3it.pickworth.info:3000/person_infos");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}
		
	// Connect to database
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	// Async task for the Message board to display
	private class MyTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// updateDisplay("Starting task");

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

			tasks.remove(this);
			if (tasks.size() == 0) {
				pb.setVisibility(View.INVISIBLE);
			}

		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}
	
	// Async task for the Message board to display
		private class damnUserTask extends AsyncTask<String, String, String> {

			@Override
			protected void onPreExecute() {
				// updateDisplay("Starting task");

//				if (userTask.size() == 0) {
//					pb.setVisibility(View.VISIBLE);
//				}
//				userTask.add(this);
			}

			@Override
			protected String doInBackground(String... params) {

				String content = HttpManager.getData(params[0]);
				return content;

			}

			@Override
			protected void onPostExecute(String result) {

				userList = UsersJSONParser.parseFeed(result);
				updateDisplay();

				
//				userTask.remove(this);
//				if (userTask.size() == 0) {
//					pb.setVisibility(View.INVISIBLE);
//				}

			}

			@Override
			protected void onProgressUpdate(String... values) {
				// updateDisplay(values[0]);
			}

		}

	// Class to handle posting comments
	public class postCommentHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			// Get Text from editText field
			EditText commentData = (EditText) findViewById(R.id.text_Comments);
			// Put Text into string form
			String commentString = commentData.getText().toString();

			// get user data from session
	        HashMap<String, String> userStored = session.getUserDetails();	        
	        
	        // get info id
	        int userID = Integer.parseInt(userStored.get("id"));

			// Postal Code info (hard coded for now, but will need to get logged
			// in person postalcode)
			int postalInfo = 9001;

			// If statement, checks to make sure that user has put something
			// into the edit text field
			if (commentString.equals("")) {
				// If Edit Text is empty it will show a toast
				Toast.makeText(MainActivity.this,
						"You will need to write a message to post",
						Toast.LENGTH_LONG).show();
			} else {
				// If Edit Text is not empty it will Post to the new message to
				// the database
				
				//Just a toast letting us know that message was posted, DO NOT LEAVE THIS TOAST HERE
				Toast.makeText(MainActivity.this,
						"Posted",
						Toast.LENGTH_LONG).show();
				// Create new Message Object, then pass data into sets
				messageEntity = new Message();
				// Passes the infoID
				messageEntity.setInfoID(userID);
				// Passes the Postal Code
				messageEntity.setPostalCode(postalInfo);
				// Passes the comment
				messageEntity.setComment(commentString);

				// Calls the PostTask Method, and posts the messageEntity
				posttasks = new ArrayList<>();
				PostTask task = new PostTask();
				task.execute();
				// Sets the Edit Text field to empty
				commentData.setText("");
				
				//refresh the display for the list view.
				display();
				userDisplay();
			}
			// Debugging Activity
			// String messageEntityString =
			// MessageJSONParser.POSTMessage(messageEntity);
			// Toast.makeText(MainActivity.this, messageEntityString,
			// Toast.LENGTH_LONG).show();

		}

	}

	// Handles the posting side
	private class PostTask extends AsyncTask<String, String, String> {
		String messageEntityString = MessageJSONParser
				.POSTMessage(messageEntity);

		@Override
		protected void onPreExecute() {
			// updateDisplay("Starting task");

			if (posttasks.size() == 0) {
				pb.setVisibility(View.VISIBLE);
			}
			posttasks.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			HttpManager.postData("http://gb3it.pickworth.info:3000/comments",
					messageEntityString);
			String result = "Message Posted";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			// String messageResult = (result);

			posttasks.remove(this);
			if (tasks.size() == 0) {
				pb.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		protected void onProgressUpdate(String... values) {
			updateDisplay();
		}

	}

}