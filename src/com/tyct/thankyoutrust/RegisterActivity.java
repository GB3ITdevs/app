package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import com.tyct.thankyoutrust.model.Users;
import com.tyct.thankyoutrust.parsers.UsersJSONParser;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A registration screen that allows users to register an account requiring name/email/password.
 */
public class RegisterActivity extends Activity implements LoaderCallbacks<Cursor> {

	List<PostTask> tasks;
	Users newUser;
	
	List<Users> userList;

	/**
	 * Keep track of the registration task to ensure we can cancel it if requested.
	 */
	private UserRegisterTask mAuthTask = null;

	// UI references.
	private EditText mNameView;
	private EditText mSurnameView;
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private EditText mPasswordCheckView;
	private View mProgressView;
	private View mRegisterFormView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		// Fetch intent data
        Bundle extras = getIntent().getExtras();
        String userEmail = null;
		if (extras != null) {
			userEmail = extras.getString("email");
		}

		// Set up the registration form.
		mNameView = (EditText) findViewById(R.id.reg_firstName);
		
		mSurnameView = (EditText) findViewById(R.id.reg_surname);
		
		mEmailView = (AutoCompleteTextView) findViewById(R.id.reg_email);
		populateAutoComplete();
		mEmailView.setText(userEmail);

		mPasswordView = (EditText) findViewById(R.id.reg_password);
		
		mPasswordCheckView = (EditText) findViewById(R.id.reg_password_check);
		mPasswordCheckView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.register || id == EditorInfo.IME_NULL) {
							registrationDialog();
							return true;
						}
						return false;
					}
				});

		// register button handler
		Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
		mEmailRegisterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				registrationDialog();
			}
		});

		mRegisterFormView = findViewById(R.id.email_register_form);
		mProgressView = findViewById(R.id.register_progress);
		
		tasks = new ArrayList<>();
		
		if (isOnline()) {
			requestData("http://gb3it.pickworth.info:3000/person_infos");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
		}
	}
	
	private void requestData(String uri) {
		PostTask task = new PostTask();
		task.execute(uri);
	}

	private void populateAutoComplete() {
		getLoaderManager().initLoader(0, null, this);
	}

	/**
	 * Attempts to register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual attempt is made.
	 */
	public void attemptRegister() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mNameView.setError(null);
		mSurnameView.setError(null);
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mPasswordCheckView.setError(null);

		// Store values at the time of the registration attempt.
		String fName = mNameView.getText().toString();
		String lName = mSurnameView.getText().toString();
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		String pwCheck = mPasswordCheckView.getText().toString();

		boolean cancel = false; // Deals with input errors
		boolean userExists = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		
		// Check entered password matches retyped password
		if (!password.equals(pwCheck)) {
			mPasswordCheckView.setError(getString(R.string.error_nonmatching_password));
			focusView = mPasswordCheckView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		
		// Check if user entered email address exists in database.
		for (Users user : userList) {
			if (user.getEmail().equals(email)) {
				// Account exists.
				userExists = true;
			}
		}
		
		if (userExists) {			
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}				

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			Toast.makeText(this, "Unsuccessful login", Toast.LENGTH_SHORT).show();
			focusView.requestFocus();
		} else {
			//Create new User Object, passing the data into sets
			newUser = new Users();
			newUser.setFirstName(fName);
			newUser.setLastName(lName);
			newUser.setEmail(email);
			newUser.setPassword(password);
			
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserRegisterTask();
			mAuthTask.execute((Void) null);
		}
	}

	private boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 3;
	}
	
	/**
	 * Shows pop-up dialog to confirm user wishes to register an account
	 */
	public void registrationDialog () {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
 
			// set title
			alertDialogBuilder.setTitle("Are you sure you want to register this account?");
 
			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked,
						// attempt registration
						attemptRegister();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
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
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mRegisterFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,

				// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	private interface ProfileQuery {
		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };

		int ADDRESS = 0;
		@SuppressWarnings("unused")
		int IS_PRIMARY = 1;
	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		// Create adapter to tell the AutoCompleteTextView what to show in its
		// dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				RegisterActivity.this,
				android.R.layout.simple_dropdown_item_1line,
				emailAddressCollection);

		mEmailView.setAdapter(adapter);
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

	/**
	 * Represents an asynchronous register task used to authenticate
	 * the user.
	 */
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

		String newUserString = UsersJSONParser.POSTUsers(newUser);

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			// register the new account here.
			HttpManager.postData("http://gb3it.pickworth.info:3000/person_infos", newUserString);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivity(i);
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	/**
	 * Represents an asynchronous task used to retrieve user data
	 * from database.
	 */
	private class PostTask extends AsyncTask<String, String, String> 
	{
		@Override
		protected void onPreExecute() {
			tasks.add(this);
		}
		
		@Override
		protected String doInBackground(String... params) {			
			String content = HttpManager.getData(params[0]);
			return content;
		}
		
		@Override
		protected void onPostExecute(String result) {			
			userList = UsersJSONParser.parseFeed(result);
			tasks.remove(this);

		}
	}
}
