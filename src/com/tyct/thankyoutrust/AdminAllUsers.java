package com.tyct.thankyoutrust;

import java.util.ArrayList;
import java.util.List;

import com.tyct.thankyoutrust.model.AdminID;
import com.tyct.thankyoutrust.model.Users;
import com.tyct.thankyoutrust.parsers.AdminIDJSONParser;
import com.tyct.thankyoutrust.parsers.UsersJSONParser;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AdminAllUsers extends Activity {
	
	List<Users> userList;
	List<AdminID> adminList;
	List<AdminTask> adminTask;
	List<DeleteAdminTask> deleteAdminTask;
	String[] userNames;
	List<MyTask> tasks;
	List<PostAdminTask> postadmintask;
	DialogFragment userOptions;
	OptionsDialog dialog;
	String selectedItem = "";
	int[] UsersInfoId;
	int selectedInfoId;
	int adminid;
	AdminID adminEntity;
	//Array for Admin Options
	String[] optionsArray = {"Set as admin", "Remove as admin", "Delete user", "Change community"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_all_users);
		
		//start async task
		tasks = new ArrayList<>();
		adminTask = new ArrayList<>();
		
		//makes connection to database
		display();
		adminInfo();
		
		//setup listview to and call method for clickable
		ListView groupAct = (ListView) findViewById(R.id.lstvewuser);
		groupAct.setOnItemClickListener(new ListViewClickHandler());

	}

	//Top menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//Menu Options
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent goTo = new Intent();
		if (item.getItemId() == R.id.action_projects)  //Project page
		{
			goTo = new Intent(AdminAllUsers.this, Projects.class);
			
		}
		if (item.getItemId() == R.id.action_home)  //home page
		{
			goTo = new Intent(AdminAllUsers.this, MainActivity.class);
		}
		if (item.getItemId() == R.id.action_about_us) //about us page
		{
			goTo = new Intent(AdminAllUsers.this, AboutUs.class);
		}
		if (item.getItemId() == R.id.admin) //admin page
		{
			goTo = new Intent(AdminAllUsers.this, AdminHomePage.class);
		}
		startActivity(goTo);
		return false;
	}

	public int getInfoId(int positionClicked)
	{
		int infoID = UsersInfoId[positionClicked];
		return infoID;
		
	}
	private void requestData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}
	


	//Method to setup the List View to display all users 
	public void setUserList(List<Users> userList)
	{

		userNames = new String[userList.size()];
		UsersInfoId = new int[userList.size()];
		int i = 0;
		//Add each user name from the project list to the array of strings
		for(Users user : userList)
		{
			userNames[i] = user.getFirstName() + " " + user.getLastName();
			UsersInfoId[i]=user.getInfoID();
			i++;
		}
		
		//Create the adapter
		ArrayAdapter<String> adminOptionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userNames);
		//Create the ListView
		ListView userNameListView = (ListView) findViewById(R.id.lstvewuser);
		//Bind the ListView to the above adapter
		userNameListView.setAdapter(adminOptionsAdapter);
	}
	

	//Method that checks if phone is online
	public void display(){
		if (isOnline()) {
			requestData("http://gb3it.pickworth.info:3000/person_infos");
		} else {
			Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG)
					.show();
		}
	}
	
	private void requestAdminData(String uri) {
		AdminTask task = new AdminTask();
		task.execute(uri);
	}
	
	public void adminInfo() {
		if (isOnline()) {
			requestAdminData("http://gb3it.pickworth.info:3000/administrators");
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
	
	// Handles the posting side
		private class PostAdminTask extends AsyncTask<String, String, String> {
			String adminEntityString = AdminIDJSONParser.POST(adminEntity);

			@Override
			protected void onPreExecute() {
				postadmintask.add(this);
			}

			@Override
			protected String doInBackground(String... params) {
				HttpManager.postData("http://gb3it.pickworth.info:3000/administrators",
						adminEntityString);
				String result = "Admin Posted";
				return result;
			}

			@Override
			protected void onPostExecute(String result) {

				// String messageResult = (result);

				postadmintask.remove(this);
				
			}
			
			

			@Override
			protected void onProgressUpdate(String... values) {
			}

		}
		
		// Handles the posting side
				private class DeleteAdminTask extends AsyncTask<String, String, String> {
					String deleteAdmin = "http://gb3it.pickworth.info:3000/administrators/";
					@Override
					protected void onPreExecute() {
						deleteAdminTask.add(this);
					}

					@Override
					protected String doInBackground(String... params) {
						HttpManager.deleteData(deleteAdmin+adminid);
						String result = "Admin Deleted";
						return result;
					}

					@Override
					protected void onPostExecute(String result) {

						// String messageResult = (result);

						deleteAdminTask.remove(this);
						
					}
					
					

					@Override
					protected void onProgressUpdate(String... values) {
					}

				}
		
		/**
    	 * Represents an asynchronous task used to retrieve contact data from
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
	private class MyTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// updateDisplay("Starting task");
			
		}

		@Override
		protected String doInBackground(String... params) {

			//HttpManager
			String content = HttpManager.getData(params[0]);
			return content;

		}

		@Override
		protected void onPostExecute(String result) {

			//populates userlist from parser
			userList = UsersJSONParser.parseFeed(result);
			//populates the list view using the setUserList Method.
			setUserList(userList);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// updateDisplay(values[0]);
		}

	}
	
	
	//Handles the ListView clicks
	public class ListViewClickHandler implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> list, View itemview, int posistion, long id) {
			//Gets the string of what is been clicked on.
			String clickedItemString = (String) list.getItemAtPosition(posistion).toString();
			int selectedInfoID = getInfoId(posistion);
			//debugging
			//Toast.makeText(AdminAllUsers.this, clickedUsersInfoId, Toast.LENGTH_LONG).show();
			
			dialog = new OptionsDialog(clickedItemString, selectedInfoID);
			FragmentManager fm = getFragmentManager();
			dialog.show(fm, "confirm");
			
		}
		
	}
	
	//Method to return data to the Dialog Fragment
    public void setDialogResults(boolean result)
    {
    	dialog.dismiss();
    	
    	if(result == true)
    	{
    		//Toast.makeText(AdminAllUsers.this, "Submitted you choice " + selectedItem, Toast.LENGTH_LONG).show();
    		setOptionIntents();
    	}
    	
    	if (result == false)
    	{
    		//Toast.makeText(AdminAllUsers.this, "Cancelled", Toast.LENGTH_LONG).show();
    	}
    	
    }
   
    //Method where selected options are implemented
    public void setOptionIntents()
    {
    	//if selected item is same as optionArray[0] (Set As admin) then add as an admin
		if (selectedItem == optionsArray[0]) 
		{
			addAdmin(selectedInfoId);			
		}
		//if selected item is same as optionArray[1] (Remove As admin) then delete admin status
		if (selectedItem == optionsArray[1]) 
		{
			deleteAdmin();	
		}
		//if selected item is same as optionArray[2] (delete user) then delete user
		if (selectedItem == optionsArray[2]) 
		{
			deleteUser();	
		}
		
		//if selected item is same as optionArray[3] (Change community) then change community
		if (selectedItem == optionsArray[3]) 
		{
			Toast.makeText(AdminAllUsers.this, optionsArray[3] + " " + selectedItem, Toast.LENGTH_LONG).show();
		}
    }
  
    //method to delete a user
    public void deleteUser()
    {
    
    	Toast.makeText(AdminAllUsers.this, optionsArray[2] + " " + selectedItem, Toast.LENGTH_LONG).show();
    }
    
    //method to delete a user
    public void deleteAdmin()
    {
    	
    	adminTask = new ArrayList<>();
    	
    	
    	for(AdminID adm : adminList)
		{
    		if (selectedInfoId == adm.getInfoID())
    		{
    			 adminid = adm.getAdminID();
    				deleteAdminTask = new ArrayList<>();
        			DeleteAdminTask task = new DeleteAdminTask();
        			task.execute();
    			//Toast.makeText(AdminAllUsers.this, adm.getInfoID() + " " + selectedInfoId +" are all there" + " " + adminid, Toast.LENGTH_LONG).show();
    		}
			
		}
    	//Toast.makeText(AdminAllUsers.this, optionsArray[1] + " " + selectedItem, Toast.LENGTH_LONG).show();
    }
    
    //method to delete a user
    public void addAdmin(int infoId)
    {
    	//adminInfo();
    	adminEntity = new AdminID();
    	adminEntity.setInfoID(infoId);

    	postadmintask = new ArrayList<>();
		PostAdminTask task = new PostAdminTask();
		task.execute();
    	//Toast.makeText(AdminAllUsers.this, optionsArray[0] + " " + selectedItem + "  selected info ID "+ infoId, Toast.LENGTH_LONG).show();
    }

	public class OptionsDialog extends android.app.DialogFragment  {
    	//selected user
    	String selectedUser;
    	
    	

    public OptionsDialog() {}
    public OptionsDialog(String user, int infoId) {
    	selectedUser = user;
    	selectedInfoId = infoId ;
    }
    	
    	public Dialog onCreateDialog(Bundle savedInstanceState)
    	{
    		
    		Builder builder = new AlertDialog.Builder(getActivity());
    		//Set Title
    		builder.setTitle("Select Options for "+ selectedUser);
    		//Set single choice options (radio buttons)
    		builder.setSingleChoiceItems(optionsArray,  -1, new OnMultiChoiceClickListener());
    		//set Submit Button
    		builder.setPositiveButton("Submit", new positiveListener());
    		//set Cancel Button
    		builder.setNegativeButton("Cancel", new negativeListener());
    		
    		Dialog dialog = builder.create();
    		
    		return dialog;
    		
    	}
    	
    	public class positiveListener implements DialogInterface.OnClickListener
    	{

    		@Override
    		public void onClick(DialogInterface dialog, int which) 
    		{
    			setDialogResults(true);
    		}
    		
    	}
    	
    	public class negativeListener implements DialogInterface.OnClickListener
    	{

    		@Override
    		public void onClick(DialogInterface dialog, int which) 
    		{
    			setDialogResults(false);
    		}
    		
    	}
    	
    	public class OnMultiChoiceClickListener implements DialogInterface.OnClickListener, android.content.DialogInterface.OnMultiChoiceClickListener
    	{

			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				//use this method for checkboxes			
			}

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				//Get selected Item from radio buttons
				selectedItem =optionsArray[which];
				
				//debugging
				//Toast.makeText(AdminAllUsers.this, selectedItem, Toast.LENGTH_LONG).show();
			}
    		
    	}
    	
    	

    }
}
