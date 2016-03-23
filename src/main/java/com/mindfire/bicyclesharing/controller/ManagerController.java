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

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.dto.UserDTO;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.service.UserService;

/**
 * ManagerController contains all the mappings related to the manager.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class ManagerController {

	@Autowired
	private UserService userService;

	/**
	 * This method is used to map the add new user request by the manager.
	 * Simply render the addNewUser view
	 * 
	 * @return addNewUser View
	 */
	@RequestMapping(value = { "/manager/addNewUser" }, method = RequestMethod.GET)
	public ModelAndView addNewUser() {
		return new ModelAndView("addNewUser");
	}

	/**
	 * This method is used to map the payment request by the manager. Simply
	 * render the managerPayment view
	 * 
	 * @param userDTO
	 *            to receive the incoming data
	 * @param session
	 *            the current session
	 * @return managerPayment view
	 */
	@RequestMapping(value = { "/manager/managerPayment" }, method = RequestMethod.POST)
	public ModelAndView getPayment(@ModelAttribute("userData") UserDTO userDTO, HttpSession session) {
		session.setAttribute("userDTO", userDTO);
		return new ModelAndView("managerPayment");
	}

	/**
	 * This method is used to map the booking of the bicycles. Simply render the
	 * booking view
	 * 
	 * @param model
	 *            to map the model attributes
	 * @param id
	 *            userId of the respective manager
	 * @return booking view
	 */
	@RequestMapping(value = { "/manager/booking/{id}" }, method = RequestMethod.GET)
	public ModelAndView getBookingView(Model model, @PathVariable("id") Long id) {
		User userDetails = userService.userDetails(id);
		model.addAttribute("user", userDetails);
		return new ModelAndView("booking");
	}

	/**
	 * This method is used to map the bicycle transfer request. Simply render
	 * the bicycleTransfer view.
	 * 
	 * @return bicycleTransfer view
	 */
	@RequestMapping(value = { "/manager/bicycleTransfer" }, method = RequestMethod.GET)
	public ModelAndView getTransfersView() {
		return new ModelAndView("bicycleTransfer");
	}

}
