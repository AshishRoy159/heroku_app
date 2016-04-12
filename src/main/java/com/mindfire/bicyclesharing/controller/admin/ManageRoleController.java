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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.constant.ModelAttributeConstant;
import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.dto.ManageRoleDTO;
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

	@Autowired
	private PickUpPointService pickUpPointService;

	@Autowired
	private PickUpPointManagerService pickUpPointManagerService;

	@Autowired
	private UserService userService;

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
		if (userId == currentUser.getUserId()) {
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
			model.addAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Something went wrong. Operation failed.");
			return new ModelAndView(ViewConstant.SEARCH_USERS, ModelAttributeConstant.USERS_LIST, users);
		}

		if (userService.updateUserRole(manageRoleDTO) > 0) {
			if (manageRoleDTO.getUserRoleId() == 2) {
				pickUpPointManagerService.setPickUpPointToManager(manageRoleDTO);
			}
			return new ModelAndView(ViewConstant.SEARCH_USERS, ModelAttributeConstant.USERS_LIST, users);
		} else {
			return new ModelAndView(ViewConstant.SEARCH_USERS, ModelAttributeConstant.USERS_LIST, users);
		}
	}

	/**
	 * This method is used to approve the user by admin or manager.
	 * 
	 * @param id
	 *            user id
	 * @param redirectAttributes
	 *            this is used for displaying messages
	 * @param authentication
	 *            this is used for retrieve the current user.
	 * @return searchUser view.
	 */
	@RequestMapping(value = "/user/userApproval/{id}", method = RequestMethod.GET)
	public ModelAndView userApproval(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
			Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		if ((currentUser.getUserRole().equals("ADMIN") && currentUser.getUserId() != id) || (currentUser.getUserRole().equals("MANAGER")
				&& userService.userDetails(id).getRole().getUserRole().equals("USER"))) {
			if (null == userService.setApproval(id)) {
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
						"Sorry operation failed...!");
				if (currentUser.getUserRole().equals("ADMIN")) {
					return new ModelAndView(ViewConstant.REDIRECT + "/admin/userList");
				} else {
					return new ModelAndView(ViewConstant.REDIRECT + "/manager/userList");
				}
			} else {
				if (currentUser.getUserRole().equals("ADMIN")) {
					return new ModelAndView(ViewConstant.REDIRECT + "/admin/userList");
				} else {
					return new ModelAndView(ViewConstant.REDIRECT + "/manager/userList");
				}
			}
		} else {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
					"You don't have permission to update this details...!");
			if (currentUser.getUserRole().equals("ADMIN")) {
				return new ModelAndView(ViewConstant.REDIRECT + "/admin/userList");
			} else {
				return new ModelAndView(ViewConstant.REDIRECT + "/manager/userList");
			}
		}
	}

	/**
	 * This method is used to enable the user by admin or manager.
	 * 
	 * @param id
	 *            user id
	 * @param redirectAttributes
	 *            this is used for displaying messages
	 * @param authentication
	 *            this is used for retrieve the current user.
	 * @return searchUser view.
	 */
	@RequestMapping(value = "/user/userEnable/{id}", method = RequestMethod.GET)
	public ModelAndView userEnable(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
			Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		if ((currentUser.getUserRole().equals("ADMIN") && currentUser.getUserId() != id) || (currentUser.getUserRole().equals("MANAGER")
				&& userService.userDetails(id).getRole().getUserRole().equals("USER"))) {
			if (null == userService.setEnable(id)) {
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
						"Sorry operation failed...!");
				if (currentUser.getUserRole().equals("ADMIN")) {
					return new ModelAndView(ViewConstant.REDIRECT + "/admin/userList");
				} else {
					return new ModelAndView(ViewConstant.REDIRECT + "/manager/userList");
				}
			} else {
				if (currentUser.getUserRole().equals("ADMIN")) {
					return new ModelAndView(ViewConstant.REDIRECT + "/admin/userList");
				} else {
					return new ModelAndView(ViewConstant.REDIRECT + "/manager/userList");
				}
			}
		} else {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
					"You don't have permission to update this details...!");
			if (currentUser.getUserRole().equals("ADMIN")) {
				return new ModelAndView(ViewConstant.REDIRECT + "/admin/userList");
			} else {
				return new ModelAndView(ViewConstant.REDIRECT + "/manager/userList");
			}
		}
	}
}
