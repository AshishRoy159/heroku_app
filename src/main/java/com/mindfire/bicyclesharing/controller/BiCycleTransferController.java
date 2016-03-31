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

package com.mindfire.bicyclesharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.dto.TransferRequestDTO;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.service.TransferRequestService;

/**
 * BiCycleTransferController contains all the mappings related to the bicycle
 * transfer.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class BiCycleTransferController {

	@Autowired
	private TransferRequestService transferRequestService;

	/**
	 * This method maps the request for bicycle transfer request page. Simply
	 * renders the transferRequest view
	 * 
	 * @return transferRequest view
	 */
	@RequestMapping(value = "manager/transferRequest", method = RequestMethod.GET)
	public ModelAndView transferRequest() {
		return new ModelAndView("transferRequest");
	}

	/**
	 * This method is used to handle incoming bicycle transfer requests.
	 * 
	 * @param quantity
	 *            the amount requested
	 * @param redirectAttributes
	 *            to map the model attributes
	 * @return tranferRequest view
	 */
	@RequestMapping(value = "manager/sendRequest", method = RequestMethod.POST)
	public ModelAndView sendRequest(@ModelAttribute("transferData") TransferRequestDTO transferRequestDTO,
			RedirectAttributes redirectAttributes, Authentication authentication) {
		if (transferRequestService.addNewTransferRequest(authentication, transferRequestDTO) == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Transfer request filed!");
		} else {
			redirectAttributes.addFlashAttribute("successMessage",
					"Transfer request for " + transferRequestDTO.getQuantity() + " bicycles sent successfully");
		}

		return new ModelAndView("redirect:transferRequest");
	}
	
	@RequestMapping(value = "admin/requests", method = RequestMethod.GET)
	public ModelAndView viewRequests(@RequestParam(value = "page") Integer page, Model model) {
		Page<TransferRequest> allrequests = transferRequestService.findAllRequests(page);
		int endIndex = allrequests.getTotalPages();
		model.addAttribute("requests", allrequests);
		model.addAttribute("currentIndex", page);
		model.addAttribute("endIndex", endIndex);
		
		return new ModelAndView("requestsAndNotificatons");
	}

}
