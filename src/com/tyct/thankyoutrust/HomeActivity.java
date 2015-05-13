//Home Screen/Comment Board
package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tyct.thankyoutrust.model.Comment;
import com.tyct.thankyoutrust.model.Community;
import com.tyct.thankyoutrust.model.User;
import com.tyct.thankyoutrust.parsers.CommentJSONParser;
import com.tyct.thankyoutrust.parsers.CommunityJSONParser;
import com.tyct.thankyoutrust.parsers.UserJSONParser;

public class HomeActivity extends ListActivity {

	TextView output;
	ProgressBar pb;
	List<MyTask> tasks;
	List<UserTask> userTask;
	List<CommunityTask> communityTask;
	List<PostTask> posttasks;
	Comment commentEntity;

	List<Comment> commentList;
	List<Comment> commentListUnsorted;

	List<User> userList;
	
	List<Community> communityList;

	int userID;
	int communityID;
	String communityName;

	// User Session Manager Class
	SessionManager session;

	boolean admin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		// Check user login (this is the important point)
		// If User is not logged in , This will redirect user to LoginActivity
		// and finish current activity from activity stack.
		if (session.checkLogin())
			finish();

		// get user data from session
		HashMap<String, String> userStored = session.getUserDetails();
		// get email
		String userEmail = userStored.get("email");
		// get user id
		userID = Integer.parseInt(userStored.get("id"));
		// get admin
		int adminStatus = Integer.parseInt(userStored.get("admin"));
		// Retrieve the logged in users communityID
		communityID = Integer.parseInt(userStored.get("communityID"));

		// Progress Bar
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);

		tasks = new ArrayList<>();

		// get data from database
		display();
		userDisplay();
		getCommunities();

		// Button to post to comments
		Button postCommentButton = (Button) findViewById(R.id.button_Post_Comments);
		postCommentButton.setOnClickListener(new postCommentHandler());

		// set admin
		if (adminStatus == 1) {
			admin = true;
		}
		// Toast message used for
		// development**************************************
		// Toast.makeText(this,
		// userEmail + ", usid: " + userId + " admin = " + adminStatus,
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!admin) {
			getMenuInflater().inflate(R.menu.admin_all_users, menu);
		}

		if (admin) {
			getMenuInflater().inflate(R.menu.main, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent goTo = new Intent();
		switch (item.getItemId()) {
		case R.id.action_projects:
			goTo = new Intent(HomeActivity.this, Projects.class);
			startActivity(goTo);
			return true;
		case R.id.action_home:
			finish();
			goTo = getIntent();
			startActivity(goTo);
			return true;
		case R.id.admin:
			goTo = new Intent(HomeActivity.this, AdminHomePage.class);
			startActivity(goTo);
			return true;
		case R.id.action_profile:
			goTo = new Intent(HomeActivity.this, ProfileActivity.class);
			startActivity(goTo);
			return true;
		case R.id.action_about_us:
			goTo = new Intent(HomeActivity.this, AboutUs.class);
			startActivity(goTo);
			return true;
		case R.id.action_logout:
			session.logoutUser();
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}

	private void requestUserData(String uri) {
		UserTask task = new UserTask();
		task.execute(uri);
	}
	
	private void requestCommunityData(String uri) {
		CommunityTask task = new CommunityTask();
		task.execute(uri);
	}

	protected void updateDisplay() {
		// get list view from CommentAdapter
		CommentAdapter adapter = new CommentAdapter(this,
				R.layout.item_message, commentList, userList);
		setListAdapter(adapter);
	}

	public void display() {
		if (isOnline()) {
			requestData("http://gb3it.pickworth.info:3000/comments");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void userDisplay() {
		if (isOnline()) {
			requestUserData("http://gb3it.pickworth.info:3000/users");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}
	
	public void getCommunities() {
		if (isOnline()) {
			requestCommunityData("http://gb3it.pickworth.info:3000/communities");
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

	// Async task for the Comment board to display
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

			commentListUnsorted = CommentJSONParser.parseFeed(result);

			commentList = new ArrayList<>();

			for (Comment comment : commentListUnsorted) {
				if (comment.getCommunityID() == communityID) {
					commentList.add(comment);
				}
			}

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

	// Async task for the Comment board to display users
	private class UserTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// updateDisplay("Starting task");

			// if (userTask.size() == 0) {
			// pb.setVisibility(View.VISIBLE);
			// }
			// userTask.add(this);
		}

		@Override
		protected String doInBackground(String... params) {

			String content = HttpManager.getData(params[0]);
			return content;

		}

		@Override
		protected void onPostExecute(String result) {

			userList = UserJSONParser.parseFeed(result);
			updateDisplay();

			// userTask.remove(this);
			// if (userTask.size() == 0) {
			// pb.setVisibility(View.INVISIBLE);
			// }

		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}
	
	// Async task to get community name
		private class CommunityTask extends AsyncTask<String, String, String> {

			@Override
			protected String doInBackground(String... params) {

				String content = HttpManager.getData(params[0]);
				return content;

			}

			@Override
			protected void onPostExecute(String result) {

				communityList = CommunityJSONParser.parseFeed(result);

				for (Community community : communityList) {
					if (community.getCommunityID() == communityID) {
						communityName = community.getCommunityName();
					}
				}
				// Header Text
				TextView header = (TextView) findViewById(R.id.tvMsgboardHeader);
				header.setText(communityName + " Community Messageboard");
				updateDisplay();
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

			// If statement, checks to make sure that user has put something
			// into the edit text field
			if (commentString.equals("")) {
				// If Edit Text is empty it will show a toast
				Toast.makeText(HomeActivity.this,
						"You will need to write a comment to post",
						Toast.LENGTH_LONG).show();
			} else {
				// If Edit Text is not empty it will Post to the new comment to
				// the database

				// TODO Debugging, DO NOT LEAVE THIS TOAST HERE
				Toast.makeText(HomeActivity.this, "Posted", Toast.LENGTH_LONG)
						.show();
				// Create new Comment Object, then pass data into sets
				commentEntity = new Comment();
				// Passes the infoID
				commentEntity.setUserID(userID);
				// Passes the Postal Code
				commentEntity.setCommunityID(communityID);
				// Passes the comment
				commentEntity.setComment(commentString);

				// Calls the PostTask Method, and posts the commentEntity
				posttasks = new ArrayList<>();
				PostTask task = new PostTask();
				task.execute();
				// Sets the Edit Text field to empty
				commentData.setText("");

				// refresh the display for the list view.
				display();
				userDisplay();
			}
			// Debugging Activity
			// String commentEntityString =
			// CommentJSONParser.POSTComment(commentEntity);
			// Toast.makeText(MainActivity.this, commentEntityString,
			// Toast.LENGTH_LONG).show();

		}

	}

	// Handles the posting side
	private class PostTask extends AsyncTask<String, String, String> {
		String commentEntityString = CommentJSONParser
				.POSTComment(commentEntity);

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
					commentEntityString);
			String result = "Comment Posted";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			// String commentResult = (result);

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