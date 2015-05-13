package com.tyct.thankyoutrust;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeScreen extends Activity {
	// User Session Manager Class
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);

		// Session class instance
		session = new SessionManager(getApplicationContext());
		// If a user is currently logged in, go straight to message board
		if (session.isUserLoggedIn()) {
			Intent intent = new Intent(WelcomeScreen.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}

		// Login button
		Button buttonLogin = (Button) findViewById(R.id.btnLogin);
		buttonLogin.setOnClickListener(new loginButton());

		// Register button
		Button buttonRegister = (Button) findViewById(R.id.btnRegister);
		buttonRegister.setOnClickListener(new RegisterButton());

		// About us button
		Button buttonAboutUs = (Button) findViewById(R.id.btnAboutUs);
		buttonAboutUs.setOnClickListener(new AboutUsButton());
	}

	// Method for Login Button
	public class loginButton implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
			startActivity(intent);
		}
	}

	// Method for Registration page
	public class RegisterButton implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WelcomeScreen.this,
					RegisterActivity.class);
			startActivity(intent);
		}
	}

	// Method to go to About Us page
	public class AboutUsButton implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WelcomeScreen.this, AboutUs.class);
			startActivity(intent);
		}
	}
}