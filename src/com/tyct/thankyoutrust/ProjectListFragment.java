package com.tyct.thankyoutrust;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tyct.thankyoutrust.model.Project;

public class ProjectListFragment extends Fragment
{
	//Declare the class fields
	
	
		List<Project> projectList;
		String[] projectNames;
		Projects ma;
		ListView projectListView;
		private Callbacks mCallbacks = sCallbacks;		
		
		
		public interface Callbacks 
		{
			/**
			 * Callback for when an item has been selected.
			 */
			public void onItemSelected(String id);
		}

		private static Callbacks sCallbacks = new Callbacks() 
		{
			@Override
			public void onItemSelected(String id) 
			{
			}
		};
		

		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View v = inflater.inflate(R.layout.fragment_project_list,container,false);

			ma = (Projects) getActivity();
			
			projectNames = ma.getProjects();
			//projectList = ma.getProjectList();
			
			projectListView = (ListView) v.findViewById(R.id.projectListView);
			
			ArrayAdapter<String> projectNamesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, projectNames);
			OnItemClickListener listItemClick = new onListItemClick();
			
			projectListView.setAdapter(projectNamesAdapter);
			
			projectListView.setOnItemClickListener(listItemClick);
			
			return v;
		}
		
		public class onListItemClick implements OnItemClickListener
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				String clickedItem = (String) projectListView.getItemAtPosition(position).toString();
				//Project selectedProject = projectList.get(position);
				ma.changeProjectDetails(clickedItem);
				
				//mCallbacks.onItemSelected(clickedItem);
				
			}
			
		}		
}
