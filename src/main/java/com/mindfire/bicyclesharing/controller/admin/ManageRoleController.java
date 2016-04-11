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
			return new ModelAndView("searchUsers", "usersList", userService.getAllUsers());
		}
		model.addAttribute("userId", userId);
		return new ModelAndView("manageRole", "pickUpPoints", pickUpPointService.getAllPickupPoints());
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
			model.addAttribute("errorMessage", "Something went wrong. Operation failed.");
			return new ModelAndView("searchUsers", "usersList", users);
		}

		if (userService.updateUserRole(manageRoleDTO) > 0) {
			if (manageRoleDTO.getUserRoleId() == 2) {
				pickUpPointManagerService.setPickUpPointToManager(manageRoleDTO);
			}
			return new ModelAndView("searchUsers", "usersList", users);
		} else {
			return new ModelAndView("searchUsers", "usersList", users);
		}
	}
}
