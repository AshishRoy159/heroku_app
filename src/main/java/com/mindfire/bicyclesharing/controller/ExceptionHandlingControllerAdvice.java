
package com.mindfire.bicyclesharing.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.exception.CustomException;

@ControllerAdvice
public class ExceptionHandlingControllerAdvice {

	private Logger logger;

	public ExceptionHandlingControllerAdvice() {
		logger = Logger.getLogger(getClass());
	}

	@ExceptionHandler(CustomException.class)
	public ModelAndView handleNotFoundError(HttpServletRequest request, Exception exception) {
		logger.info("Not Found Exception Occurred");
		return showErrorPage(exception);
	}
		
	private ModelAndView showErrorPage(Exception exception){
		ModelAndView mav = new ModelAndView();
		mav.addObject("cause", exception.getCause());
		mav.addObject("message", exception.getMessage());
		mav.addObject("printStackTrace", exception.fillInStackTrace());

		mav.setViewName("errorPage");
		return mav;
	}
}
