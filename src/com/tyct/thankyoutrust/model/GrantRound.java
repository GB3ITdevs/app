package com.tyct.thankyoutrust.model;

public class GrantRound {
	
	private int roundID;
	private int communityID;
	private String startDate;
	private String endDate;
	
	
	public int getRoundID() {
		return roundID;
	}
	public void setRoundID(int roundID) {
		this.roundID = roundID;
	}
	public int getCommunityID() {
		return communityID;
	}
	public void setCommunityID(int communityID) {
		this.communityID = communityID;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
