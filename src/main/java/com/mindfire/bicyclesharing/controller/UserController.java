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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
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

import com.mindfire.bicyclesharing.component.MessageBean;
import com.mindfire.bicyclesharing.constant.CustomLoggerConstant;
import com.mindfire.bicyclesharing.constant.ModelAttributeConstant;
import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.dto.ChangePasswordDTO;
import com.mindfire.bicyclesharing.dto.ForgotPasswordDTO;
import com.mindfire.bicyclesharing.dto.RegistrationPaymentDTO;
import com.mindfire.bicyclesharing.dto.SetPasswordDTO;
import com.mindfire.bicyclesharing.dto.UserDTO;
import com.mindfire.bicyclesharing.event.RegistrationCompleteEvent;
import com.mindfire.bicyclesharing.event.ResendVerificationTokenEvent;
import com.mindfire.bicyclesharing.event.ResetPasswordEvent;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.ProofDetail;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.VerificationToken;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.security.CurrentUser;
import com.mindfire.bicyclesharing.service.UserService;
import com.mindfire.bicyclesharing.service.VerificationTokenService;

/**
 * UserController contains all the mappings related to the users
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class UserController {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private UserService userService;

	@Autowired
	private VerificationTokenService verificationTokenService;

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
	 * @param model
	 *            to map the model attributes
	 * @return successRegister view in case of successful registration else
	 *         failure view
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ModelAndView addUser(@Valid @ModelAttribute("paymentData") RegistrationPaymentDTO regPaymentDTO,
			BindingResult result, HttpSession session, WebRequest request, Model model) throws ParseException {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.PAYMENT, ModelAttributeConstant.ERROR_MESSAGE, "Invalid Payment data");
		}
		UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
		Optional<User> existing = userService.getUserByEmail(userDTO.getEmail());

		if (existing.isPresent()) {
			logger.info("An user with the email id already exists. Transaction cancelled.");
			model.addAttribute("failure", "User with same email already exists!!");
			return new ModelAndView(ViewConstant.REGISTRATION);
		} else {
			logger.info("User details are valid for registration.");
			WalletTransaction transaction = userService.saveUserDetails(userDTO, regPaymentDTO);

			if (transaction != null) {
				logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
				User registered = transaction.getWallet().getUser();

				try {
					eventPublisher.publishEvent(new RegistrationCompleteEvent(registered, request.getLocale()));
				} catch (Exception me) {
					System.out.println(me.getMessage());
				}
				return new ModelAndView(ViewConstant.SUCCESS_REGISTER, ModelAttributeConstant.USER, userDTO);
			} else {
				logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
				return new ModelAndView(ViewConstant.FAILURE);
			}
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
		VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

		if (verificationToken == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		User user = verificationToken.getUser();

		if (user.getEnabled()) {
			logger.info("This user is already enabled. Transaction cancelled.");
			String message = messageBean.getAlreadyActivated();
			model.addAttribute(ModelAttributeConstant.MESSAGE, message);
			return new ModelAndView(ViewConstant.BAD_USER);
		} else {
			logger.info("User and verification token are valid.");
			Calendar cal = Calendar.getInstance();

			if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
				logger.info("Verification token is expired. Transaction cancelled.");
				model.addAttribute(ModelAttributeConstant.MESSAGE, messageBean.getExpired());
				model.addAttribute("expired", true);
				model.addAttribute("token", verificationToken);
				return new ModelAndView(ViewConstant.BAD_USER);
			} else {
				logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
				model.addAttribute(ModelAttributeConstant.USER, userService.saveRegisteredUser(user));
				return new ModelAndView(ViewConstant.SET_PASSWORD);
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
		VerificationToken newToken = verificationTokenService.generateNewVerificationToken(existingToken);
		User user = userService.getUser(newToken.getToken());

		try {
			eventPublisher.publishEvent(new ResendVerificationTokenEvent(request.getLocale(), newToken, user));
		} catch (Exception me) {
			System.out.println(me.getMessage());
		}
		return new ModelAndView(ViewConstant.SUCCESS_REGISTER);
	}

	/**
	 * This method is used to map the set password request. Simply render the
	 * setPassword view
	 * 
	 * @return the setPassword view
	 */
	@RequestMapping(value = "setPassword", method = RequestMethod.GET)
	public ModelAndView returnSetPasswordPage() {
		return new ModelAndView(ViewConstant.SET_PASSWORD);
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
	@RequestMapping(value = "/setPasswordConfirmation", method = RequestMethod.POST)
	public ModelAndView setPassword(@ModelAttribute("setPasswordData") @Valid SetPasswordDTO setPasswordDTO,
			Model model, BindingResult result) {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.SET_PASSWORD, ModelAttributeConstant.ERROR_MESSAGE,
					"Invalid password format");
		}
		int num = userService.savePassword(setPasswordDTO.getPassword(), setPasswordDTO.getEmail());

		if (num == 0) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			return new ModelAndView(ViewConstant.SET_PASSWORD);
		} else {
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
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
		return new ModelAndView(ViewConstant.FORGOT_PASSWORD);
	}

	/**
	 * This method takes the email id and send the link for set the new
	 * password.
	 * 
	 * @param forgotPasswordDTO
	 *            to receive incoming data
	 * @param request
	 *            to access general request meta data
	 * @param result
	 *            for validating incoming data
	 * @return successRegister view
	 */
	@RequestMapping(value = "forgotPasswordConfirmation", method = RequestMethod.POST)
	public ModelAndView forgotOldPassword(
			@Valid @ModelAttribute("forgotPasswordData") ForgotPasswordDTO forgotPasswordDTO, WebRequest request,
			BindingResult result) {
		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.FORGOT_PASSWORD, ModelAttributeConstant.ERROR_MESSAGE,
					"Invalid Email");
		}
		User user = userService.userDetailsByEmail(forgotPasswordDTO);

		if (null == user) {
			logger.info("Email id doesn't exist. Request for reset password denied.");
			return new ModelAndView(ViewConstant.FORGOT_PASSWORD, ModelAttributeConstant.ERROR_MESSAGE,
					"Email doesn't exist");
		}

		try {
			eventPublisher.publishEvent(new ResetPasswordEvent(user, request.getLocale()));
		} catch (Exception me) {
			System.out.println(me.getMessage());
		}
		logger.info("Request for password reset approved.");
		return new ModelAndView(ViewConstant.SUCCESS_REGISTER);
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
		VerificationToken passwordResetToken = verificationTokenService.getVerificationToken(token);

		if (passwordResetToken == null) {
			logger.error("Password reset token is 'null'. Transaction cancelled.");
			String message = messageBean.getInvalidToken();
			model.addAttribute(ModelAttributeConstant.MESSAGE, message);
			return new ModelAndView(ViewConstant.BAD_USER);
		}
		User user = passwordResetToken.getUser();

		if (!user.getEnabled()) {
			logger.info("User is not enabled. Transaction cancelled.");
			String message = messageBean.getDisabled();
			model.addAttribute(ModelAttributeConstant.MESSAGE, message);
			return new ModelAndView(ViewConstant.BAD_USER);
		} else {
			logger.info("User details are valid for password reset request.");
			Calendar cal = Calendar.getInstance();
			if ((passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
				logger.info("Password reset token is expired. Transaction cancelled.");
				model.addAttribute(ModelAttributeConstant.MESSAGE, messageBean.getExpired());
				model.addAttribute("expired", true);
				model.addAttribute("token", passwordResetToken);
				return new ModelAndView(ViewConstant.BAD_USER);
			} else {
				logger.info("User verifeid for password reset request. Redirected  to set password page.");
				model.addAttribute(ModelAttributeConstant.USER, userService.saveRegisteredUser(user));
				return new ModelAndView(ViewConstant.SET_PASSWORD);
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
		return ViewConstant.CHANGE_PASSWORD;
	}

	/**
	 * This method is used for taking data from the changePassword view and send
	 * to the component for update the password.
	 * 
	 * @param authentication
	 *            token for an authentication request or for an authenticated
	 *            principal
	 * @param result
	 *            for validating incoming data
	 * @param changePasswordDTO
	 *            to receive the incoming data
	 * @param redirectAttributes
	 *            to map model attributes
	 * @return signIn view
	 */
	@RequestMapping(value = "user/changePasswordConfirmation", method = RequestMethod.POST)
	public String afterChangePassword(Authentication authentication,
			@Valid @ModelAttribute("changePasswordData") ChangePasswordDTO changePasswordDTO, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.INVALID_PASSWORD,
					"Password must be 4 to 16 characters long without any spaces");
			return ViewConstant.REDIRECT + ViewConstant.CHANGE_PASSWORD;
		}
		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

		if (passEncoder.matches(changePasswordDTO.getOldPassword(), currentUser.getUser().getPassword())) {
			logger.info("The existing password is valid.");
			if (userService.savePassword(changePasswordDTO.getNewPassword(), currentUser.getUser().getEmail()) != 0) {
				logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
				return "redirect:/logout";
			}
		} else {
			logger.info("Invalid old password. Transaction cancelled.");
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.INVALID_PASSWORD, "Incorrect Old Password");
		}
		return ViewConstant.REDIRECT + ViewConstant.CHANGE_PASSWORD;
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
		User user = userService.userDetails(id);

		if (user == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		model.addAttribute(ModelAttributeConstant.USER, user);
		return new ModelAndView(ViewConstant.USER_PROFILE);
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

		if (user == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		model.addAttribute(ModelAttributeConstant.USER, userService.userDetails(id));
		return new ModelAndView(ViewConstant.UPDATE_USER_DETAILS);
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
	 * @param result
	 *            for validating incoming data
	 * @return userProfile view in case of successful updation else
	 *         updateUserDetails view
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	@PostAuthorize("@currentUserService.canAccessUser(principal, #id)")
	@RequestMapping(value = { "user/updateUserDetails/{id}" }, method = RequestMethod.POST)
	public ModelAndView afterUpdateUserDetails(@Valid @ModelAttribute("userDetailData") UserDTO userDTO,
			@PathVariable("id") Long id, Model model, BindingResult result) throws ParseException {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.UPDATE_USER_DETAILS, ModelAttributeConstant.ERROR_MESSAGE,
					"Invalid data. Updation failed.");
		}
		User user = userService.userDetails(id);

		if (user == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		int success = userService.updateUserDetail(userDTO);
		model.addAttribute(ModelAttributeConstant.USER, userService.userDetails(id));

		if (success == 0) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			return new ModelAndView(ViewConstant.UPDATE_USER_DETAILS, ModelAttributeConstant.ERROR_MESSAGE,
					"Invalid data. Updation failed.");
		} else {
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
			return new ModelAndView(ViewConstant.USER_PROFILE);
		}
	}

	/**
	 * This method is used to map payment request
	 * 
	 * @param userDTO
	 *            to receive the incoming data
	 * @param session
	 *            the current session
	 * @param result
	 *            for validating incoming data
	 * @param model
	 *            to map model attributes
	 * @return payment view
	 */
	@PostAuthorize("isAnonymous()")
	@RequestMapping(value = "payment", method = RequestMethod.POST)
	public ModelAndView getPayment(@Valid @ModelAttribute("userData") UserDTO userDTO, BindingResult result,
			HttpSession session, Model model) throws CustomException {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.REGISTRATION, ModelAttributeConstant.ERROR_MESSAGE,
					"Invalid User data");
		}
		Optional<User> existingEmail = userService.getUserByEmail(userDTO.getEmail());
		Optional<User> existingMobileNo = userService.getUserByMobileNo(userDTO.getMobileNo());
		Optional<ProofDetail> proofDetail = userService.getProofDetailByProofNo(userDTO.getProofNo());

		if (existingEmail.isPresent()) {
			logger.info("An user with the email id already exists. Transaction cancelled.");
			model.addAttribute("failure", "User with same email already exists!!");
			return new ModelAndView(ViewConstant.REGISTRATION);
		} else if (existingMobileNo.isPresent()) {
			logger.info("An user with the mobile number already exists. Transaction cancelled.");
			model.addAttribute("failure", "User with same mobile number already exists!!");
			return new ModelAndView(ViewConstant.REGISTRATION);
		} else if (proofDetail.isPresent()) {
			logger.info("An user with the proof number already exists. Transaction cancelled.");
			model.addAttribute("failure", "User with same proof number already exists!!");
			return new ModelAndView(ViewConstant.REGISTRATION);
		}

		try {
			userService.saveUserDocument(userDTO);
		} catch (IOException e) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.PAYLOAD_TOO_LARGE);
		}
		session.setAttribute("userDTO", userDTO);
		return new ModelAndView(ViewConstant.PAYMENT);
	}

	/**
	 * This method maps the request for fetching all users detail.
	 * 
	 * @return searchUsers view
	 */
	@RequestMapping(value = { "admin/userList", "manager/userList" })
	public ModelAndView userList() {
		return new ModelAndView(ViewConstant.SEARCH_USERS);
	}
}
