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

@Service
public class EmailService {
	
	@Autowired 
    private JavaMailSender mailSender;
    
    @Autowired 
    private TemplateEngine templateEngine;
    
	public void sendSimpleMail(String recipientName, String recipientEmail, Locale locale, String subject, String confirmationUrl) throws Exception	{
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
            final String htmlContent = this.templateEngine.process("email-simple", ctx);
            message.setText(htmlContent, true /* isHtml */);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // Send email
        this.mailSender.send(mimeMessage);
        
    }
}
