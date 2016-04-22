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

package com.mindfire.bicyclesharing.service;

import java.util.Date;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * EmailService class contains methods for email related operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	/**
	 * This method is used to send mails for various operations using java mail.
	 * 
	 * @param recipientName
	 *            the user's name
	 * @param recipientEmail
	 *            the user's email
	 * @param locale
	 *            the current locale
	 * @param subject
	 *            the subject of the mail
	 * @param confirmationUrl
	 *            the link to be sent with the mail
	 * @param template
	 *            the mail template
	 * @throws Exception
	 *             messaging exception
	 */
	public void sendSimpleMail(String recipientName, String recipientEmail, Locale locale, String subject,
			String confirmationUrl, String template) throws Exception {
		// Prepare the evaluation context
		final Context ctx = new Context(locale);

		ctx.setVariable("name", recipientName);
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("confirmationUrl", confirmationUrl);

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

		try {
			message.setSubject(subject);
			message.setFrom("bicyclerentaljava@gmail.com");
			message.setTo(recipientEmail);

			// Create the HTML body using Thymeleaf
			final String htmlContent = this.templateEngine.process(template, ctx);
			message.setText(htmlContent, true /* isHtml */);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.mailSender.send(mimeMessage);
	}
}
