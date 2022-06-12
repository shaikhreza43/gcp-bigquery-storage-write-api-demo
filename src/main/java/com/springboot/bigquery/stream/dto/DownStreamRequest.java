/**
 * 
 */
package com.springboot.bigquery.stream.dto;

import javax.validation.constraints.NotNull;

/**
 * @author Shaikh Ahmed Reza
 *
 */
public class DownStreamRequest {

	@NotNull(message = "RequestId Cannot be Null or Empty")
	private String requestId;

	@NotNull(message = "Source Cannot be Null or Empty")
	private String source;

	@NotNull(message = "ProjectType Cannot be Null or Empty")
	private String projectType;

	@NotNull(message = "Environment Cannot be Null or Empty")
	private String environment;

	@NotNull(message = "Action Cannot be Null or Empty")
	private String action;

	@NotNull(message = "DownStreamApplicationServiceAccount Cannot be Null or Empty")
	private String downStreamApplicationServiceAccount;

	@NotNull(message = "DownStreamApplicationEamsNumber Cannot be Null or Empty")
	private String downStreamApplicationEamsNumber;

	@NotNull(message = "LastApprovedTimeStamp Cannot be Null or Empty")
	private long lastApprovedTimeStamp;

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the projectType
	 */
	public String getProjectType() {
		return projectType;
	}

	/**
	 * @param projectType the projectType to set
	 */
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the downStreamApplicationServiceAccount
	 */
	public String getDownStreamApplicationServiceAccount() {
		return downStreamApplicationServiceAccount;
	}

	/**
	 * @param downStreamApplicationServiceAccount the
	 *                                            downStreamApplicationServiceAccount
	 *                                            to set
	 */
	public void setDownStreamApplicationServiceAccount(String downStreamApplicationServiceAccount) {
		this.downStreamApplicationServiceAccount = downStreamApplicationServiceAccount;
	}

	/**
	 * @return the downStreamApplicationEamsNumber
	 */
	public String getDownStreamApplicationEamsNumber() {
		return downStreamApplicationEamsNumber;
	}

	/**
	 * @param downStreamApplicationEamsNumber the downStreamApplicationEamsNumber to
	 *                                        set
	 */
	public void setDownStreamApplicationEamsNumber(String downStreamApplicationEamsNumber) {
		this.downStreamApplicationEamsNumber = downStreamApplicationEamsNumber;
	}

	/**
	 * @return the lastApprovedTimeStamp
	 */
	public long getLastApprovedTimeStamp() {
		return lastApprovedTimeStamp;
	}

	/**
	 * @param lastApprovedTimeStamp the lastApprovedTimeStamp to set
	 */
	public void setLastApprovedTimeStamp(long lastApprovedTimeStamp) {
		this.lastApprovedTimeStamp = lastApprovedTimeStamp;
	}

}
