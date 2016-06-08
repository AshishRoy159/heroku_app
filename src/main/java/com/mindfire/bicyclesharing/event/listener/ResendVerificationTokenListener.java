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

import com.mindfire.bicyclesharing.event.ResendVerificationTokenEvent;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.VerificationToken;
import com.mindfire.bicyclesharing.service.EmailService;

/**
 * ResendVerificationTokenListener is an event listener for
 * ResendVerificationTokenEvent event class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class ResendVerificationTokenListener implements ApplicationListener<ResendVerificationTokenEvent> {

	@Autowired
	private EmailService emailService;

	@Autowired
	private HttpServletRequest request;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationListener#onApplicationEvent(org.
	 * springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ResendVerificationTokenEvent event) {
		try {
			this.resendVerificationToken(event);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This method is used for creating verification token, construct and send
	 * the verification email
	 * 
	 * @param event
	 *            ResendVerificationTokenEvent
	 * @throws Exception
	 */
	private void resendVerificationToken(final ResendVerificationTokenEvent event) throws Exception {
		String appUrl = request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf("/"));
		final Locale locale = event.getLocale();
		final VerificationToken token = event.getNewToken();
		final User user = event.getUser();
		final String recipientAddress = user.getEmail();
		final String subject = "Registration Confirmation";
		final String confirmationUrl = appUrl + "/registrationConfirm.html?token=" + token.getToken();
		final String template = "/mail/resendVerificationMail";

		emailService.sendSimpleMail(user.getFirstName(), recipientAddress, locale, subject, confirmationUrl, template);
	}
}
