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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.component.PickUpPointComponent;
import com.mindfire.bicyclesharing.dto.PickUpPointDTO;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.service.PickUpPointService;

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

	@Autowired
	private PickUpPointService pickUpPointService;

	/**
	 * This method maps the add new pick up point request. Simply render the
	 * addNewPickupPoint view
	 * 
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
	 *            to receive the incoming data
	 * @param result
	 *            to validate the incoming data
	 * @param redirectAttributes
	 *            to map the model attributes
	 * @return addNewPickupPoint view
	 */
	@RequestMapping(value = "/admin/addPickupPoint", method = RequestMethod.POST)
	public ModelAndView addedPickupPoint(@ModelAttribute("pickupPointData") PickUpPointDTO pickUpPointDTO,
			BindingResult result, RedirectAttributes redirectAttributes) {
		PickUpPoint pickUpPoint = pickUpPointComponent.mapPickUpPointDetails(pickUpPointDTO);

		if (pickUpPoint == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Oops... Operation failed!!");
			return new ModelAndView("redirect:addNewPickupPoint");
		} else {
			redirectAttributes.addFlashAttribute("successMessage", "Successfully Added!!!");
			return new ModelAndView("redirect:addNewPickupPoint");
		}
	}

	/**
	 * This method maps the Pickup PointDetail Details request. Simply render
	 * the pickupPointDetails view.
	 * 
	 * @param model
	 *            to map the model attributes
	 * @return pickupPointDetails view
	 */
	@RequestMapping(value = "admin/pickupPointDetails", method = RequestMethod.GET)
	public ModelAndView pickupPointDetails(Model model) {
		List<PickUpPoint> pickUpPoints = pickUpPointService.getAllPickupPoints();
		return new ModelAndView("pickupPointDetails", "pickUpPointList", pickUpPoints);
	}

	/**
	 * This method maps the update pickup point details request. Simply render
	 * the updatePickupPointDetails view.
	 * 
	 * @param pickUpPointId
	 *            the id of the respective pickup point
	 * @return updatePickupPointDetails view
	 */
	@RequestMapping(value = "admin/updatePickupPointDetails/{id}", method = RequestMethod.GET)
	public ModelAndView pickupPointUpdateForm(@PathVariable("id") Integer pickUpPointId) {
		PickUpPoint pickUpPoint = pickUpPointService.getPickupPointById(pickUpPointId);
		return new ModelAndView("updatePickupPointDetails", "pickUpPoint", pickUpPoint);
	}

	/**
	 * This method receives the data from updatePickupPointDetails view and send
	 * the data to the corresponding component class.
	 * 
	 * @param pickUpPointDTO
	 *            to receive the incoming data
	 * @return pickupPointDetails view
	 */
	@RequestMapping(value = "admin/updatePickupPointDetails", method = RequestMethod.POST)
	public ModelAndView updatePickUpPointDetails(@ModelAttribute("pickupPointData") PickUpPointDTO pickUpPointDTO) {
		int updatedResult = pickUpPointComponent.mapUpdatePickUpPointDetails(pickUpPointDTO);

		if (updatedResult != 0) {
			return new ModelAndView("redirect:pickupPointDetails");
		}
		return new ModelAndView("redirect:pickupPointDetails");
	}
}