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

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.constant.CustomLoggerConstant;
import com.mindfire.bicyclesharing.constant.ModelAttributeConstant;
import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.dto.UserDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
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

	Logger logger = Logger.getLogger(getClass());

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
		return new ModelAndView(ViewConstant.ADD_NEW_USER);
	}

	/**
	 * This method is used to map the payment request by the manager. Simply
	 * render the managerPayment view
	 * 
	 * @param userDTO
	 *            to receive the incoming data
	 * @param result
	 *            for validating incoming data
	 * @param session
	 *            the current session
	 * @param redirectAttributes
	 *            to map model attributes
	 * @return managerPayment view
	 */
	@RequestMapping(value = { "/manager/managerPayment" }, method = RequestMethod.POST)
	public ModelAndView getPayment(@Valid @ModelAttribute("userData") UserDTO userDTO, BindingResult result,
			HttpSession session, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
					"Invalid User Data ! Try Again.");
			return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.ADD_NEW_USER);
		}

		try {
			userService.saveUserDocument(userDTO);
		} catch (IOException e) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.PAYLOAD_TOO_LARGE);
		}
		session.setAttribute("userDTO", userDTO);
		return new ModelAndView(ViewConstant.MANAGER_PAYMENT);
	}
}
