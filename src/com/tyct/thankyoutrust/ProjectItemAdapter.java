package com.tyct.thankyoutrust;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tyct.thankyoutrust.model.Project;
import com.tyct.thankyoutrust.model.ProjectRating;

public class ProjectItemAdapter extends ArrayAdapter<Project>
{

		Context context;
		List<ProjectRating> ratingList;
		List<Project> projects;
		int userID;

		public ProjectItemAdapter(Context contextPassed, int resource, List<Project> projectList, List<ProjectRating> ratings, int loggedInUserID) {
			super(contextPassed, resource, projectList);
			
			context = contextPassed;
			ratingList = ratings;
			projects = projectList;
			userID = loggedInUserID;
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
			View view = inflater.inflate(R.layout.item_project, parent, false);
			
			//Text View that displays the comments
			TextView tvProjectName = (TextView) view.findViewById(R.id.tvItemProjectName);
			
			tvProjectName.setText(currentProject.getProjectName());

			RatingBar itemRating = (RatingBar) view.findViewById(R.id.itemRatingBar);
			
			 GradientDrawable shape = (GradientDrawable) view.findViewById(R.id.backgroundShape).getBackground();
			
			 if(currentProject.getStatus().equals("In Progress"))
			 {
				 if(currentProject.getStatus().equals("In Progress"))
				 {
					 shape.setColor(Color.LTGRAY);
				 }
			 }
			 else
			 {
				 shape.setColor(Color.argb(190, 83, 158, 228));
			 }
			 
			 
			
			//for users in the userlist where user id equals message get name
			for(ProjectRating rating : ratingList)
			{
				if((rating.getUserID()== userID) && (rating.getProjectID() == currentProject.getProjectID()))
				{
					itemRating.setRating(rating.getRating());
				}
			}
			
			//return the view
			return view;
		}
}
