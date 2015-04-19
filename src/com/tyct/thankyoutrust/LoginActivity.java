package com.tyct.thankyoutrust;

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

import java.util.ArrayList;
import java.util.List;

import com.tyct.thankyoutrust.model.ContactInfo;
import com.tyct.thankyoutrust.model.UserID;
import com.tyct.thankyoutrust.model.AdminID;
import com.tyct.thankyoutrust.model.Users;
import com.tyct.thankyoutrust.parsers.AdminIDJSONParser;
import com.tyct.thankyoutrust.parsers.ContactInfoJSONParser;
import com.tyct.thankyoutrust.parsers.UserIDJSONParser;
import com.tyct.thankyoutrust.parsers.UsersJSONParser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

	// Async Tasks
	List<UsersTask> tasks;
	List<ContactTask> contactTask;
	List<UsidTask> usidTask;
	List<AdminTask> adminTask;

	// retrieved data lists
	List<Users> userList;
	List<UserID> usidList;
	List<ContactInfo> contactList;
	List<AdminID> adminList;

	// User Session Manager Class
	SessionManager session;

	// User data
	int loggedInUserId;
	int contactId;
	int userId;
	String uName;
	String uSurname;
	String uCity;
	String uSuburb;
	String uPostcode;
	String uAdmin;

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// User Session Manager
		session = new SessionManager(getApplicationContext());

		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		populateAutoComplete();

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		// Sign in button handler
		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mLoginFormView = findViewById(R.id.email_login_form);
		mProgressView = findViewById(R.id.login_progress);

		tasks = new ArrayList<>();
		contactTask = new ArrayList<>();
		usidTask = new ArrayList<>();
		adminTask = new ArrayList<>();

		personInfo();
		userInfo();
		contactInfo();
		adminInfo();
	}

	private void populateAutoComplete() {
		getLoaderManager().initLoader(0, null, this);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false; // Deals with input errors
		boolean userExists = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
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
				break;
			}
		}

		if (!userExists) {
			registrationDialog();
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			Toast.makeText(this, "Unsuccessful login", Toast.LENGTH_SHORT)
					.show();
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask(email, password);
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
	public void registrationDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				LoginActivity.this);

		// set title
		alertDialogBuilder
				.setTitle("No account is currently registered to this email address");

		// set dialog message
		alertDialogBuilder
				.setMessage("Do you want to register a new account?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked,
								// go to registration activity
								Intent registration = new Intent(
										LoginActivity.this,
										RegisterActivity.class);
								String email = (String) mEmailView.getText()
										.toString();
								registration.putExtra("email", email);
								startActivity(registration);
								LoginActivity.this.finish();
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

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
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
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
				LoginActivity.this,
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

	private void requestData(String uri) {
		UsersTask task = new UsersTask();
		task.execute(uri);
	}

	private void requestUserData(String uri) {
		UsidTask task = new UsidTask();
		task.execute(uri);
	}

	private void requestContactData(String uri) {
		ContactTask task = new ContactTask();
		task.execute(uri);
	}
	
	private void requestAdminData(String uri) {
		AdminTask task = new AdminTask();
		task.execute(uri);
	}

	public void personInfo() {
		if (isOnline()) {
			requestData("http://gb3it.pickworth.info:3000/person_infos");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void userInfo() {
		if (isOnline()) {
			requestUserData("http://gb3it.pickworth.info:3000/users");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void contactInfo() {
		if (isOnline()) {
			requestContactData("http://gb3it.pickworth.info:3000/contact_infos");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}
	
	public void adminInfo() {
		if (isOnline()) {
			requestAdminData("http://gb3it.pickworth.info:3000/administrators");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * Represents an asynchronous login/register task used to authenticate the
	 * user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String mEmail;
		private final String mPassword;

		UserLoginTask(String email, String password) {
			mEmail = email;
			mPassword = password;
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			for (Users user : userList) {
				if (user.getEmail().equals(mEmail)) {
					// if the password matches, retrieve further user info
					if (user.getPassword().equals(mPassword)) {
						loggedInUserId = user.getInfoID();
						uName = user.getFirstName();
						uSurname = user.getLastName();

						for (UserID usid : usidList) {
							if (usid.getInfoID() == loggedInUserId) {
								// get userID
								userId = usid.getUserID();
								// get contactID
								contactId = usid.getContactID();
								break;
							}
						}
						
						//Admin 
						//If infor id is in admin page make string a 1, otherwise a 0
						uAdmin = "0";
						for (AdminID adm : adminList) {
							if(adm.getInfoID()==loggedInUserId){
								uAdmin ="1";
							}
							
						}
						
//TODO fix this shit
//						for (ContactInfo ci : contactList) {
//							if (ci.getContactID() == loggedInUserId) {
//								// get address
//								// TODO uAddress = ci.getAddress();
//								// get suburb
//								uSuburb = ci.getSuburb();
//								// get city
//								uCity = ci.getCity();
//								// get postcode
//								uPostcode = Integer
//										.toString(ci.getPostalCode());
//								break;
//							}
//						}
					}
					return user.getPassword().equals(mPassword);
				}
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				session.createUserLoginSession(loggedInUserId, mEmail, uName,
						uSurname, uSuburb, uCity, uPostcode, uAdmin);
				Intent i = new Intent(LoginActivity.this, MainActivity.class);
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
	 * Represents an asynchronous task used to retrieve user data from person
	 * info.
	 */
	private class UsersTask extends AsyncTask<String, String, String> {

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

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}

	/**
	 * Represents an asynchronous task used to retrieve contact data from
	 * contact info.
	 */
	private class ContactTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			contactTask.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			contactList = ContactInfoJSONParser.parseFeed(result);
			contactTask.remove(this);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}

	/**
	 * Represents an asynchronous task used to retrieve user id data from users
	 */
	private class UsidTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			usidTask.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			usidList = UserIDJSONParser.parseFeed(result);
			usidTask.remove(this);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}
	
	/**
	 * Represents an asynchronous task used to retrieve admin data from
	 * contact info.
	 */
	private class AdminTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			adminTask.add(this);
		}

		@Override
		protected String doInBackground(String... params) {
			String content = HttpManager.getData(params[0]);
			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			adminList = AdminIDJSONParser.parseFeed(result);
			adminTask.remove(this);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}
}