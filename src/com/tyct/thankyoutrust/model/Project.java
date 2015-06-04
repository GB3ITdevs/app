package com.tyct.thankyoutrust.model;

public class Project {
	private int projectID;
	private int roundID;
	private String applicantName;
	private String applicantEmail;
	private String projectBlurb;
	private String fundsBlurb;
	private String organizationBlurb;
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

	
	public String getProjectBlurb() {
		return projectBlurb;
	}
	
	public void setProjectBlurb(String projectBlurb) {
		this.projectBlurb = projectBlurb;
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

	public String getApplicantEmail() {
		return applicantEmail;
	}

	public void setApplicantEmail(String applicantEmail) {
		this.applicantEmail = applicantEmail;
	}

	public String getFundsBlurb() {
		return fundsBlurb;
	}

	public void setFundsBlurb(String fundsBlurb) {
		this.fundsBlurb = fundsBlurb;
	}

	public String getOrganizationBlurb() {
		return organizationBlurb;
	}

	public void setOrganizationBlurb(String organizationBlurb) {
		this.organizationBlurb = organizationBlurb;
	}
}
