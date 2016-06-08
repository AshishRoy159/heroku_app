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

import java.text.ParseException;
import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.constant.CustomLoggerConstant;
import com.mindfire.bicyclesharing.constant.ModelAttributeConstant;
import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.dto.ManageRateGroupDTO;
import com.mindfire.bicyclesharing.dto.RateGroupDTO;
import com.mindfire.bicyclesharing.dto.RateGroupTypeDTO;
import com.mindfire.bicyclesharing.model.RateGroup;
import com.mindfire.bicyclesharing.service.RateGroupService;
import com.mindfire.bicyclesharing.service.UserService;

/**
 * This class contains all the Request Mappings related to the rate group
 * related pages from admin section.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class RateGroupController {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private RateGroupService rateGroupService;

	@Autowired
	private UserService userService;

	/**
	 * This method is used to map requests for adding new rate group. Simply
	 * renders the addNewRateGroup view.
	 * 
	 * @return addNewRateGroup view
	 */
	@RequestMapping(value = "/admin/addRateGroup", method = RequestMethod.GET)
	public ModelAndView addNewRateGroup() {
		return new ModelAndView("addNewRateGroup");
	}

	/**
	 * This method is used to map requests for adding new rate group to the
	 * database.
	 * 
	 * @param rateGroupDTO
	 *            the new rate group details
	 * @param result
	 *            for validating incoming data
	 * @return addNewRateGroup view
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	@RequestMapping(value = "/admin/addRateGroup", method = RequestMethod.POST)
	public @ResponseBody String addRateGroup(@Valid @ModelAttribute RateGroupDTO rateGroupDTO, BindingResult result)
			throws ParseException {

		if (result.hasErrors()) {
			return "Enter Valid Data";
		}
		RateGroup message = rateGroupService.addNewRateGroup(rateGroupDTO);
		if (null == message) {
			return "Operation Failed";
		} else {
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
			return "Succesfully Added";
		}
	}

	/**
	 * This method maps the Rate Group Details request. Simply render the
	 * rateGroupDetails view.
	 * 
	 * @param model
	 *            to map the model attributes.
	 * @return rateGroupDetails view.
	 */
	@RequestMapping(value = "admin/rateGroupDetails", method = RequestMethod.GET)
	public ModelAndView getRateGroup(Model model) {
		List<RateGroup> rateGroups = rateGroupService.getAllRateGroup();
		return new ModelAndView(ViewConstant.RATE_GROUP_DETAILS, ModelAttributeConstant.RATE_GROUPS, rateGroups);
	}

	/**
	 * This method is used to map request and render the view along with rate
	 * groups.
	 * 
	 * @return selectRateGroup view
	 */
	@RequestMapping(value = "/admin/selectRateGroup", method = RequestMethod.GET)
	public ModelAndView checkGroupType() {
		List<RateGroup> rateGroups = rateGroupService.getAllRateGroupAndIsActive(true);
		return new ModelAndView("selectRateGroup", "rateGroups", rateGroups);
	}

	/**
	 * This method is used to map the request for update the rate group and
	 * simply render the view along with the rate group data
	 * 
	 * @param rateGroupTypeDTO
	 *            this object contains rate group id
	 * @param redirectAttributes
	 *            this is used to hold the messages and object for retrieving
	 *            data on the view.
	 * @param result
	 *            this is used to validate the rate group type DTO
	 * @return updateRateGroup or selectRateGroup view.
	 */
	@RequestMapping(value = "/admin/updateRateGroup", method = RequestMethod.POST)
	public ModelAndView updateRateGroupView(
			@Valid @ModelAttribute("rateGroupTypeData") RateGroupTypeDTO rateGroupTypeDTO, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Invalid Data..");
			return new ModelAndView("redirect:/admin/selectRateGroup");
		}
		RateGroup rateGroup = rateGroupService.getRateGroupById(rateGroupTypeDTO.getRateGroupId());

		if (null == rateGroup) {
			logger.info("The rate group doesn't exist." + CustomLoggerConstant.TRANSACTION_FAILED);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Operation failed..");
			return new ModelAndView("redirect:/admin/selectRateGroup");
		}
		return new ModelAndView("updateRateGroup", "rateGroup", rateGroup);
	}

	/**
	 * This is used to map the request for update the rate group and create new
	 * rate group and simply render the view.
	 * 
	 * @param rateGroupDTO
	 *            this object contains rate group related data
	 * @param result
	 *            this is used to validate the data Rate Group DTO
	 * @return selectRateGroupType view
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	@RequestMapping(value = "/admin/updatedRateGroup", method = RequestMethod.POST)
	public @ResponseBody String updateRateGroup(@Valid @ModelAttribute RateGroupDTO rateGroupDTO, BindingResult result)
			throws ParseException {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return "Inavlid Data ! ";
		}
		String message = rateGroupService.updateRateGroup(rateGroupDTO);
		if (null == message) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			return "Operation Failed. !";
		} else {
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
			return "Successfully Updated";
		}

	}

	/**
	 * This method is used to map manage rate group and simply render the view
	 * along with the rateGroup data.
	 * 
	 * @param model
	 *            this is used to show the message and data on the view.
	 * @return manageRateGroup view.
	 */
	@RequestMapping(value = "/manager/manageRateGroup", method = RequestMethod.GET)
	public ModelAndView manageRateGroupView(Model model) {
		List<RateGroup> rateGroups = rateGroupService.getAllRateGroupAndIsActive(true);
		model.addAttribute(ModelAttributeConstant.RATE_GROUPS, rateGroups);
		return new ModelAndView(ViewConstant.MANAGE_RATE_GROUP);
	}

	/**
	 * This method is used map the request assignRateGroup for update the rate
	 * group to specific user.
	 * 
	 * @param manageRateGroupDTO
	 *            Manage rate group related data.
	 * @param result
	 *            to validate incoming data
	 * @param redirectAttributes
	 *            this is used to show the message and data on the view.
	 * @return manageRateGroup view.
	 */
	@RequestMapping(value = "/manager/assignRateGroup", method = RequestMethod.POST)
	public ModelAndView assignRateGroup(@ModelAttribute("assignRateGroupData") ManageRateGroupDTO manageRateGroupDTO,
			BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Invalid Data.");
		} else if (null == userService.UpdateRateGroup(manageRateGroupDTO)) {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Operation failed..!");
		} else {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.SUCCESS_MESSAGE,
					"Rate group is assigned successfully!");
		}
		return new ModelAndView(ViewConstant.REDIRECT + "/manager/" + ViewConstant.MANAGE_RATE_GROUP);
	}
}
