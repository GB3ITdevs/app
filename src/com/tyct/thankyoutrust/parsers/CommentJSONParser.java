package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.Comment;

public class CommentJSONParser {
	
	public static List<Comment> parseFeed(String content) {
		
		try {
			JSONArray ar = new JSONArray(content);
			List<Comment> commentList = new ArrayList<>();
			
			for (int i = 0; i < ar.length(); i++) {
				
				JSONObject obj = ar.getJSONObject(i);
				Comment comment = new Comment();
				
				comment.setCommentID(obj.getInt("commentID"));
				comment.setUserID(obj.getInt("userID"));
				comment.setCommunityID(obj.getInt("communityID"));
				comment.setComment(obj.getString("comment"));
				comment.setDate(obj.getString("date"));
			
				commentList.add(comment);
			}
			
			return commentList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	// Post user data to database
				public static String POSTComment(Comment comment) {
					String json = "";
					JSONObject jsonComment = new JSONObject();
					try {
						jsonComment.accumulate("userID", Integer.toString(comment.getUserID()));
						jsonComment.accumulate("communityID", Integer.toString(comment.getCommunityID()));
						jsonComment.accumulate("comment", comment.getComment());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					json = "{\"comment\":" + jsonComment.toString() + "}";
					return json;
				}
			}
