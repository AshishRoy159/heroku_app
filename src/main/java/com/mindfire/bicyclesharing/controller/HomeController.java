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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.service.BookingService;
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

	@Autowired
	private PickUpPointService pickUpPointService;

	@Autowired
	private PickUpPointManagerService pickUpPointManagerService;

	@Autowired
	private BookingService bookingService;

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
		return new ModelAndView("index");
	}

	/**
	 * This method maps register request. Simply render the registration view.
	 * 
	 * @return the registration view.
	 */
	@RequestMapping(value = { "register" }, method = RequestMethod.GET)
	public String getUserCreatePage() {
		return "registration";
	}

	/**
	 * This method maps login request. Simply render the signIn view.
	 * 
	 * @param error
	 *            to catch login errors
	 * @return the signIn view.
	 */
	@PostAuthorize("isAnonymous()")
	@RequestMapping(value = { "login" }, method = RequestMethod.GET)
	public ModelAndView getUserSignInPage(@RequestParam Optional<String> error) {
		return new ModelAndView("signIn", "error", error);
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
	 * @return adminHome view
	 */
	@RequestMapping(value = { "admin", "admin/adminHome", "manager/managerHome" })
	public ModelAndView adminHome(Model model) {
		model.addAttribute("bookings", bookingService.getAllBookingDetails(true));
		model.addAttribute("pickUpPointManagers", pickUpPointManagerService.getAllPickUpPointManager());
		return new ModelAndView("adminHome");
	}
}
