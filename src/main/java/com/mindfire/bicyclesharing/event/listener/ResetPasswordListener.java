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

import java.util.Properties;
import java.util.UUID;

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
import com.mindfire.bicyclesharing.event.OnResetPasswordEvent;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.service.UserService;

/**
 * RegistrationListener is an event listener for OnRegistrationCompleteEvent
 * event class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

	@Autowired
	private UserService service;

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
	public void onApplicationEvent(OnResetPasswordEvent event) {
		try {
			this.confirmRegistration(event);
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This method is used for creating verification token, construct and send
	 * the verification email
	 * 
	 * @param event
	 *            OnResetPasswordEvent
	 * @throws MessagingException
	 */
	private void confirmRegistration(final OnResetPasswordEvent event) throws MessagingException {
		final User user = event.getUser();
		final String token = UUID.randomUUID().toString();
		service.createResetPasswordTokenForUser(user, token);

		final Message email = constructEmailMessage(event, user, token);
		Transport.send(email);
	}

	/**
	 * This method constructs the verification email message
	 * 
	 * @param event
	 *            OnResetPasswordEvent
	 * @param user
	 *            User object
	 * @param token
	 *            generated token
	 * @return Message object
	 */
	private final Message constructEmailMessage(final OnResetPasswordEvent event, final User user, final String token) {
		final String recipientAddress = user.getEmail();
		final String subject = "ResetPassword";
		final String confirmationUrl = Constant.CONTEXT_ROOT + "/resetPassword.html?token=" + token;
		final String message = messageBean.getResetPassword();
		final String msg = message + "\r\n" + "<a href='" + confirmationUrl + "'>Click Here</a>";

		System.out.println(confirmationUrl);

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
