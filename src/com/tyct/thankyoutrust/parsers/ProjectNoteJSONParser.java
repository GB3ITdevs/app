package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.ProjectNote;

public class ProjectNoteJSONParser {

	public static List<ProjectNote> parseFeed(String content) {

		try {
			// Create a JSON Array from the string parsed into the method
			JSONArray ar = new JSONArray(content);

			// Create a list of user ID objects
			List<ProjectNote> projectNoteList = new ArrayList<>();

			// Loop over each item in the array
			for (int i = 0; i < ar.length(); i++) {
				// Create a new JSON object from the array item
				JSONObject obj = ar.getJSONObject(i);

				// Create a new Project object
				ProjectNote projectNote = new ProjectNote();

				// Set each JSON user ID field to the appropriate user ID object
				// field
				projectNote.setNoteID(obj.getInt("noteID"));
				projectNote.setProjectID(obj.getInt("projectID"));
				projectNote.setUserID(obj.getInt("userID"));
				projectNote.setNote(obj.getString("note"));

				// Add the user ID object to the list of Projects
				projectNoteList.add(projectNote);
			}
			// Return the list of project note objects
			return projectNoteList;
		}

		// If there is an exception print the stack trace and return a null
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method to parse the Android user ID object to JSON user ID format in
	// order to post
	public static String POST(ProjectNote projectNote) {
		// Create a new empty string
		String json = "";

		// Create a new JSON object
		JSONObject jsonProjectNote = new JSONObject();

		try {
			// For each field in the parsed in user ID object, set it to the
			// appropriate JSON object field
			jsonProjectNote.accumulate("projectID",
					Integer.toString(projectNote.getProjectID()));
			jsonProjectNote.accumulate("userID",
					Integer.toString(projectNote.getUserID()));
			jsonProjectNote.accumulate("note", projectNote.getNote());

		}
		// If there is an exception print the stack trace
		catch (JSONException e) {
			e.printStackTrace();
		}

		// Set the string to the JSON object to string method
		json = "{\"project_note\":" + jsonProjectNote.toString() + "}";

		// Return the JSON String
		return json;
	}
}
