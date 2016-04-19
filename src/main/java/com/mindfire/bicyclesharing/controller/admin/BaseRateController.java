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
import com.mindfire.bicyclesharing.dto.BaseRateDTO;
import com.mindfire.bicyclesharing.service.BaseRateService;

/**
 * This class contains all the Request Mappings related to the navigation of the
 * Base Rate view from admin section.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class BaseRateController {

	@Autowired
	private BaseRateService baseRateService;

	/**
	 * This method maps the request to update base rate view along with base
	 * rate data.
	 * 
	 * @param model
	 *            this is used to map model attributes
	 * @return updateBaseRate view
	 */
	@RequestMapping(value = "/admin/updateBaseRate", method = RequestMethod.GET)
	public ModelAndView getBaseRate(Model model) {
		model.addAttribute("groupType", baseRateService.getBaseRate("USER"));
		return new ModelAndView(ViewConstant.UPDATE_BASE_RATE);
	}

	/**
	 * This method maps the request for updating the base rate details. Simply
	 * render the updatBaseRate view along with data
	 * 
	 * @param baseRateDTO
	 *            this object holds base rate related data.
	 * @param result
	 *            this is used for validating the base rate details.
	 * @param redirectAttributes
	 *            this is used to map redirect attributes.
	 * @return updatBaseRate view.
	 */
	@RequestMapping(value = "/admin/updateBaseRate", method = RequestMethod.POST)
	public ModelAndView updateBaseRate(@Valid @ModelAttribute("baseRateData") BaseRateDTO baseRateDTO,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Enter valid data!");
			return new ModelAndView(ViewConstant.REDIRECT + "/admin/" + ViewConstant.UPDATE_BASE_RATE);
		}

		if (null == baseRateService.updateBaseRate(baseRateDTO)) {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Oops Operation failed..!");
		} else {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.SUCCESS_MESSAGE, "Successfully updated...!");
		}
		return new ModelAndView(ViewConstant.REDIRECT + "/admin/" + ViewConstant.UPDATE_BASE_RATE);
	}

}
