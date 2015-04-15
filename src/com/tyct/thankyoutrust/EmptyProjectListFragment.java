package com.tyct.thankyoutrust;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmptyProjectListFragment extends Fragment
{
	//Declare the class fields

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View v = inflater.inflate(R.layout.fragment_empty_projects_list,container,false);

			return v;
		}	
}
