/*
 * Copyright 2016 Mindfire Solutions
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mindfire.bicyclesharing.component;

import org.springframework.stereotype.Component;

/**
 * This component class is used for setting the message coming from the message.properties file
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class MessageBean {

	private String changePassword;
	private String disabled;
	private String expired;
	private String forgetPassword;
	private String invalidOldPassword;
	private String invalidToken;
	private String invalidUser;
	private String regError;
	private String regSucc;
	private String resendToken;
	private String resetPassword;
	private String resetPasswordEmail;
	private String resetPasswordSuc;
	private String resetYourPassword;
	private String updatePasswordSuc;
	private String userNotFound;
	private String contextPath;
	private String alreadyActivated;
	
	
	/**
	 * @return the alreadyActivated
	 */
	public String getAlreadyActivated() {
		return alreadyActivated;
	}

	/**
	 * @param alreadyActivated
	 *            the alreadyActivated to set
	 */
	public void setAlreadyActivated(String alreadyActivated) {
		this.alreadyActivated = alreadyActivated;
	}

	/**
	 * @return the contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @param contextPath
	 *            the contextPath to set
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * @return the changePassword
	 */
	public String getChangePassword() {
		return changePassword;
	}

	/**
	 * @return the disabled
	 */
	public String getDisabled() {
		return disabled;
	}

	/**
	 * @return the expired
	 */
	public String getExpired() {
		return expired;
	}

	/**
	 * @return the forgetPassword
	 */
	public String getForgetPassword() {
		return forgetPassword;
	}

	/**
	 * @return the invalidOldPassword
	 */
	public String getInvalidOldPassword() {
		return invalidOldPassword;
	}

	/**
	 * @return the invalidToken
	 */
	public String getInvalidToken() {
		return invalidToken;
	}

	/**
	 * @return the invalidUser
	 */
	public String getInvalidUser() {
		return invalidUser;
	}

	/**
	 * @return the regError
	 */
	public String getRegError() {
		return regError;
	}

	/**
	 * @return the regSucc
	 */
	public String getRegSucc() {
		return regSucc;
	}

	/**
	 * @return the resendToken
	 */
	public String getResendToken() {
		return resendToken;
	}

	/**
	 * @return the resetPassword
	 */
	public String getResetPassword() {
		return resetPassword;
	}

	/**
	 * @return the resetPasswordEmail
	 */
	public String getResetPasswordEmail() {
		return resetPasswordEmail;
	}

	/**
	 * @return the resetPasswordSuc
	 */
	public String getResetPasswordSuc() {
		return resetPasswordSuc;
	}

	/**
	 * @return the resetYourPassword
	 */
	public String getResetYourPassword() {
		return resetYourPassword;
	}

	/**
	 * @return the updatePasswordSuc
	 */
	public String getUpdatePasswordSuc() {
		return updatePasswordSuc;
	}

	/**
	 * @return the userNotFound
	 */
	public String getUserNotFound() {
		return userNotFound;
	}

	/**
	 * @param changePassword
	 *            the changePassword to set
	 */
	public void setChangePassword(String changePassword) {
		this.changePassword = changePassword;
	}

	/**
	 * @param disabled
	 *            the disabled to set
	 */
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	/**
	 * @param expired
	 *            the expired to set
	 */
	public void setExpired(String expired) {
		this.expired = expired;
	}

	/**
	 * @param forgetPassword
	 *            the forgetPassword to set
	 */
	public void setForgetPassword(String forgetPassword) {
		this.forgetPassword = forgetPassword;
	}

	/**
	 * @param invalidOldPassword
	 *            the invalidOldPassword to set
	 */
	public void setInvalidOldPassword(String invalidOldPassword) {
		this.invalidOldPassword = invalidOldPassword;
	}

	/**
	 * @param invalidToken
	 *            the invalidToken to set
	 */
	public void setInvalidToken(String invalidToken) {
		this.invalidToken = invalidToken;
	}

	/**
	 * @param invalidUser
	 *            the invalidUser to set
	 */
	public void setInvalidUser(String invalidUser) {
		this.invalidUser = invalidUser;
	}

	/**
	 * @param regError
	 *            the regError to set
	 */
	public void setRegError(String regError) {
		this.regError = regError;
	}

	/**
	 * @param regSucc
	 *            the regSucc to set
	 */
	public void setRegSucc(String regSucc) {
		this.regSucc = regSucc;
	}

	/**
	 * @param resendToken
	 *            the resendToken to set
	 */
	public void setResendToken(String resendToken) {
		this.resendToken = resendToken;
	}

	/**
	 * @param resetPassword
	 *            the resetPassword to set
	 */
	public void setResetPassword(String resetPassword) {
		this.resetPassword = resetPassword;
	}

	/**
	 * @param resetPasswordEmail
	 *            the resetPasswordEmail to set
	 */
	public void setResetPasswordEmail(String resetPasswordEmail) {
		this.resetPasswordEmail = resetPasswordEmail;
	}

	/**
	 * @param resetPasswordSuc
	 *            the resetPasswordSuc to set
	 */
	public void setResetPasswordSuc(String resetPasswordSuc) {
		this.resetPasswordSuc = resetPasswordSuc;
	}

	/**
	 * @param resetYourPassword
	 *            the resetYourPassword to set
	 */
	public void setResetYourPassword(String resetYourPassword) {
		this.resetYourPassword = resetYourPassword;
	}

	/**
	 * @param updatePasswordSuc
	 *            the updatePasswordSuc to set
	 */
	public void setUpdatePasswordSuc(String updatePasswordSuc) {
		this.updatePasswordSuc = updatePasswordSuc;
	}

	/**
	 * @param userNotFound
	 *            the userNotFound to set
	 */
	public void setUserNotFound(String userNotFound) {
		this.userNotFound = userNotFound;
	}
}
