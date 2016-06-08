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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.constant.CustomLoggerConstant;
import com.mindfire.bicyclesharing.constant.ModelAttributeConstant;
import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.dto.PickUpPointDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
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

	Logger logger = Logger.getLogger(getClass());

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
		return new ModelAndView(ViewConstant.ADD_NEW_PICKUP_POINT);
	}

	/**
	 * This method receives data from the addNewPickUpPoint view and send the
	 * data to the corresponding component class
	 * 
	 * @param pickUpPointDTO
	 *            to receive the incoming data
	 * @param result
	 *            to validate the incoming data
	 * @return addNewPickupPoint view
	 */
	@RequestMapping(value = "/admin/addPickupPoint", method = RequestMethod.POST)
	public @ResponseBody String addedPickupPoint(
			@Valid @ModelAttribute("pickupPointData") PickUpPointDTO pickUpPointDTO, BindingResult result) {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return "Oops... Operation failed!!";
		} else if (null == pickUpPointService.savePickUpPoint(pickUpPointDTO)) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			return "Oops... Operation failed!!";
		} else {
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
			return "Successfully Added!!!";
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
	@RequestMapping(value = { "admin/pickupPointDetails", "manager/pickupPointDetails" }, method = RequestMethod.GET)
	public ModelAndView pickupPointDetails(Model model) {
		List<PickUpPoint> pickUpPoints = pickUpPointService.getAllPickupPoints();
		return new ModelAndView(ViewConstant.PICKUP_POINT_DETAILS, ModelAttributeConstant.PICKUP_POINTS, pickUpPoints);
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

		if (pickUpPointService.getPickupPointById(pickUpPointId) == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		return new ModelAndView(ViewConstant.UPDATE_PICKUP_POINT_DETAILS, ModelAttributeConstant.PICKUP_POINT,
				pickUpPointService.getPickupPointById(pickUpPointId));
	}

	/**
	 * This method receives the data from updatePickupPointDetails view and send
	 * the data to the corresponding component class.
	 * 
	 * @param pickUpPointDTO
	 *            to receive the incoming data
	 * @param result
	 *            for validation of incoming data
	 * @param redirectAttributes
	 *            for mapping model attributes
	 * @return pickupPointDetails view
	 */
	@RequestMapping(value = "admin/updatePickupPointDetails", method = RequestMethod.POST)
	public ModelAndView updatePickUpPointDetails(
			@Valid @ModelAttribute("pickupPointData") PickUpPointDTO pickUpPointDTO, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Please Enter valid data");
		} else if (null == pickUpPointService.updatePickUpPointDetails(pickUpPointDTO)) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Operation Failed...!!");
		} else {
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
		}
		return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.UPDATE_PICKUP_POINT_DETAILS + "/"
				+ pickUpPointDTO.getPickUpPointId());
	}
}
