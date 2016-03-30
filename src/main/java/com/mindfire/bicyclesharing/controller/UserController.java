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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.component.MessageBean;
import com.mindfire.bicyclesharing.component.UserComponent;
import com.mindfire.bicyclesharing.constant.Constant;
import com.mindfire.bicyclesharing.dto.ChangePasswordDTO;
import com.mindfire.bicyclesharing.dto.ForgotPasswordDTO;
import com.mindfire.bicyclesharing.dto.RegistrationPaymentDTO;
import com.mindfire.bicyclesharing.dto.SetPasswordDTO;
import com.mindfire.bicyclesharing.dto.UserDTO;
import com.mindfire.bicyclesharing.event.OnRegistrationCompleteEvent;
import com.mindfire.bicyclesharing.event.OnResetPasswordEvent;
import com.mindfire.bicyclesharing.event.ResendVerificationTokenEvent;
import com.mindfire.bicyclesharing.model.PasswordResetToken;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.VerificationToken;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.PasswordResetTokenRepository;
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
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private MessageBean messageBean;

	/**
	 * This method maps the registration request. Simply render the
	 * successRegister view.
	 * 
	 * @param regPaymentDTO
	 *            to receive the incoming data
	 * @param result
	 *            to validate the incoming data
	 * @param session
	 *            the current session
	 * @param request
	 *            to access general request meta data
	 * @return successRegister view in case of successful registration else
	 *         failure view
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ModelAndView addUser(@Valid @ModelAttribute("paymentData") RegistrationPaymentDTO regPaymentDTO,
			BindingResult result, HttpSession session, WebRequest request) {
		if (result.hasErrors()) {
			return new ModelAndView("payment", "errorMessage", "Invalid Payment data");
		}

		UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
		WalletTransaction transaction = userComponent.mapUserComponent(userDTO, regPaymentDTO);

		if (transaction != null) {
			User registered = transaction.getWallet().getUser();
			try {
				String appUrl = System.getProperty("server.context-path");
				eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
			} catch (Exception me) {
				System.out.println(me.getMessage());
			}
			return new ModelAndView("successRegister", "user", userDTO);
		} else {
			return new ModelAndView("failure");
		}
	}

	/**
	 * This method maps the registration confirmation from the user's email.
	 * Simply render the setPassword view.
	 * 
	 * @param model
	 *            to map model attributes
	 * @param token
	 *            to validate the request
	 * @return ModelAndView object, setPassword view in case of valid requests
	 *         else badUser view
	 */
	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public ModelAndView confirmRegistration(Model model, @RequestParam("token") String token) {
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
	 * This method is used for mapping the request when the user request to
	 * resend the verification mail
	 * 
	 * @param request
	 *            for request information
	 * @param existingToken
	 *            the expired token
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
	 * This method is used to map the set password request. Simply render the
	 * setPassword view
	 * 
	 * @return the setPassword view
	 */
	@RequestMapping(value = "setPassword", method = RequestMethod.GET)
	public ModelAndView returnSetPasswordPage() {
		return new ModelAndView("setPassword");
	}

	/**
	 * This method maps the request after the user sets the password. Simply
	 * render the userProfile view.
	 * 
	 * @param setPasswordDTO
	 *            to receive the incoming data
	 * @param model
	 *            to map model attributes
	 * @param result
	 *            to validate incoming data
	 * @return the signIn view
	 */
	@RequestMapping(value = "/afterSetPassword", method = RequestMethod.POST)
	public ModelAndView setPassword(@ModelAttribute("setPasswordData") @Valid SetPasswordDTO setPasswordDTO,
			Model model, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("setPassword", "errorMessage", "Invalid password format");
		}

		int num = userComponent.mapPassword(setPasswordDTO.getPassword(), setPasswordDTO.getEmail());

		if (num == 0) {
			return new ModelAndView("setPassword");
		} else {
			return new ModelAndView("redirect:/logout");
		}
	}

	/**
	 * This method maps the request after the user clicks on forgot password
	 * link. Simply render the forgotPassword view.
	 * 
	 * @return forgotPassword view.
	 */
	@RequestMapping(value = "forgotPassword", method = RequestMethod.GET)
	public ModelAndView forgotPassword() {
		return new ModelAndView("forgotPassword");
	}

	/**
	 * This method takes the email id and send the link for set the new
	 * password.
	 * 
	 * @param forgotPasswordDTO
	 *            to receive incoming data
	 * @param request
	 *            to access general request meta data
	 * @return successRegister view
	 */
	@RequestMapping(value = "afterForgotPassword", method = RequestMethod.POST)
	public ModelAndView forgotOldPassword(
			@Valid @ModelAttribute("forgotPasswordData") ForgotPasswordDTO forgotPasswordDTO, WebRequest request,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("forgotPassword", "errorMessage", "Invalid Email");
		}

		User user = userComponent.retrieveUserPassword(forgotPasswordDTO);

		try {
			String appUrl = System.getProperty("server.context-path");
			eventPublisher.publishEvent(new OnResetPasswordEvent(user, request.getLocale(), appUrl));
		} catch (Exception me) {
			System.out.println(me.getMessage());
		}

		return new ModelAndView("successRegister");
	}

	/**
	 * This method maps the reset password from the user's email. Simply render
	 * the setPassword view.
	 * 
	 * @param model
	 *            to map model attributes
	 * @param token
	 *            to validate the request
	 * @return ModelAndView object, setPassword view in case of valid requests
	 *         else badUser view
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public ModelAndView resetPassword(Model model, @RequestParam("token") String token) {
		System.out.println(token);
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

		if (passwordResetToken == null) {
			String message = messageBean.getInvalidToken();
			model.addAttribute("message", message);
			return new ModelAndView("badUser");
		}

		User user = passwordResetToken.getUser();
		if (!user.getEnabled()) {
			String message = messageBean.getDisabled();
			model.addAttribute("message", message);
			return new ModelAndView("badUser");
		} else {
			Calendar cal = Calendar.getInstance();
			if ((passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
				model.addAttribute("message", messageBean.getExpired());
				model.addAttribute("expired", true);
				model.addAttribute("token", passwordResetToken);
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
	 * This method is used for check authentication and map the request for
	 * change password and simply render the changePassword view.
	 * 
	 * @return changePassword view.
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = { "/user/changePassword" }, method = RequestMethod.GET)
	public String changePassword() {
		return Constant.CHANGE_PASSWORD;
	}

	/**
	 * This method is used for taking data from the changePassword view and send
	 * to the component for update the password.
	 * 
	 * @param authentication
	 *            token for an authentication request or for an authenticated
	 *            principal
	 * @param changePasswordDTO
	 *            to receive the incoming data
	 * @return signIn view
	 */
	@RequestMapping(value = "user/afterChangePassword", method = RequestMethod.POST)
	public String afterChangePassword(Authentication authentication,
			@Valid @ModelAttribute("changePasswordData") ChangePasswordDTO changePasswordDTO, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("invalidPassword",
					"Password must be 4 to 16 characters long without any spaces");
			return "redirect:changePassword";
		}

		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

		if (passEncoder.matches(changePasswordDTO.getOldPassword(), currentUser.getUser().getPassword())) {
			if (userComponent.mapPassword(changePasswordDTO.getNewPassword(), currentUser.getUser().getEmail()) != 0) {
				return "redirect:/logout";
			}
		} else {
			redirectAttributes.addFlashAttribute("invalidPassword", "Incorrect Old Password");
		}

		return "redirect:" + Constant.CHANGE_PASSWORD;
	}

	/**
	 * This method maps the request for user profile view
	 * 
	 * @param authentication
	 *            token for an authentication request or for an authenticated
	 *            principal
	 * @param model
	 *            to map the model attributes
	 * @param id
	 *            the id of the user whose details is to be shown
	 * @return userProfile view
	 */
	@PostAuthorize("@currentUserService.canAccessUser(principal, #id)")
	@RequestMapping(value = { "/user/userProfile/{id}" })
	public ModelAndView userProfile(Authentication authentication, Model model, @PathVariable Long id) {

		User userDetails = userService.userDetails(id);
		model.addAttribute("user", userDetails);

		return new ModelAndView("userProfile");
	}

	/**
	 * This method map the request for view the update user details.
	 * 
	 * @param model
	 *            to map model attributes
	 * @param id
	 *            the id of the user whose details is to be shown
	 * @return updateUserDetails view
	 */
	@PostAuthorize("@currentUserService.canAccessUser(principal, #id)")
	@RequestMapping(value = { "/user/updateUserDetails/{id}" }, method = RequestMethod.GET)
	public ModelAndView updateUserDetails(Model model, @PathVariable("id") Long id) {
		User user = userService.userDetails(id);
		model.addAttribute("user", user);

		return new ModelAndView("updateUserDetails");
	}

	/**
	 * This method receives data from the updateUserDetail view and send the
	 * data to the UserComponent class for updating the user data.
	 * 
	 * @param userDTO
	 *            to receive the incoming data
	 * @param id
	 *            the id of the user whose details is to be shown
	 * @param model
	 *            to map model attributes
	 * @return userProfile view in case of successful updation else
	 *         updateUserDetails view
	 */
	@PostAuthorize("@currentUserService.canAccessUser(principal, #id)")
	@RequestMapping(value = { "user/updateUserDetails/{id}" }, method = RequestMethod.POST)
	public ModelAndView afterUpdateUserDetails(@Valid @ModelAttribute("userDetailData") UserDTO userDTO,
			@PathVariable("id") Long id, Model model, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("updateUserDetails", "errorMessage", "Invalid data. Updation failed.");
		}

		int success = userComponent.mapUpdateUserDetail(userDTO);
		User userDetails = userService.userDetails(id);
		model.addAttribute("user", userDetails);

		if (success == 0) {
			return new ModelAndView("updateUserDetails", "errorMessage", "Invalid data. Updation failed.");
		} else {
			return new ModelAndView("userProfile");
		}
	}

	/**
	 * This method is used to map payment request
	 * 
	 * @param userDTO
	 *            to receive the incoming data
	 * @param session
	 *            the current session
	 * @return payment view
	 */
	@RequestMapping(value = "payment", method = RequestMethod.POST)
	public ModelAndView getPayment(@Valid @ModelAttribute("userData") UserDTO userDTO, HttpSession session,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("registration", "errorMessage", "Invalid User data");
		}

		session.setAttribute("userDTO", userDTO);
		return new ModelAndView("payment");
	}

	/**
	 * This method maps the request for fetching all users detail.
	 * 
	 * @return searchUsers view
	 */
	@RequestMapping(value = { "admin/userList", "manager/userList" })
	public ModelAndView userList() {
		return new ModelAndView("searchUsers", "usersList", userService.getAllUsers());
	}
}
