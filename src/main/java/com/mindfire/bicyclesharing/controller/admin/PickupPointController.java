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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.component.PickUpPointComponent;
import com.mindfire.bicyclesharing.dto.PickUpPointDTO;
import com.mindfire.bicyclesharing.model.PickUpPoint;

/**
 * This class contains all the Request Mappings related to the navigation of the
 * pickup point related pages from admin section.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class PickupPointController {

	@Autowired
	private PickUpPointComponent pickUpPointComponent;

	/**
	 * This method maps the add new pick up point request. Simply render the
	 * addNewPickupPoint view
	 * 
	 * @param model
	 * @return addNewPickupPoint view
	 */
	@RequestMapping(value = "admin/addNewPickupPoint", method = RequestMethod.GET)
	public ModelAndView addPickupPointForm() {
		return new ModelAndView("addNewPickupPoint");
	}

	/**
	 * This method receives data from the addNewPickUpPoint view and send the
	 * data to the corresponding component class
	 * 
	 * @param pickUpPointDTO
	 * @param result
	 * @param model
	 * @return addNewPickupPoint view
	 */
	@RequestMapping(value = "/admin/addPickupPoint", method = RequestMethod.POST)
	public ModelAndView addedPickupPoint(@ModelAttribute("pickupPointData") PickUpPointDTO pickUpPointDTO,
			BindingResult result, Model model) {
		PickUpPoint pickUpPoint = pickUpPointComponent.mapPickUpPointDetails(pickUpPointDTO);

		if (pickUpPoint == null) {
			model.addAttribute("errorMessage", "Operation Failed");
			return new ModelAndView("addNewPickupPoint");
		} else {
			model.addAttribute("successMessage", "Successfully added!!!");
			return new ModelAndView("addNewPickupPoint");
		}
	}

}