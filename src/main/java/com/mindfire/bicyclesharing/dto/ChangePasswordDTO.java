package com.mindfire.bicyclesharing.dto;

public class ChangePasswordDTO {

	private String oldPassword;
	private String newPassword;
	private String cnfPassword;

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword
	 *            the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword
	 *            the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the cnfPassword
	 */
	public String getCnfPassword() {
		return cnfPassword;
	}

	/**
	 * @param cnfPassword
	 *            the cnfPassword to set
	 */
	public void setCnfPassword(String cnfPassword) {
		this.cnfPassword = cnfPassword;
	}

}
