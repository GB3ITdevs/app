package com.tyct.thankyoutrust;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.ProjectRating;

public class AdminProjectItemAdapter extends ArrayAdapter<Project>
{

		Context context;
		List<ProjectRating> ratingList;
		List<Project> projects;
		AdministrativeCalculations calculate;

		public AdminProjectItemAdapter(Context contextPassed, int resource, List<Project> projectList, List<ProjectRating> ratings) {
			super(contextPassed, resource, projectList);
			
			context = contextPassed;
			ratingList = ratings;
			projects = projectList;
			
			calculate = new AdministrativeCalculations();
		}
		
		//Get Item and reverse order it
		@Override
		public Project getItem(int position)
		{
		    return super.getItem(position);
		}
		
		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			Project currentProject = projects.get(position);

			LayoutInflater inflater = 
					(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.item_admin_project, parent, false);
			
			//Text View that displays the comments
			TextView tvProjectName = (TextView) view.findViewById(R.id.tvItemAdminProjectName);
			tvProjectName.setText(currentProject.getProjectName());

			RatingBar itemRating = (RatingBar) view.findViewById(R.id.itemAdminRatingBar);
			

			int averageRating = calculate.CalculateAverageRating(ratingList, currentProject.getProjectID());
			itemRating.setRating(averageRating);
			
			//return the view
			return view;
		}
}
