package com.tyct.thankyoutrust.parsers;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tyct.thankyoutrust.model.Message;

public class MessageJSONParser {
	
	public static List<Message> parseFeed(String content) {
		
		try {
			JSONArray ar = new JSONArray(content);
			List<Message> messageList = new ArrayList<>();
			
			for (int i = 0; i < ar.length(); i++) {
				
				JSONObject obj = ar.getJSONObject(i);
				Message message = new Message();
				
				message.setCommentID(obj.getInt("commentID"));
				message.setInfoID(obj.getInt("infoID"));
				message.setPostalCode(obj.getInt("postalCode"));
				message.setComment(obj.getString("comment"));
				message.setDate(obj.getString("date"));
			
				messageList.add(message);
			}
			
			return messageList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}