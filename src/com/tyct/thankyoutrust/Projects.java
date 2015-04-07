package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.parsers.ProjectsJSONParser;

public class Projects extends Activity 
{
	List<MyTask> tasks;
	List<Project> projectList;
	String[] projectNames;
	ProgressBar pb;
	Project selectedProject;
	
	SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projects);
		
		//Create a new list of tasks
		tasks = new ArrayList<>();
		
		//Initialize the progress bar and set it to not be visible
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
		
		prefs = getSharedPreferences("UserDetails", MODE_PRIVATE);
		
		//If the phone is online retrieve the projects info from the url
		if (isOnline()) 
		{
			//Begin the request data method below
			requestData("http://gb3it.pickworth.info:3000/projects");

		} 
		//Otherwise make a toast message to state the network is not available
		else 
		{
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
		}
		selectedProject = new Project();
		
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

	//To do: create the top menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//Auto-generated:
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent goTo = new Intent();
		if (item.getItemId() == R.id.action_projects) 
		{
			goTo = new Intent(Projects.this, Projects.class);
			
		}
		if (item.getItemId() == R.id.action_home) 
		{
			goTo = new Intent(Projects.this, MainActivity.class);
		}
		if (item.getItemId() == R.id.action_about_us) 
		{
			goTo = new Intent(Projects.this, AboutUs.class);
		}
		startActivity(goTo);
		finish();
		return false;
	}

	
	//Method to create a and start a new task
	private void requestData(String uri) 
	{
		//Create the new async task
		MyTask task = new MyTask();
		//Start it using the url that has been passed into the method
		task.execute(uri);
	}
	
	//Method to replace the list fragment with the details fragment
	public void changeProjectDetails(String clickedItem)
	{
		for (int i = 0; i < projectList.size(); i++) 
		{
			if(clickedItem == projectList.get(i).getProjectName())
			{
				selectedProject = projectList.get(i);
			}
		}
		
		//Create new Fragments
		Fragment projectDetailsFrag = new ProjectDetailsFragment();
		
		//Create a fragment manager
		FragmentManager fm = getFragmentManager();
		
		//Create a new fragment transaction
		FragmentTransaction ft = fm.beginTransaction();
		
		//Replace the list container with the new details fragment
		ft.replace(R.id.fragment_container2, projectDetailsFrag);
		
		//Commit the transaction changes
		ft.commit();
	}
	
	//Method to setup the project name list and display the project list fragment
	public void setProjectList(List<Project> projectList)
	{

		projectNames = new String[projectList.size()];
		int i = 0;
		//Add each project name from the project list to the array of strings
		for(Project project : projectList)
		{
			projectNames[i] = project.getProjectName();
			i++;
		}
		
		//Create new Fragments
		Fragment projectListFrag = new ProjectListFragment();
		
		//Create a fragment manager
		FragmentManager fm = getFragmentManager();
		
		//Create a new fragment transaction
		FragmentTransaction ft = fm.beginTransaction();
		
		//Replace the empty container with the fragment
		ft.replace(R.id.fragment_container1, projectListFrag);
		
		//Commit the transaction changes
		ft.commit();	
	}
	
	//Method to retrieve the array of project names for use in the fragments
	public String[] getProjects()
	{
		
		return projectNames;
	}
	
	//Method to retrieve the array of project names for use in the fragments
	public Project getSelectProject()
	{
		
		return selectedProject;
	}

	//Method to check if the phone is connected to a network and online
	protected boolean isOnline() 
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		
		if (netInfo != null && netInfo.isConnectedOrConnecting()) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}
	
	//Inner class for performing network activity
	private class MyTask extends AsyncTask<String, String, String> 
	{
		
		//Tasks pre-execute method
		@Override
		protected void onPreExecute() 
		{
			//If the task has been started set the progess bar to visible
			if (tasks.size() == 0) 
			{
				pb.setVisibility(View.VISIBLE);
			}
			tasks.add(this);
		}
		
		//Tasks do in background method
		@Override
		protected String doInBackground(String... params) 
		{
			//Create a new string from the http managers get data method and return it
			String content = HttpManager.getData(params[0]);
			return content;
		}
		
		//Tasks post-execute method
		@Override
		protected void onPostExecute(String result) 
		{
			//Create a new list of projects from the JSON parser using the passed in string from the http manager
			projectList = ProjectsJSONParser.parseFeed(result);
			//Populate the list fragment using the set project list method
			setProjectList(projectList);
			//Remove the current task and set the progress bar to be invisible again
			tasks.remove(this);
			if (tasks.size() == 0) 
			{
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
