package com.tyct.thankyoutrust.model;

public class Project {
	private int projectID;
	private int roundID;
	private String applicantName;
	private String projectName;
	private String projectBlurb;
	private int fundsRequested;
	private String useOfFunds;
	private String status;
	
	
	public int getProjectID() {
		return projectID;
	}
	
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	
	
	public String getApplicantName() {
		return applicantName;
	}
	
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	public String getProjectBlurb() {
		return projectBlurb;
	}
	
	public void setProjectBlurb(String projectBlurb) {
		this.projectBlurb = projectBlurb;
	}
	
	
	public int getFundsRequested() {
		return fundsRequested;
	}
	
	public void setFundsRequested(int fundsRequested) {
		this.fundsRequested = fundsRequested;
	}
	
	
	public String getUseOfFunds() {
		return useOfFunds;
	}
	public void setUseOfFunds(String useOfFunds) {
		this.useOfFunds = useOfFunds;
	}

	public int getRoundID() {
		return roundID;
	}

	public void setRoundID(int communityID) {
		this.roundID = communityID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
