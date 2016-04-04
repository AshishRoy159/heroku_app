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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.dto.IssueCycleForOnlineDTO;
import com.mindfire.bicyclesharing.dto.PaymentAtPickUpPointDTO;
import com.mindfire.bicyclesharing.dto.UserBookingDTO;
import com.mindfire.bicyclesharing.dto.UserBookingPaymentDTO;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.BookingRepository;
import com.mindfire.bicyclesharing.repository.RateGroupRepository;
import com.mindfire.bicyclesharing.repository.UserRepository;
import com.mindfire.bicyclesharing.service.BookingService;
import com.mindfire.bicyclesharing.service.PickUpPointManagerService;
import com.mindfire.bicyclesharing.service.WalletService;

/**
 * UserBookingController class is used for the mappings related requests for the
 * user's booking.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */

@Controller
public class UserBookingController {

	@Autowired
	private BookingService bookingSevice;

	@Autowired
	private RateGroupRepository rateGroupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WalletService walletService;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private PickUpPointManagerService pickupPointManagerService;

	/**
	 * This method maps the user booking requests and send the control and data
	 * to the corresponding classes
	 * 
	 * @param userBookingDTO
	 *            this parameter holds the user booking data.
	 * @param redirectAttributes
	 *            this object redirect the required messages to the views.
	 * @param authentication
	 *            this object is used to retrieve the current user.
	 * @param model
	 * @return index or user payment view
	 */
	@RequestMapping(value = "/user/booking", method = RequestMethod.POST)
	public ModelAndView userBooking(@ModelAttribute("userBookingData") UserBookingDTO userBookingDTO,
			RedirectAttributes redirectAttributes, Authentication authentication, Model model) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Booking existing = bookingRepository.findByUserAndIsOpen(currentUser.getUser(), true);
		if (null == existing) {
			Booking userBooking = bookingSevice.saveUserBookingDetails(userBookingDTO, authentication);
			if (null == userBooking) {
				redirectAttributes.addFlashAttribute("errorMessage", "oops..!! Booking Failed.");
				return new ModelAndView("redirect:/index");
			} else {
				redirectAttributes.addFlashAttribute("bookingSuccess",
						"Your Booking is successfully completed..Please Choose your payment.");
				long actualTime = (userBooking.getExpectedIn().getTime() - userBooking.getExpectedOut().getTime());
				long hour = (actualTime / (60 * 1000)) / 60;
				double baseRate = rateGroupRepository.findByGroupType(
						userRepository.findByUserId(userBooking.getUser().getUserId()).getRateGroup().getGroupType())
						.getBaseRate();
				double fare = (hour * baseRate);
				model.addAttribute("fare", fare);
				return new ModelAndView("userBookingPayment", "userBookingDetails", userBooking);
			}
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "You have a boooking still open!!");
			return new ModelAndView("redirect:/index");
		}

	}

	/**
	 * This method is used for saving the user booking payment details.
	 * 
	 * @param userBookingPaymentDTO
	 *            this parameter holds user booking payment data
	 * @param authentication
	 *            this is used for retrieving current user
	 * @param redirectAttributes
	 *            for displaying specific messages
	 * @return printUserBookingDetails view
	 */
	@RequestMapping(value = "/user/userBookingPayment", method = RequestMethod.POST)
	public ModelAndView userBookingPayment(
			@ModelAttribute("userBookingPaymentData") UserBookingPaymentDTO userBookingPaymentDTO,
			Authentication authentication, RedirectAttributes redirectAttributes) {
		if (userBookingPaymentDTO.getMode().equals("cash")) {
			redirectAttributes.addFlashAttribute("bookingDetails",
					bookingSevice.getBookingById(userBookingPaymentDTO.getBookingId()));
			return new ModelAndView("redirect:/user/printUserBookingDetails");
		} else {
			WalletTransaction walletTransaction = walletService.saveUserBookingPayment(userBookingPaymentDTO,
					authentication);
			if (null == walletTransaction) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Your Payment is Not successfully completed Due to low balance");
				return new ModelAndView("redirect:/index");
			} else {
				redirectAttributes.addFlashAttribute("message", "Your Payment is successfully completed");
				redirectAttributes.addFlashAttribute("bookingDetails",
						bookingSevice.getBookingById(userBookingPaymentDTO.getBookingId()));
				return new ModelAndView("redirect:/user/printUserBookingDetails");
			}
		}
	}

	/**
	 * This method maps the request for printing user booking receipt.
	 * 
	 * @return printUserBookingDetails view
	 */
	@RequestMapping(value = "/user/printUserBookingDetails", method = RequestMethod.GET)
	public ModelAndView printUserBookingReceipt() {
		return new ModelAndView("printUserBookingDetails");

	}

	/**
	 * This method maps the request for printing issue bicycle details and
	 * simply renders the view.
	 * 
	 * @return printIssueBicycleDetails view
	 */
	@RequestMapping(value = "/manager/printIssueBicycleDetails", method = RequestMethod.GET)
	public ModelAndView printDetails() {
		return new ModelAndView("printIssueBicycleDetails");
	}

	/**
	 * This method maps the request for issuing bicycle and sends the control
	 * and data to the corresponding classes.
	 * 
	 * @param issueCycleForOnlineDTO
	 *            this object holds issue bicycle related data
	 * @param redirectAttributes
	 *            used for displaying required messages
	 * @param authentication
	 *            used for retrieving current user
	 * @return booking or printIssueBicycleDetails views
	 */
	@RequestMapping(value = "/manager/printBookingDetails", method = RequestMethod.POST)
	public ModelAndView issueOnlineBookingBicycle(
			@ModelAttribute("issueBookingData") IssueCycleForOnlineDTO issueCycleForOnlineDTO,
			RedirectAttributes redirectAttributes, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Booking bookingStatus = bookingRepository.findByBookingId(issueCycleForOnlineDTO.getBookingId());
		if (bookingStatus.getPickedUpFrom().getPickUpPointId() != pickupPointManagerService
				.getPickupPointManager(currentUser.getUser()).getPickUpPoint().getPickUpPointId()) {
			redirectAttributes.addFlashAttribute("bookingFailure", "Your PickUp place is Not valid..!");
			return new ModelAndView("redirect:/manager/booking");
		} else {
			if (bookingStatus.getFare() == 0) {
				long actualTime = (bookingStatus.getExpectedIn().getTime() - bookingStatus.getExpectedOut().getTime());
				long hour = (actualTime / (60 * 1000)) / 60;
				double baseRate = rateGroupRepository.findByGroupType(
						userRepository.findByUserId(bookingStatus.getUser().getUserId()).getRateGroup().getGroupType())
						.getBaseRate();
				double fare = (hour * baseRate);
				redirectAttributes.addFlashAttribute("fare", fare);
				redirectAttributes.addFlashAttribute("bicycleId", issueCycleForOnlineDTO.getBicycleId());
				redirectAttributes.addFlashAttribute("bookingStatus", bookingStatus);
				return new ModelAndView("redirect:/manager/paymentAtPickupPoint");
			} else {
				Booking bookingDetails = bookingSevice.updateIssueBicycleDetails(issueCycleForOnlineDTO,
						bookingStatus.getFare());
				if (null == bookingDetails) {
					return new ModelAndView("booking", "bookingFailure", "Sorry ..!! Your Operation Failed.");
				} else {
					redirectAttributes.addFlashAttribute("bookingDetails", bookingDetails);
					return new ModelAndView("redirect:/manager/printIssueBicycleDetails");
				}
			}
		}
	}

	/**
	 * This method maps the request for payment at pickup point and simply
	 * render the view.
	 * 
	 * @return paymentAtPickupPoint view
	 */
	@RequestMapping(value = "/manager/paymentAtPickupPoint", method = RequestMethod.GET)
	public ModelAndView paymentAtPickupPoint() {
		return new ModelAndView("paymentAtPickupPoint");
	}

	/**
	 * This method maps the request for updating the booking details and
	 * printing the required receipt.
	 * 
	 * @param paymentAtPickUpPointDTO
	 *            this object holds the payment at pickup point related data.
	 * @param redirectAttributes
	 *            this is used for retrieving current user..
	 * @return booking or printIssueBicycleDetails view
	 */
	@RequestMapping(value = "/manager/printBookingReceipt", method = RequestMethod.POST)
	public ModelAndView printBookingDetails(
			@ModelAttribute("bookingPaymentData") PaymentAtPickUpPointDTO paymentAtPickUpPointDTO,
			RedirectAttributes redirectAttributes) {
		Booking bookingDetails = bookingSevice.updateIssueBicycleDetailsWithPayment(paymentAtPickUpPointDTO);
		if (null == bookingDetails) {
			redirectAttributes.addFlashAttribute("bookingFailure", "Sorry ..!! Your Operation Failed.");
			return new ModelAndView("booking");
		} else {
			bookingSevice.createUserPaymentTransaction(paymentAtPickUpPointDTO);
			redirectAttributes.addFlashAttribute("bookingDetails", bookingDetails);
			return new ModelAndView("redirect:/manager/printIssueBicycleDetails");
		}
	}
}
