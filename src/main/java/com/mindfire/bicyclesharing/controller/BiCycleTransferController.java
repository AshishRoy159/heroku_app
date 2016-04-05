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

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.dto.TransferDataDTO;
import com.mindfire.bicyclesharing.dto.TransferRensponseDTO;
import com.mindfire.bicyclesharing.dto.TransferRequestDTO;
import com.mindfire.bicyclesharing.dto.TransferRequestRespondedDTO;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.Transfer;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.model.TransferResponse;
import com.mindfire.bicyclesharing.service.BiCycleService;
import com.mindfire.bicyclesharing.service.PickUpPointManagerService;
import com.mindfire.bicyclesharing.service.TransferRequestService;
import com.mindfire.bicyclesharing.service.TransferResponseService;
import com.mindfire.bicyclesharing.service.TransferService;

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

	@Autowired
	private TransferResponseService transferResponseService;

	@Autowired
	private TransferService transferService;

	@Autowired
	private PickUpPointManagerService pickUpPointManagerService;

	@Autowired
	private BiCycleService bicycleService;

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
	 * This method is used to handle incoming bicycle transfer requests from
	 * transferRequest view.
	 * 
	 * @param transferRequestDTO
	 *            the incoming transfer request data
	 * @param redirectAttributes
	 *            to map the model attributes
	 * @param authentication
	 *            to get the current logged in user information
	 * @return transferRequest view
	 */
	@RequestMapping(value = "manager/sendRequest", method = RequestMethod.POST)
	public ModelAndView sendRequest(@Valid @ModelAttribute("transferData") TransferRequestDTO transferRequestDTO,
			BindingResult result, RedirectAttributes redirectAttributes, Authentication authentication) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("transferErrorMessage",
					"Inavlid Request. Please enter valid quantity");
			return new ModelAndView("redirect:transferRequest");
		}
		if (transferRequestService.addNewTransferRequest(authentication, transferRequestDTO) == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Transfer request filed!");
		} else {
			redirectAttributes.addFlashAttribute("successMessage",
					"Transfer request for " + transferRequestDTO.getQuantity() + " bicycles sent successfully");
		}

		return new ModelAndView("redirect:transferRequest");
	}

	/**
	 * This method is used to map request for the requestsAndNotificatons page
	 * from admin. Simply renders the requestsAndNotificatons view.
	 * 
	 * @param model
	 *            to map the model attributes
	 * @return requestsAndNotificatons view
	 */
	@RequestMapping(value = "admin/requests", method = RequestMethod.GET)
	public ModelAndView viewRequests(Model model) {
		List<TransferRequest> allrequests = transferRequestService.findAllRequests();
		model.addAttribute("requests", allrequests);

		return new ModelAndView("requestsAndNotificatons");
	}

	/**
	 * This method is used to map request for the requestsAndNotificatons page
	 * from manager. Simply renders the requestsAndNotificatons view.
	 * 
	 * @param model
	 *            to map the model attributes
	 * @param authentication
	 *            to get the current logged in user information
	 * @return requestsAndNotificatons view
	 */
	@RequestMapping(value = "manager/requests", method = RequestMethod.GET)
	public ModelAndView viewOthersRequests(Model model, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		List<TransferRequestRespondedDTO> allrequests = transferRequestService.findOtherRequest(currentUser);
		model.addAttribute("requests", allrequests);

		return new ModelAndView("requestsAndNotificatons");
	}

	/**
	 * This method is used to map the requests for response view from manager.
	 * Simply renders the transferResponseManager view.
	 * 
	 * @param requestId
	 *            the id of the request to be responded
	 * @param model
	 *            to map the model attributes
	 * @return transferResponseManager view
	 */
	@RequestMapping(value = "manager/respond/{id}", method = RequestMethod.GET)
	public ModelAndView managerResponse(@PathVariable("id") Long requestId, Model model,
			Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		int currentAvailable = pickUpPointManagerService.getCurrentAvailability(currentUser.getUser());
		TransferRequest request = transferRequestService.findTransferRequest(requestId);
		model.addAttribute("request", request);
		model.addAttribute("max", Math.min(request.getQuantity(), currentAvailable));
		return new ModelAndView("transferResponseManager");
	}

	/**
	 * This method is used to handle the incoming response details from the
	 * transferResponseManager view.
	 * 
	 * @param transferResponseDTO
	 *            the incoming response data
	 * @param authentication
	 *            to get the current logged in user information
	 * @return
	 */
	@RequestMapping(value = "manager/sendResponse", method = RequestMethod.POST)
	public ModelAndView sendResponse(@ModelAttribute("responseData") TransferRensponseDTO transferResponseDTO,
			Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		transferResponseService.addNewResponse(transferResponseDTO, currentUser);
		return new ModelAndView("redirect:requests");

	}

	/**
	 * This method is used to map requests for showing pickup point's responses
	 * to a specific transfer request. Simply renders the transferResponseAdmin
	 * view.
	 * 
	 * @param requestId
	 *            id of the transfer request
	 * @param model
	 *            to map the model attributes
	 * @return transferResponseAdmin view
	 */
	@RequestMapping(value = "admin/respond/{id}", method = RequestMethod.GET)
	public ModelAndView adminResponse(@PathVariable("id") Long requestId, Model model) {
		TransferRequest transferRequest = transferRequestService.findTransferRequest(requestId);
		List<TransferResponse> responses = transferResponseService.findresponsesForRequest(transferRequest);
		model.addAttribute("request", transferRequest);
		model.addAttribute("responses", responses);
		return new ModelAndView("transferResponseAdmin");
	}

	/**
	 * This method is used to approve one response from a pickup point to a
	 * transfer request.
	 * 
	 * @param responseId
	 *            id of the response
	 * @return transferResponseAdmin view
	 */
	@RequestMapping(value = "admin/approveResponse/{id}", method = RequestMethod.GET)
	public ModelAndView approveResponse(@PathVariable("id") Long responseId) {
		TransferResponse transferResponse = transferResponseService.findResponseById(responseId);
		transferResponseService.updateApproval(true, responseId);
		transferService.addNewTransfer(transferResponse);
		return new ModelAndView("redirect:/admin/respond/" + transferResponse.getRequest().getRequestId());
	}

	/**
	 * 
	 * @param model
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "manager/transfers", method = RequestMethod.GET)
	public ModelAndView viewTransfers(Model model, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		List<Transfer> outgoingTransfers = transferService.findOutgoingTransfers(currentUser);
		List<Transfer> incomingTransfers = transferService.findIncomingTransfers(currentUser);
		model.addAttribute("outgoings", outgoingTransfers);
		model.addAttribute("incomings", incomingTransfers);
		return new ModelAndView("transfers");
	}

	/**
	 * 
	 * @param transferId
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "manager/sendShipment/{id}", method = RequestMethod.GET)
	public ModelAndView sendShipment(@PathVariable("id") Long transferId, Model model, HttpSession session) {
		Transfer transfer = transferService.findTransferDetails(transferId);
		List<BiCycle> biCycles = bicycleService.findBicyclesForShipment(transfer);
		session.setAttribute("bicycles", biCycles);

		return new ModelAndView("transferConfirm", "transfer", transfer);
	}

	/**
	 * 
	 * @param transferDataDTO
	 * @return
	 */
	@RequestMapping(value = "manager/confirmShipment", method = RequestMethod.POST)
	public ModelAndView confirmShipment(@ModelAttribute("transferData") TransferDataDTO transferDataDTO,
			HttpSession session) {
		transferService.confirmTransfer(transferDataDTO, session);
		return new ModelAndView("redirect:transfers");
	}

}
