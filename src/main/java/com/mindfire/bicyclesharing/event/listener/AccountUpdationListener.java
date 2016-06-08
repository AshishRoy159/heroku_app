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

package com.mindfire.bicyclesharing.event.listener;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.event.AccountUpdationEvent;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.service.EmailService;

/**
 * RegistrationListener is an event listener for OnRegistrationCompleteEvent
 * event class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class AccountUpdationListener implements ApplicationListener<AccountUpdationEvent> {

	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private EmailService emailService;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationListener#onApplicationEvent(org.
	 * springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(AccountUpdationEvent event) {
		try {
			this.confirmUpdate(event);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This method is used for creating verification token, construct and send
	 * the verification email
	 * 
	 * @param event
	 *            OnRegistrationCompleteEvent
	 * @throws Exception
	 */
	private void confirmUpdate(final AccountUpdationEvent event) throws Exception {

		final User user = event.getUser();
		Locale locale = request.getLocale();
		final String recipientAddress = user.getEmail();
		final String subject = "Account Credentials Updated";
		String confirmationUrl;
		if(event.getStatus() == "approved"){
			confirmationUrl = "Approved";
		} else if(event.getStatus() == "disapproved") {
			confirmationUrl = "Disapproved";
		} else if(event.getStatus() == "enabled") {
			confirmationUrl = "Enabled";
		} else {
			confirmationUrl = "Disabled";
		}
		
		final String template = "/mail/activationAndApproval";

		emailService.sendSimpleMail(user.getFirstName(), recipientAddress, locale, subject, confirmationUrl, template);
	}
}
