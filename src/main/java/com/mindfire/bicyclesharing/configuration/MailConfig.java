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

package com.mindfire.bicyclesharing.configuration;

import java.io.IOException;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * MailConfiguration class is for getting the configurations for the mail
 * services
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Configuration
public class MailConfig {

	private static final String PROPERTIES_FILE = "configuration.properties";
	private static final String JAVA_MAIL_FILE = "javamail.properties";

	private static final String HOST = "mail.server.host";
	private static final String PORT = "mail.server.port";
	private static final String PROTOCOL = "mail.server.protocol";
	private static final String USERNAME = "mail.server.username";
	private static final String PASSWORD = "mail.server.password";

	/**
	 * This class is used to set the mail properties for java mail services
	 * 
	 * @return {@link JavaMailSender} object
	 * @throws IOException
	 *             may occur while reading from properties file
	 */
	@Bean
	public JavaMailSender mailSender() throws IOException {
		Properties properties = configProperties();
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(properties.getProperty(HOST));
		mailSender.setPort(Integer.parseInt(properties.getProperty(PORT)));
		mailSender.setProtocol(properties.getProperty(PROTOCOL));
		mailSender.setUsername(properties.getProperty(USERNAME));
		mailSender.setPassword(properties.getProperty(PASSWORD));
		mailSender.setJavaMailProperties(javaMailProperties());
		return mailSender;
	}

	/**
	 * This method is used to get the properties from configuration.properties
	 * file
	 * 
	 * @return {@link Properties} object
	 * @throws IOException
	 *             may occur while reading from properties file
	 */
	private Properties configProperties() throws IOException {
		Properties properties = new Properties();
		properties.load(new ClassPathResource(PROPERTIES_FILE).getInputStream());
		return properties;
	}

	/**
	 * This method is used to get the properties from javamail.properties file
	 * 
	 * @return {@link Properties} object
	 * @throws IOException
	 *             may occur while reading from properties file
	 */
	private Properties javaMailProperties() throws IOException {
		Properties properties = new Properties();
		properties.load(new ClassPathResource(JAVA_MAIL_FILE).getInputStream());
		return properties;
	}
}
