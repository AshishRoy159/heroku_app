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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.component.BiCycleComponent;
import com.mindfire.bicyclesharing.dto.BiCycleDTO;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.service.PickUpPointService;

/**
 * This class contains all the Request Mappings related to the navigation of the
 * Bicycle related pages from admin section.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class BicycleController {

	@Autowired
	private PickUpPointService pickUpPointService;

	@Autowired
	private BiCycleComponent biCycleComponent;

	/**
	 * This method maps the add new pickup point request. Simply render the
	 * addNewBicycle view.
	 * 
	 * @param model
	 *            to map model attributes
	 * @return addNewBicycle view
	 */
	@RequestMapping(value = "admin/addNewBicycle", method = RequestMethod.GET)
	public ModelAndView addBicycleForm(Model model) {
		model.addAttribute("pickUpPoints", pickUpPointService.getAllPickupPoints());
		return new ModelAndView("addNewBicycle");
	}

	/**
	 * This method receives data from the addNewBicycle view and sends the data
	 * to the corresponding component class
	 * 
	 * @param biCycleDTO
	 *            to receive the incoming data
	 * @param result
	 *            to validate the incoming data
	 * @param redirectAttributes
	 *            to map the model attributes
	 * @return addNewBicycle view
	 */
	@RequestMapping(value = "admin/addBicycle", method = RequestMethod.POST)
	public ModelAndView addedNewBicycle(@ModelAttribute("bicycleData") BiCycleDTO biCycleDTO, BindingResult result,
			RedirectAttributes redirectAttributes) {
		BiCycle biCycle = biCycleComponent.mapBiCycleData(biCycleDTO);

		if (biCycle == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Oops... Operation failed!!");
			return new ModelAndView("redirect:addNewBicycle");
		} else {
			redirectAttributes.addFlashAttribute("successMessage", "Successfully Added!!!");
			return new ModelAndView("redirect:addNewBicycle");
		}
	}
}
