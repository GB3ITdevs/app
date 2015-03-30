package com.tyct.thankyoutrust.model;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Message {
	private int commentID;
	private int infoID;
	private int postalCode;
	private String comment;
	private String date;
	
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int commentId) {
		this.commentID = commentId;
	}
	public int getInfoID() {
		return infoID;
	}
	public void setInfoID(int infoId) {
		this.infoID = infoId;
	}
	public int getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDate() {
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
//		String dateString = sdf.format(date);
		
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
