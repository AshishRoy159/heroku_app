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

package com.mindfire.bicyclesharing.controller.admin;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.constant.CustomLoggerConstant;
import com.mindfire.bicyclesharing.constant.ModelAttributeConstant;
import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.dto.ManageRoleDTO;
import com.mindfire.bicyclesharing.event.AccountUpdationEvent;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.security.CurrentUser;
import com.mindfire.bicyclesharing.service.PickUpPointManagerService;
import com.mindfire.bicyclesharing.service.PickUpPointService;
import com.mindfire.bicyclesharing.service.UserService;

/**
 * ManageRoleController contains all the mappings related to managing user roles
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class ManageRoleController {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private PickUpPointService pickUpPointService;

	@Autowired
	private PickUpPointManagerService pickUpPointManagerService;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	/**
	 * This method maps requests for manageRole view
	 * 
	 * @param userId
	 *            id of the respective user
	 * @param model
	 *            to map the model attribute
	 * @param authentication
	 *            to get the current logged in user details
	 * @return manageRole view
	 */
	@RequestMapping("/admin/manageRole/{id}")
	public ModelAndView manageRole(@PathVariable("id") Long userId, Model model, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

		if (null == userService.userDetails(userId)) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}

		if (userId == currentUser.getUserId()) {
			logger.info("Redirected as request was for updating current user role. Transaction cancelled.");
			return new ModelAndView(ViewConstant.SEARCH_USERS, ModelAttributeConstant.USERS_LIST,
					userService.getAllUsers());
		}
		model.addAttribute("userId", userId);
		return new ModelAndView(ViewConstant.MANAGE_ROLE, ModelAttributeConstant.PICKUP_POINTS,
				pickUpPointService.getAllPickupPoints());
	}

	/**
	 * This method maps request for managing user role and send data to the
	 * corresponding component classes
	 * 
	 * @param manageRoleDTO
	 *            to receive the incoming data
	 * @param result
	 *            for validation of incoming data
	 * @param model
	 *            to map model attributes
	 * @return searchUsers view
	 */
	@RequestMapping(value = "admin/updateRole", method = RequestMethod.POST)
	public ModelAndView setPickUpPoint(@Valid @ModelAttribute("manageRoleData") ManageRoleDTO manageRoleDTO,
			BindingResult result, Model model) {
		List<User> users = userService.getAllUsers();

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			model.addAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Something went wrong. Operation failed.");
		} else if (userService.updateUserRole(manageRoleDTO) > 0) {
			logger.info("Role choice was valid.");

			if (manageRoleDTO.getUserRoleId() == 2) {
				logger.info("Role choice was manager.");
				pickUpPointManagerService.setPickUpPointToManager(manageRoleDTO);
			}
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
		} else {
			logger.info("Invalid role choice.");
		}

		return new ModelAndView(ViewConstant.SEARCH_USERS, ModelAttributeConstant.USERS_LIST, users);
	}

	/**
	 * This method is used to approve the user by admin or manager.
	 * 
	 * @param id
	 *            user id
	 * @param authentication
	 *            this is used for retrieve the current user.
	 * @return searchUser view.
	 */
	@RequestMapping(value = "/user/userApproval/{id}", method = RequestMethod.GET)
	public @ResponseBody String userApproval(@PathVariable("id") Long id, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

		if (userService.userDetails(id) == null) {
			return "No data available for the specified userId";
		}

		if ((currentUser.getUserRole().equals("ADMIN") && currentUser.getUserId() != id)
				|| (currentUser.getUserRole().equals("MANAGER")
						&& userService.userDetails(id).getRole().getUserRole().equals("USER"))) {
			logger.info("Permission granted to change approval of user.");
			String approval;
			User user = userService.setApproval(id);
			if (null == user) {
				logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
				return "Sorry operation failed...!";
			} else {
				logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
				if (user.getIsApproved()) {
					approval = "approved";
				} else {
					approval = "disapproved";
				}
				try {
					eventPublisher.publishEvent(new AccountUpdationEvent(user, approval));
				} catch (Exception me) {
					System.out.println(me.getMessage());
				}
				return approval;
			}
		} else {
			logger.info("Permission not granted for the request to update approval of user.");
			return "You don't have permission to update this details...!";
		}

	}

	/**
	 * This method is used to enable the user by admin or manager.
	 * 
	 * @param id
	 *            user id
	 * @param authentication
	 *            this is used for retrieve the current user.
	 * @return searchUser view.
	 */
	@RequestMapping(value = "/user/userEnable/{id}", method = RequestMethod.GET)
	public @ResponseBody String userEnable(@PathVariable("id") Long id, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

		String enable;
		if (null == userService.userDetails(id)) {
			return "User does not exists with provided userId";
		}

		if ((currentUser.getUserRole().equals("ADMIN") && currentUser.getUserId() != id)
				|| (currentUser.getUserRole().equals("MANAGER")
						&& userService.userDetails(id).getRole().getUserRole().equals("USER"))) {
			logger.info("Permission granted to enable user.");

			User user = userService.setEnable(id);
			if (null == user) {
				logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
				return "Sorry operation failed...!";
			} else {
				logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
				if (user.getEnabled()) {
					enable = "enabled";
				} else {
					enable = "disabled";
				}
				try {
					eventPublisher.publishEvent(new AccountUpdationEvent(user, enable));
				} catch (Exception me) {
					System.out.println(me.getMessage());
				}
				return enable;
			}
		} else {
			logger.info("Permission not granted for the request to activate/deactivae user.");
			return "You don't have permission to update this details...!";
		}
	}
}
