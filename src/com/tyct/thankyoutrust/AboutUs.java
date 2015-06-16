package com.tyct.thankyoutrust;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AboutUs extends Activity {

	// User Session Manager Class
    SessionManager session;
    
    boolean userIsLoggedIn;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);	
		
		 // Session class instance
        session = new SessionManager(getApplicationContext());
        userIsLoggedIn = session.isUserLoggedIn();
        
        //Make background image transparent
//        View backgroundimage = findViewById(R.id.about_us);
//        Drawable background = backgroundimage.getBackground();
//        background.setAlpha(70);
        
	}
}
