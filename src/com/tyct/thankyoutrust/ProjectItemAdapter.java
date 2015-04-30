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

import com.tyct.thankyoutrust.model.ProjectRating;

public class ProjectItemAdapter extends ArrayAdapter<String>
{

		Context context;
		List<ProjectRating> ratingList;
		String[] projects;
		int userID;

		public ProjectItemAdapter(Context contextPassed, int resource, String[] projectNames, List<ProjectRating> ratings, int loggedInUserID) {
			super(contextPassed, resource, projectNames);
			
			context = contextPassed;
			ratingList = ratings;
			projects = projectNames;
			userID = loggedInUserID;
		}
		
		//Get Item and reverse order it
		@Override
		public String getItem(int position)
		{
		    return super.getItem(position);
		}
		
		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = 
					(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.item_project, parent, false);
			
			//Text View that displays the comments
			TextView tvProjectName = (TextView) view.findViewById(R.id.tvItemProjectName);
			tvProjectName.setText(projects[position]);

			RatingBar itemRating = (RatingBar) view.findViewById(R.id.itemRatingBar);
			
			//for users in the userlist where user info id equals message get name
			for(ProjectRating rating : ratingList)
			{
				if(rating.getUserID()== userID)
				{
					itemRating.setIsIndicator(false);
					itemRating.setRating(rating.getRating());
					itemRating.setIsIndicator(true);
				}
			}
			
			//return the view
			return view;
		}
}
