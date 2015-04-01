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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome_screen, menu);
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

	// Method to got to About Us page
	public class AboutUsButton implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WelcomeScreen.this, AboutUs.class);
			startActivity(intent);

		}

	}
}
