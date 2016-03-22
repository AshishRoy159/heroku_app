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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.component.UserComponent;
import com.mindfire.bicyclesharing.dto.ManageRoleDTO;
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
	private UserComponent userComponent;

	@Autowired
	private PickUpPointService pickUpPointService;

	@Autowired
	private UserService userService;

	/**
	 * This method maps requests for manageRole view
	 * 
	 * @param userId
	 * @param model
	 * @return manageRole view
	 */
	@RequestMapping("/admin/manageRole/{id}")
	public ModelAndView manageRole(@PathVariable("id") Long userId, Model model) {
		model.addAttribute("userId", userId);
		return new ModelAndView("manageRole", "pickUpPoints", pickUpPointService.getAllPickupPoints());
	}

	/**
	 * This method maps request for managing user role and send data to the
	 * corresponding component classes
	 * 
	 * @param manageRoleDTO
	 * @return searchUsers view
	 */
	@RequestMapping(value = "admin/setPickUpPoint", method = RequestMethod.POST)
	public ModelAndView setPickUpPoint(@ModelAttribute("manageRoleData") ManageRoleDTO manageRoleDTO) {
		if (userComponent.mapUpdateRole(manageRoleDTO) > 0) {
			if (manageRoleDTO.getUserRoleId() == 2) {
				userComponent.mapPickUpPointManagerDetails(manageRoleDTO);
			}
			return new ModelAndView("searchUsers", "usersList", userService.getAllUsers());
		} else {
			return new ModelAndView("searchUsers", "usersList", userService.getAllUsers());
		}
	}
}
