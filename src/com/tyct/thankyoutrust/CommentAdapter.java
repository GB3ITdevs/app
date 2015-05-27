package com.tyct.thankyoutrust;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tyct.thankyoutrust.model.AdminID;
import com.tyct.thankyoutrust.model.Comment;
import com.tyct.thankyoutrust.model.User;

public class CommentAdapter extends ArrayAdapter<Comment> {

	Context context;
	List<User> userList;
	List<DeleteCommentTask> deleteCommentTask;
	Animation anim;

	// User Session Manager Class
	SessionManager session;

	public CommentAdapter(Context contextPassed, int resource,
			List<Comment> objects, List<User> o1) {
		super(contextPassed, resource, objects);

		context = contextPassed;
		userList = o1;
		
		// Load animation
		anim = AnimationUtils.loadAnimation(context, R.anim.scale_anim);

		// Session class instance
		session = new SessionManager(context);
	}

	// Get Item and reverse order it
	@Override
	public Comment getItem(int position) {
		return super.getItem(getCount() - 1 - position);
	}

	@SuppressLint({ "ViewHolder", "SimpleDateFormat" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_message, parent, false);

		// get user data from session
		HashMap<String, String> userStored = session.getUserDetails();

		// Reverse order the messageList.
		Comment message = getItem(position);
		// Old Code that worked fine but did not reverse order anything
		// Comment message = messageList.get(position);

		// Text View that displays the comments
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
				"EEE, d MMM, ''yy h:mm aaa");
		// reformat date and put back into a string
		String formattedDate = newFormat.format(formatInDateFrom);

		// Set up TextView that displays dates
		TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
		// Put Formatted Date into TextView
		tvDate.setText(formattedDate);

		// String to hold the users display Name
		String displayName = "";

		// Button for user to delete their comment if they wish
		ImageView deleteMsg = (ImageView) view
				.findViewById(R.id.btn_comment_delete);
		// By default the button is not visible
		deleteMsg.setVisibility(View.INVISIBLE);
		deleteMsg.setOnClickListener(new DeleteButtonHandler(message
				.getCommentID(), message.getComment()));

		// for users in the userlist where user info id equals message get name
		for (User user : userList) {
			if (user.getUserID() == message.getUserID()) {
				// get last name from user list
				String lastName = user.getLastName();
				// get first name from user list
				String firstName = user.getFirstName();
				// If not admin, just store firstname and last name in a
				// string
				displayName = firstName + " " + lastName;
			}
		}

		// If message was posted by the logged in user OR the logged in user is
		// an admin
		if (message.getUserID() == Integer.parseInt(userStored.get("id"))
				|| userStored.get("admin").equals("1")) {
			// display the delete button
			deleteMsg.setVisibility(View.VISIBLE);
		}

		// Set Textview for Name then Display users display name
		TextView tvId = (TextView) view.findViewById(R.id.tvName);
		tvId.setText(displayName);

		// return the view
		return view;
	}

	// Delete a comment
	public class DeleteButtonHandler implements OnClickListener {
		int commentID;
		String comment;

		public DeleteButtonHandler(int commentID, String comment) {
			this.commentID = commentID;
			this.comment = comment;
		}

		@Override
		public void onClick(View v) {
			v.startAnimation(anim);
			deleteDialog(commentID, comment);
		}
	}

	// Show a pop-up dialog to confirm user wants to delete the comment
	public void deleteDialog(int commentID, String comment) {
		final Integer[] commentToDelete = new Integer[1];
		commentToDelete[0] = commentID;

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder
				.setTitle(comment);

		// set dialog message
		alertDialogBuilder
				.setMessage("Are you sure you want to delete this message?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked,
								// delete the comment
								DeleteCommentTask deleteCommentTask = new DeleteCommentTask();
								deleteCommentTask.execute(commentToDelete);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	/**
	 * Delete comment Async task (DELETE)
	 */
	private class DeleteCommentTask extends AsyncTask<Integer, String, String> {
		String deleteURI = "http://gb3it.pickworth.info:3000/comments/";

		@Override
		protected String doInBackground(Integer... params) {
			HttpManager.deleteData(deleteURI + params[0]);
			String result = "Comment Deleted";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			((Activity) context).finish();
			Intent goTo = new Intent(context, HomeActivity.class);
			context.startActivity(goTo);
			//Toast.makeText(context, result, Toast.LENGTH_LONG).show();
		}
	}
}