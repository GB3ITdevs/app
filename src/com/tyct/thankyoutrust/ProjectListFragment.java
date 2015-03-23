package com.tyct.thankyoutrust;

import com.tyct.thankyoutrust.model.Project;

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

public class ProjectListFragment extends Fragment
{
	//Declare the class fields
	
	
		List<Project> projectList;
		String[] projectNames;
		Projects ma;
		ListView projectListView;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View v = inflater.inflate(R.layout.fragment_project_list,container,false);

			ma = (Projects) getActivity();
			
			projectNames = ma.getProjects();
			
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
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				String clickedItem = (String) projectListView.getItemAtPosition(position).toString();
				ma.changeProjectDetails(clickedItem);
				
			}
			
		}
	
		
}
