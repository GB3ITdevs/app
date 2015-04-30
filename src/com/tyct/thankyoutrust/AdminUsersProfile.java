package com.tyct.thankyoutrust;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AdminUsersProfile extends Activity {
	// Session Manager Class
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_users_profile);
		
		populateFields();

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_users_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void populateFields() {
		
		SharedPreferences prefs = getSharedPreferences("prefsFile", MODE_PRIVATE);
		String namedPerson = prefs.getString("name", "name");
		int userId = prefs.getInt("userId", 0);		
		String email = prefs.getString("email", "email");
		String address = prefs.getString("address", "address");
		String suburb = prefs.getString("suburb", "suburb");
		String city = prefs.getString("city", "city");
		
		// Get references
				TextView uName = (TextView) findViewById(R.id.tvProfName);
				TextView uEmail = (TextView) findViewById(R.id.tvProfEmail);
//				TextView uPhone = (TextView) findViewById(R.id.tvPPhoneNumber);
				TextView uAddress = (TextView) findViewById(R.id.tvPStreetAddress);
				TextView uSuburb = (TextView) findViewById(R.id.tvPSuburb);
				TextView uCity = (TextView) findViewById(R.id.tvPCity);
			//	TextView uCommunity = (TextView) findViewById(R.id.tvPCommunity);
				
				
				//set textfields
				
				uName.setText(namedPerson);
				uEmail.setText(email);
//				uPhone.setText(phoneNumber);
				uAddress.setText(address);
				uSuburb.setText(suburb);
				uCity.setText(city);
		
		//Toast.makeText(this, "user name = " + namedPerson + " user id = " + userId, Toast.LENGTH_LONG).show();
		
		//populateFields(userId);
		
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

}
