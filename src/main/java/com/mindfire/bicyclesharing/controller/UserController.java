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

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.component.MessageBean;
import com.mindfire.bicyclesharing.component.UserComponent;
import com.mindfire.bicyclesharing.constant.Constant;
import com.mindfire.bicyclesharing.dto.SetPasswordDTO;
import com.mindfire.bicyclesharing.dto.UserDTO;
import com.mindfire.bicyclesharing.event.OnRegistrationCompleteEvent;
import com.mindfire.bicyclesharing.event.ResendVerificationTokenEvent;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.VerificationToken;
import com.mindfire.bicyclesharing.repository.VerificationTokenRepository;
import com.mindfire.bicyclesharing.service.UserService;
/**
 * UserController contains all the mappings related to the users
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class UserController {

	@Autowired
	private UserComponent userComponent;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private UserService userService;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private MessageBean messageBean;

	/**
	 * This method maps the registration request. Simply render the registration view.
	 * 
	 * @return the registration view.
	 */
	@RequestMapping("registration.html")
	public String register(@ModelAttribute("userData") UserDTO userDTO, BindingResult result) {
		return Constant.REGISTRATION;
	}

	/**
	 * This method maps the registration request. Simply render the successRegister view.
	 * 
	 * @return the successRegister view.
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ModelAndView addUser(@ModelAttribute("userData") UserDTO userDTO, BindingResult result, WebRequest request) {

		User registered = userComponent.mapUserComponent(userDTO);
		try {
			String appUrl = System.getProperty("server.context-path");
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
		} catch (Exception me) {
			System.out.println(me.getMessage());
		}
		return new ModelAndView("successRegister", "user", userDTO);
	}

	/**
	 * This method maps the registration confirmation from the user's email.
	 * Simply render the setPassword view.
	 * 
	 * @param model
	 * @param token
	 * @return ModelAndView object
	 */
	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public ModelAndView confirmRegistration(Model model, @RequestParam("token") String token) {
		System.out.println(token);
		VerificationToken verificationToken = tokenRepository.findByToken(token);

		if (verificationToken == null) {
			String message = messageBean.getInvalidToken();
			model.addAttribute("message", message);
			return new ModelAndView("badUser");
		}

		User user = verificationToken.getUser();
		if (user.getEnabled()) {
			String message = messageBean.getAlreadyActivated();
			model.addAttribute("message", message);
			return new ModelAndView("badUser");
		} else {
			Calendar cal = Calendar.getInstance();
			if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
				model.addAttribute("message", messageBean.getExpired());
				model.addAttribute("expired", true);
				model.addAttribute("token", verificationToken);
				return new ModelAndView("badUser");
			} else {
				user.setEnabled(true);
				userService.saveRegisteredUser(user);
				model.addAttribute("user", user);
				return new ModelAndView("setPassword");
			}
		}
	}

	/**
	 * This method is used for mapping the request when the user request to resend the verification mail
	 * 
	 * @param request
	 * @param existingToken
	 * @return the successRegister view.
	 */
	@RequestMapping(value = "resendRegistrationToken", method = RequestMethod.GET)
	public ModelAndView resendRegistrationToken(HttpServletRequest request,
			@RequestParam("token") String existingToken) {

		VerificationToken newToken = userService.generateNewVerificationToken(existingToken);

		User user = userService.getUser(newToken.getToken());
		String appUrl = messageBean.getContextPath();
		try {
			eventPublisher.publishEvent(new ResendVerificationTokenEvent(appUrl, request.getLocale(), newToken, user));
		} catch (Exception me) {
			System.out.println(me.getMessage());
		}
		return new ModelAndView("successRegister");

	}

	/**
	 * This method is used to map the set password request. Simply render the setPassword view
	 * 
	 * @param setPasswordDTO
	 * @return the setPassword view
	 */
	@RequestMapping(value = "setPassword", method = RequestMethod.GET)
	public ModelAndView returnSetPasswordPage(@ModelAttribute("setPasswordData") SetPasswordDTO setPasswordDTO) {
		return new ModelAndView("setPassword");
	}

	/**
	 * This method maps the request after the user sets the password. Simply render the userProfile view.
	 * 
	 * @param setPasswordDTO
	 * @param model
	 * @param result
	 * @return the userProfile view
	 */
	@RequestMapping(value = "afterSetPassword", method = RequestMethod.POST)
	public ModelAndView setPassword(@ModelAttribute("setPasswordData") @Valid SetPasswordDTO setPasswordDTO, Model model,
			BindingResult result) {
		int num = userComponent.mapPassword(setPasswordDTO);
		if (num == 0) {
			return new ModelAndView("setPassword");
		} else {
			User user = userService.userDetails(setPasswordDTO.getEmail());
			model.addAttribute("user", user);
			return new ModelAndView("userProfile");
		}
	}

}
