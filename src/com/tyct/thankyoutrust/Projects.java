package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.parsers.ProjectsJSONParser;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Projects extends Activity 
{
	
	ProgressBar pb;
	List<MyTask> tasks;
	List<Project> projectList;
	ListView projectListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projects);
		
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
		
		tasks = new ArrayList<>();
		
			if (isOnline()) 
			{
				requestData("http://gb3it.pickworth.info:3000/projects");	
			} else 
			{
				Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
			}
		
		//ListView projectListView = (ListView) findViewById(R.id.projectList);
		
		//projectListView.set
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.projects, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void requestData(String uri) 
	{
		MyTask task = new MyTask();
		task.execute(uri);
	}
	
	protected void updateDisplay() {
		
		if (projectList != null) 
		{
			for (Project project : projectList) 
			{
				//projectListView.Set
			}
		}
		
	}
	
	protected boolean isOnline() 
	{
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
		protected void onPostExecute(String result) 
		{	
			projectList = ProjectsJSONParser.parseFeed(result);
			updateDisplay();

			tasks.remove(this);
			if (tasks.size() == 0) {
				pb.setVisibility(View.INVISIBLE);
			}

		}
		
		@Override
		protected void onProgressUpdate(String... values) 
		{
//			updateDisplay(values[0]);
		}
		
	}
}
