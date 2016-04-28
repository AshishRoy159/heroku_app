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
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.constant.CustomLoggerConstant;
import com.mindfire.bicyclesharing.constant.ModelAttributeConstant;
import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.dto.TransferDataDTO;
import com.mindfire.bicyclesharing.dto.TransferRensponseDTO;
import com.mindfire.bicyclesharing.dto.TransferRequestDTO;
import com.mindfire.bicyclesharing.dto.TransferRequestRespondedDTO;
import com.mindfire.bicyclesharing.enums.TransferStatusEnum;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.Transfer;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.model.TransferResponse;
import com.mindfire.bicyclesharing.security.CurrentUser;
import com.mindfire.bicyclesharing.service.BiCycleService;
import com.mindfire.bicyclesharing.service.BiCycleTransferService;
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

	Logger logger = Logger.getLogger(getClass());

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

	@Autowired
	private BiCycleTransferService biCycleTransferService;

	/**
	 * This method maps the request for bicycle transfer request page. Simply
	 * renders the transferRequest view
	 * 
	 * @return transferRequest view
	 */
	@RequestMapping(value = "manager/transferRequest", method = RequestMethod.GET)
	public ModelAndView transferRequest() {
		return new ModelAndView(ViewConstant.TRANSFER_REQUEST);
	}

	/**
	 * This method is used to handle incoming bicycle transfer requests from
	 * transferRequest view.
	 * 
	 * @param transferRequestDTO
	 *            the incoming transfer request data
	 * @param result
	 *            for validation of incoming data
	 * @param authentication
	 *            to get the current logged in user information
	 * @return transferRequest view
	 */
	@RequestMapping(value = "manager/sendRequest", method = RequestMethod.POST)
	public @ResponseBody String sendRequest(@Valid @ModelAttribute TransferRequestDTO transferRequestDTO,
			BindingResult result, Authentication authentication) {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return "Inavlid Request. Please enter valid quantity";
		} else if (transferRequestService.addNewTransferRequest(authentication, transferRequestDTO) == null) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			return "Request Failed..!! Request cannot exceed maximum capacity.";
		} else {
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
			return "Transfer request for " + transferRequestDTO.getQuantity() + " bicycles sent successfully";
		}
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
		List<TransferRequest> pendingRequests = transferRequestService.findRequestsByApproval(false);
		model.addAttribute(ModelAttributeConstant.REQUESTS, pendingRequests);
		return new ModelAndView(ViewConstant.REQUEST_AND_NOTIFICATIONS);
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
		List<TransferRequestRespondedDTO> allrequests = transferRequestService.findOtherRequest(currentUser, false);
		model.addAttribute(ModelAttributeConstant.REQUESTS, allrequests);
		return new ModelAndView(ViewConstant.REQUEST_AND_NOTIFICATIONS);
	}

	/**
	 * This method is used to map the requests for response view from manager.
	 * Simply renders the transferResponseManager view.
	 * 
	 * @param requestId
	 *            the id of the request to be responded
	 * @param model
	 *            to map the model attributes
	 * @param authentication
	 *            to get current logged in user details
	 * @return transferResponseManager view
	 */
	@PostAuthorize("@currentUserService.canAccessManagerResponse(principal, #requestId)")
	@RequestMapping(value = "/manager/respond/{id}", method = RequestMethod.GET)
	public ModelAndView managerResponse(@PathVariable("id") Long requestId, Model model,
			Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		TransferRequest request = transferRequestService.findTransferRequest(requestId);
		int currentAvailable = pickUpPointManagerService.getCurrentAvailability(currentUser.getUser());

		if (request == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		Optional<TransferResponse> response = transferResponseService.findResponseForRequest(request,
				pickUpPointManagerService.getPickupPointManager(currentUser.getUser()).getPickUpPoint());
		List<TransferResponse> responses = transferResponseService.findresponsesForRequest(request);

		model.addAttribute("responded", response.isPresent());
		model.addAttribute("responses", responses);
		model.addAttribute(ModelAttributeConstant.REQUEST, request);
		model.addAttribute("max", Math.min(request.getQuantity(), currentAvailable));
		return new ModelAndView(ViewConstant.TRANSFER_RESPONSE_MANAGER);
	}

	/**
	 * This method is used to handle the incoming response details from the
	 * transferResponseManager view.
	 * 
	 * @param transferResponseDTO
	 *            the incoming response data
	 * @param authentication
	 *            to get the current logged in user information
	 * @return requests view
	 */
	@RequestMapping(value = "manager/sendResponse", method = RequestMethod.POST)
	public ModelAndView sendResponse(@ModelAttribute("responseData") TransferRensponseDTO transferResponseDTO,
			Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		transferResponseService.addNewResponse(transferResponseDTO, currentUser);
		return new ModelAndView(ViewConstant.REDIRECT + "requests");

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

		if (transferRequest == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		List<TransferResponse> responses = transferResponseService.findresponsesForRequest(transferRequest);
		model.addAttribute(ModelAttributeConstant.REQUEST, transferRequest);
		model.addAttribute(ModelAttributeConstant.RESPONSES, responses);
		return new ModelAndView(ViewConstant.TRANSFER_RESPONSE_ADMIN);
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

		if (transferResponse == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		} else if (transferResponse.getRequest().getIsApproved()) {
			return new ModelAndView(
					ViewConstant.REDIRECT + "/admin/respond/" + transferResponse.getRequest().getRequestId());
		}
		transferResponseService.updateApproval(true, responseId);
		transferService.addNewTransfer(transferResponse);
		return new ModelAndView("redirect:/admin/respond/" + transferResponse.getRequest().getRequestId());
	}

	/**
	 * This method is used to map requests for closing a transfer request.
	 * 
	 * @param requestId
	 *            id of the transfer request
	 * @return Transfer requests view
	 */
	@RequestMapping(value = "/admin/closeRequest/{id}", method = RequestMethod.GET)
	public ModelAndView closeRequest(@PathVariable("id") Long requestId) {
		TransferRequest transferRequest = transferRequestService.findTransferRequest(requestId);

		if (null == transferRequest) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		transferRequestService.updateAppproval(transferRequest);
		return new ModelAndView(ViewConstant.REDIRECT + "/admin/requests");
	}

	/**
	 * This method is used to map requests for outgoing and incoming transfer
	 * from a pickup point. Simply renders the transfer view.
	 * 
	 * @param model
	 *            to map model attributes
	 * @param authentication
	 *            to get the current logged in user details
	 * @return transfers view
	 */
	@RequestMapping(value = "manager/transfers", method = RequestMethod.GET)
	public ModelAndView viewTransfers(Model model, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		List<Transfer> outgoingTransfers = transferService.findOutgoingTransfers(currentUser);
		List<Transfer> incomingTransfers = transferService.findIncomingTransfers(currentUser);

		model.addAttribute(ModelAttributeConstant.OUTGOINGS, outgoingTransfers);
		model.addAttribute(ModelAttributeConstant.INCOMINGS, incomingTransfers);
		return new ModelAndView(ViewConstant.TRANSFERS);
	}

	/**
	 * This method is used to map the request for sending bicycle transfer
	 * shipment. Renders the transferConfirm view.
	 * 
	 * @param transferId
	 *            id of the transfer record
	 * @param model
	 *            to map model attributes
	 * @param session
	 *            for session attributes
	 * @return transferConfirm view
	 */
	@PostAuthorize("@currentUserService.canAccessManagerSender(principal, #transferId)")
	@RequestMapping(value = "manager/sendShipment/{id}", method = RequestMethod.GET)
	public ModelAndView sendShipment(@PathVariable("id") Long transferId, Model model, HttpSession session) {
		Transfer transfer = transferService.findTransferDetails(transferId);

		if (transfer == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}

		if (transfer.getStatus().equals(TransferStatusEnum.IN_TRANSITION)
				|| transfer.getStatus().equals(TransferStatusEnum.CLOSED)) {
			logger.info("Transfer already shipped. " + CustomLoggerConstant.REDIRECTED_TO_MANAGER_VIEW);
			return new ModelAndView(ViewConstant.REDIRECT + "/manager/transfers");
		}
		List<BiCycle> biCycles = bicycleService.findBicyclesForShipment(transfer);
		session.setAttribute(ModelAttributeConstant.BICYCLES, biCycles);

		return new ModelAndView(ViewConstant.TRANSFER_CONFIRM, ModelAttributeConstant.TRANSFER, transfer);
	}

	/**
	 * This method is used to map requests for confirming transfer from a pickup
	 * point. Renders the transfers view.
	 * 
	 * @param transferDataDTO
	 *            the transfer details
	 * @param bindingResult
	 *            for validating incoming data
	 * @param session
	 *            for session attributes
	 * @return transfers view
	 */
	@RequestMapping(value = "manager/confirmShipment", method = RequestMethod.POST)
	public ModelAndView confirmShipment(@Valid @ModelAttribute("transferData") TransferDataDTO transferDataDTO,
			BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.REDIRECT + "/manager/sendShipment/" + transferDataDTO.getTransferId());
		}
		transferService.confirmTransfer(transferDataDTO, session);
		return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.TRANSFERS);
	}

	/**
	 * This method is used to map requests for receiving a transfer shipment.
	 * Renders the receiveConfirm view.
	 * 
	 * @param transferId
	 *            id of the transfer record
	 * @param session
	 *            for session attributes
	 * @return receiveConfirm view
	 */
	@PostAuthorize("@currentUserService.canAccessManagerReceiver(principal, #transferId)")
	@RequestMapping(value = "manager/receiveShipment/{id}", method = RequestMethod.GET)
	public ModelAndView receiveShipment(@PathVariable("id") Long transferId, HttpSession session) {
		Transfer transfer = transferService.findTransferDetails(transferId);

		if (transfer == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}

		if (transfer.getStatus().equals(TransferStatusEnum.PENDING)
				|| transfer.getStatus().equals(TransferStatusEnum.CLOSED)) {
			logger.info(
					"Transfer not yet shipped or already received. " + CustomLoggerConstant.REDIRECTED_TO_MANAGER_VIEW);
			return new ModelAndView(ViewConstant.REDIRECT + "/manager/transfers");
		}
		List<BiCycle> biCycles = biCycleTransferService.findBicyclesInTransition(transfer);
		session.setAttribute(ModelAttributeConstant.BICYCLES, biCycles);
		return new ModelAndView(ViewConstant.RECEIVE_CONFIRM, ModelAttributeConstant.TRANSFER, transfer);
	}

	/**
	 * This method is used to map the requests for confirming delivery of
	 * shipment. Renders the transfers view.
	 * 
	 * @param transferId
	 *            id of the transfer record
	 * @param session
	 *            for session attributes
	 * @param model
	 *            to map model attributes
	 * @return transfers view
	 */
	@PostAuthorize("@currentUserService.canAccessManagerReceiver(principal, #transferId)")
	@RequestMapping(value = "manager/confirmShipmentReceive/{id}", method = RequestMethod.GET)
	public ModelAndView confirmShipmentReceive(@PathVariable("id") Long transferId, HttpSession session, Model model) {
		Transfer transfer = transferService.findTransferDetails(transferId);

		if (transfer == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}

		if (transfer.getStatus().equals(TransferStatusEnum.PENDING)
				|| transfer.getStatus().equals(TransferStatusEnum.CLOSED)) {
			logger.info(
					"Transfer not yet shipped or already received. " + CustomLoggerConstant.REDIRECTED_TO_MANAGER_VIEW);
			return new ModelAndView(ViewConstant.REDIRECT + "/manager/transfers");
		}
		Transfer closedTransfer = transferService.confirmReceiveTransfer(transferId, session);

		if (null == closedTransfer) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			model.addAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Error Receiving Transfer!!");
			return new ModelAndView("receiveShipment/" + transferId);
		} else {
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
			return new ModelAndView("redirect:/manager/transfers");
		}
	}

	/**
	 * This method is used to map requests for viewing approved requests.
	 * 
	 * @return approvedRequests view
	 */
	@RequestMapping(value = { "/admin/approvedRequests", "/manager/approvedRequests" }, method = RequestMethod.GET)
	public ModelAndView approvedRequests() {
		return new ModelAndView(ViewConstant.APPROVED_REQUESTS);
	}

	/**
	 * This method is used to map requests for viewing all closed transfers.
	 * 
	 * @return closedTransfers view
	 */
	@RequestMapping(value = "admin/transfers", method = RequestMethod.GET)
	public ModelAndView closedTransfers() {
		return new ModelAndView(ViewConstant.CLOSED_TRANSFERS);
	}

	/**
	 * This method is used to map requests for fetching bicycle details of a
	 * transfer.
	 * 
	 * @param id
	 *            transfer id
	 * @return {@link BiCycle} List
	 */
	@RequestMapping(value = "admin/viewTransfer/{id}", method = RequestMethod.GET)
	public @ResponseBody List<BiCycle> getTransferedBicycles(@PathVariable Long id) {
		Transfer transfer = transferService.findTransferDetails(id);
		return biCycleTransferService.findBicyclesInTransition(transfer);
	}
}
