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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.constant.ModelAttributeConstant;
import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.dto.RateGroupDTO;
import com.mindfire.bicyclesharing.model.RateGroup;
import com.mindfire.bicyclesharing.service.RateGroupService;

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

	@Autowired
	private RateGroupService rateGroupService;

	@RequestMapping(value = "/admin/addNewRateGroup", method = RequestMethod.GET)
	public ModelAndView addNewRateGroup() {
		return new ModelAndView("addNewRateGroup");
	}

	@RequestMapping(value = "/admin/addRateGroup", method = RequestMethod.POST)
	public ModelAndView addRateGroup(@Valid @ModelAttribute("rateGroupData") RateGroupDTO rateGroupDTO,
			BindingResult result, RedirectAttributes redirectAttributes) throws ParseException {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Invalid Data..!");
			return new ModelAndView("redirect:/admin/addNewRateGroup");
		}
		if (null == rateGroupService.addNewRateGroup(rateGroupDTO)) {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Operation failed...");
			return new ModelAndView("redirect:/admin/addNewRateGroup");
		} else {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.SUCCESS_MESSAGE, "Successfully added..!");
			return new ModelAndView("redirect:/admin/addNewRateGroup");
		}
	}

	/**
	 * This method maps the Rate Group Details request. Simply render
	 * the rateGroupDetails view.
	 * @param model 
	 * 				to map the model attributes.
	 * @return rateGroupDetails view.
	 */
	@RequestMapping(value="admin/rateGroupDetails", method = RequestMethod.GET)
	public ModelAndView getRateGroup(Model model){
		List<RateGroup> rateGroups = rateGroupService.getAllRateGroup();
		return new ModelAndView(ViewConstant.RATE_GROUP_DETAILS,ModelAttributeConstant.RATE_GROUPS,rateGroups);
	}
}
