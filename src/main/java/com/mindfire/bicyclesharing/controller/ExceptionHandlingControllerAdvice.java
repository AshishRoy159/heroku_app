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

package com.mindfire.bicyclesharing.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.exception.CustomException;

/**
 * ExceptionHandlingControllerAdvice contains methods to handle exceptions
 * thrown by controllers.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@ControllerAdvice
public class ExceptionHandlingControllerAdvice {

	private Logger logger;

	/**
	 * This is the default constructor
	 */
	public ExceptionHandlingControllerAdvice() {
		logger = Logger.getLogger(getClass());
	}

	/**
	 * This method is used to handle the CustomExceptions
	 * 
	 * @param request
	 *            the {@link HttpServletRequest} object
	 * @param exception
	 *            the {@link Exception} object
	 * @return errorPage view
	 */
	@ExceptionHandler(CustomException.class)
	public ModelAndView handleNotFoundError(HttpServletRequest request, Exception exception) {
		logger.info("Not Found Exception Occurred");
		return showErrorPage(exception);
	}

	/**
	 * This method is used to redirect to error view if an exception occurs.
	 * 
	 * @param exception
	 *            the {@link Exception} object
	 * @return errorPage view
	 */
	private ModelAndView showErrorPage(Exception exception) {
		ModelAndView mav = new ModelAndView();

		mav.addObject("cause", exception.getCause());
		mav.addObject("message", exception.getMessage());
		mav.addObject("printStackTrace", exception.fillInStackTrace());
		mav.setViewName("errorPage");
		return mav;
	}
}
