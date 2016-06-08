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

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.constant.CustomLoggerConstant;
import com.mindfire.bicyclesharing.constant.ModelAttributeConstant;
import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.dto.BiCycleDTO;
import com.mindfire.bicyclesharing.service.BiCycleService;
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

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private PickUpPointService pickUpPointService;

	@Autowired
	private BiCycleService biCycleService;

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
		model.addAttribute(ModelAttributeConstant.PICKUP_POINTS, pickUpPointService.getAllPickupPoints());
		return new ModelAndView(ViewConstant.ADD_NEW_BICYCLE);
	}

	/**
	 * This method receives data from the addNewBicycle view and sends the data
	 * to the corresponding component class
	 * 
	 * @param biCycleDTO
	 *            to receive the incoming data
	 * @param result
	 *            to validate the incoming data
	 * @return addNewBicycle view
	 */
	@RequestMapping(value = "admin/addNewBicycle", method = RequestMethod.POST)
	public @ResponseBody String addedNewBicycle(@Valid @ModelAttribute BiCycleDTO biCycleDTO, BindingResult result) {
		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return "Oops... Operation failed!!";
		}
		String message = biCycleService.saveBiCycleDetails(biCycleDTO);

		if (message == null) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			return "No space for new bicycle at this pickup point!!";
		} else if(message.equals("duplicate data")) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			return "Bicycle with the given chasis number already exists.";
		} else {
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
			return message;
		}
	}
}
