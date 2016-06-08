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

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.security.CurrentUser;
import com.mindfire.bicyclesharing.service.PickUpPointManagerService;
import com.mindfire.bicyclesharing.service.PickUpPointService;

/**
 * This class contains all the Request Mappings related to the navigation of the
 * user's front-end.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class HomeController {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private PickUpPointService pickUpPointService;

	@Autowired
	private PickUpPointManagerService pickUpPointManagerService;

	/**
	 * This method maps all root request and find all pickup points and Simply
	 * render the index view along with the pickup point list.
	 * 
	 * @param model
	 *            to map model attributes
	 * @return the index view.
	 */
	@RequestMapping(value = { "/", "index" })
	public ModelAndView getHomePage(Model model) {
		model.addAttribute("pickUpPoints", pickUpPointService.getAllActivePickupPoints(true));
		return new ModelAndView(ViewConstant.INDEX);
	}

	/**
	 * This method maps register request. Simply render the registration view.
	 * 
	 * @return the registration view.
	 */
	@RequestMapping(value = { "register" }, method = RequestMethod.GET)
	public String getUserCreatePage() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			/* The user is logged in */
			return ViewConstant.REDIRECT + ViewConstant.INDEX;
		} else {
			return ViewConstant.REGISTRATION;
		}
	}

	/**
	 * This method maps login request. Simply render the signIn view.
	 * 
	 * @param error
	 *            to catch login errors
	 * @param logout
	 *            to show messages on logout
	 * @param model
	 *            to map model attributes
	 * @param authentication
	 *            to get the current user details
	 * @return the signIn view.
	 */
	@RequestMapping(value = { "login" }, method = RequestMethod.GET)
	public ModelAndView getUserSignInPage(@RequestParam("error") Optional<String> error,
			@RequestParam Optional<String> logout, Model model, Authentication authentication) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			logger.info("User already logged in. Redirected to index page.");
			/* The user is logged in */
			return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.INDEX);
		} else {
			if (error.isPresent()) {
				logger.info("Error while trying to log in.");

				if (error.get().equals("badUser")) {
					logger.info("Invalid user credentials.");
					model.addAttribute("loginError", "Invalid Email or Password");
				} else if (error.get().equals("disabled")) {
					logger.info("This user is not enabled.");
					model.addAttribute("loginError", "Your Account is Disabled. Please contact nearest Pickup Point.");
				}
			}

			if (logout.isPresent()) {
				logger.info("User successfully logged out.");
				model.addAttribute("loginError", "You have successfully logged out!!");
			}
			return new ModelAndView(ViewConstant.SIGN_IN, "error", error);
		}
	}

	/**
	 * This method maps any request which is not authorized to the user. Simply
	 * render the Access Denied view.
	 * 
	 * @return 403 view
	 */
	@RequestMapping(value = { "403" })
	public String getAccessDeniedPage() {
		return "403";
	}

	/**
	 * This method maps request for admin home page and retrieve all booking
	 * where booking status is false and pickup point manager details.
	 * 
	 * @param model
	 *            to map model attributes
	 * @param authentication
	 *            this is used for retrieving current user details.
	 * @return adminHome view
	 */
	@RequestMapping(value = { "admin", "admin/adminHome", "manager", "manager/managerHome" })
	public ModelAndView adminHome(Model model, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

		if (currentUser.getUserRole().equals("MANAGER")) {
			logger.info("The logged in user is a manager.");
			pickUpPointManagerService.openPickUpPoint(currentUser.getUser());
		}
		model.addAttribute("pickUpPointManagers", pickUpPointManagerService.getAllPickUpPointManager());
		return new ModelAndView(ViewConstant.ADMIN_HOME);
	}

	/**
	 * This method is used for closing the pickup point.
	 * 
	 * @param authentication
	 *            this is used for retrieving current user details.
	 * @return signIn view
	 */
	@RequestMapping(value = "managerLogout", method = RequestMethod.GET)
	public ModelAndView managerLogout(Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		pickUpPointManagerService.closePickUpPoint(currentUser.getUser());
		return new ModelAndView("redirect:/logout");
	}
}
