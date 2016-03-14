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

package com.mindfire.bicyclesharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

/**
 * Indicates a configuration class that declares one or more @Bean methods and
 * also triggers auto-configuration and component scanning. This is a
 * convenience annotation that is equivalent to
 * declaring @Configuration, @EnableAutoConfiguration and @ComponentScan.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@SpringBootApplication
public class BicycleSharingApplication {

	/**
	 * This is the main method of the Spring Boot Application. Execution starts
	 * here.
	 * 
	 * @see SpringApplication
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(BicycleSharingApplication.class, args);
	}

	/**
	 * This method maps all frequently encountered error pages and render the
	 * views.
	 * 
	 * @return error page views
	 */
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return (container -> {
			ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

			container.addErrorPages(error401Page, error404Page, error500Page);
		});
	}
}
