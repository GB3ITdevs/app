package com.tyct.thankyoutrust.model;

public class Comment {
	private int commentID;
	private int userID;
	private int communityID;
	private String comment;
	private String date;
	
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int commentId) {
		this.commentID = commentId;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getCommunityID() {
		return communityID;
	}
	public void setCommunityID(int communityID) {
		this.communityID = communityID;
	}
}
