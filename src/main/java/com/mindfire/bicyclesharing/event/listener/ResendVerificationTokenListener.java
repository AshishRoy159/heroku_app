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
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.component.MessageBean;
import com.mindfire.bicyclesharing.constant.Constant;
import com.mindfire.bicyclesharing.event.ResendVerificationTokenEvent;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.VerificationToken;

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
	private MessageBean messageBean;

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
	 * @throws MessagingException
	 */
	private void resendVerificationToken(final ResendVerificationTokenEvent event) throws MessagingException {
		final String appUrl = event.getAppUrl();
		final Locale locale = event.getLocale();
		final VerificationToken token = event.getNewToken();
		final User user = event.getUser();

		final Message email = constructResendVerificationTokenEmail(appUrl, locale, token, user);
		Transport.send(email);
	}

	/**
	 * This method constructs the verification email message
	 * 
	 * @param contextPath
	 *            the context path
	 * @param locale
	 *            to tailor information for the user
	 * @param newToken
	 *            new token generated
	 * @param user
	 *            User object
	 * @return Message object
	 */
	private Message constructResendVerificationTokenEmail(String contextPath, Locale locale, VerificationToken newToken,
			User user) {
		final String recipientAddress = user.getEmail();
		final String subject = "Registration Confirmation";
		final String confirmationUrl = Constant.CONTEXT_ROOT + "/registrationConfirm.html?token=" + newToken.getToken();
		final String message = messageBean.getResendToken();
		final String msg = message + "\r\n" + "<a href='" + confirmationUrl + "'>Click Here</a>";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constant.MAIL_USERNAME, Constant.MAIL_PASSWORD);
			}
		});

		final Message email = new MimeMessage(session);
		try {
			email.setFrom(new InternetAddress(Constant.MAIL_USERNAME));
			email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress));
			email.setSubject(subject);
			email.setContent(msg, "text/html; charset=utf-8");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return email;
	}
}
