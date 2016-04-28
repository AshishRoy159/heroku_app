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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
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
import com.mindfire.bicyclesharing.dto.BookingPaymentDTO;
import com.mindfire.bicyclesharing.dto.IssueCycleDTO;
import com.mindfire.bicyclesharing.dto.ReceiveBicyclePaymentDTO;
import com.mindfire.bicyclesharing.dto.ReceiveCycleDTO;
import com.mindfire.bicyclesharing.event.BookingSuccessEvent;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.PickUpPointManager;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.security.CurrentUser;
import com.mindfire.bicyclesharing.service.BiCycleService;
import com.mindfire.bicyclesharing.service.BookingService;
import com.mindfire.bicyclesharing.service.PickUpPointManagerService;
import com.mindfire.bicyclesharing.service.PickUpPointService;
import com.mindfire.bicyclesharing.service.RateGroupService;
import com.mindfire.bicyclesharing.service.UserService;
import com.mindfire.bicyclesharing.service.WalletService;

/**
 * BookingController contains all the mappings related to the Booking.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class BookingController {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private UserService userService;

	@Autowired
	private PickUpPointService pickUpPointService;

	@Autowired
	private PickUpPointManagerService pickUpPointManagerService;

	@Autowired
	private RateGroupService rateGroupService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private BiCycleService biCycleService;

	@Autowired
	private WalletService walletService;

	/**
	 * This method is used to map the booking of the bicycles request. Simply
	 * render the booking view
	 * 
	 * @param model
	 *            to map the model attributes
	 * @param authentication
	 *            to get current logged in user details
	 * @return booking view
	 */
	@PostAuthorize("hasAuthority('MANAGER')")
	@RequestMapping(value = { "/manager/booking" }, method = RequestMethod.GET)
	public ModelAndView getBookingView(Model model, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		User manager = userService.userDetails(currentUser.getUserId());
		PickUpPointManager pickUpPoint = pickUpPointManagerService.getPickupPointManager(manager);
		List<BiCycle> biCycles = biCycleService.getBicyclesByPickupPointAndAvailability(pickUpPoint.getPickUpPoint(),
				true);
		model.addAttribute(ModelAttributeConstant.BICYCLES, biCycles);
		return new ModelAndView(ViewConstant.BOOKING);
	}

	/**
	 * This method is used to map the issueCycle request and send the data to
	 * booking service. Simply render the payment view
	 * 
	 * @param redirectAttributes
	 *            this is used for showing message on the view.
	 * @param bookingPaymentDTO
	 *            for receiving in coming data from view
	 * @param bindingResult
	 *            for validating incoming data
	 * @param auth
	 *            this is used for retrieving the currentUser
	 * @param session
	 *            for session data
	 * @return payment or booking view
	 */
	@RequestMapping(value = { "/manager/issueCyclePayment" }, method = RequestMethod.POST)
	public ModelAndView issueBicycle(RedirectAttributes redirectAttributes,
			@ModelAttribute("issueCyclePaymentData") BookingPaymentDTO bookingPaymentDTO, BindingResult bindingResult,
			Authentication auth, HttpSession session) {

		if (bindingResult.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.BOOKING);
		}
		CurrentUser currentUser = (CurrentUser) auth.getPrincipal();
		User user = userService.userDetails(bookingPaymentDTO.getUserId());
		Booking existingBooking = bookingService.getBookingDetails(true, user);

		if (null != existingBooking) {
			logger.info("Open booking exists. Transaction cancelled.");
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
					"Oops..!! Your booking status is open..!");
			return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
		}
		Booking booking = bookingService.addNewBooking(auth, bookingPaymentDTO, session);

		if (null == booking) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
					"Oops..!! You do not have sufficient balance");
			return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
		} else {
			biCycleService.updateBiCycleDetails(bookingPaymentDTO.getBicycleId());
			PickUpPoint pickUpPoint = pickUpPointManagerService.getPickupPointManager(currentUser.getUser())
					.getPickUpPoint();
			pickUpPointService.updatePickUpPointAvailability(pickUpPoint,
					biCycleService.getBicyclesByPickupPointAndAvailability(pickUpPoint, true).size());

			try {
				eventPublisher.publishEvent(new BookingSuccessEvent(user, booking));
			} catch (Exception me) {
				System.out.println(me.getMessage());
			}
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_DETAILS, booking);
			return new ModelAndView("redirect:/manager/printIssueBicycleDetails");
		}
	}

	/**
	 * This method is used to map the bookingPayment request and simply render
	 * the booking payment view related to current booking of the user.
	 * 
	 * @param issueCycleDTO
	 *            IssueCycleDTO object for issue BiCycle data
	 * @param result
	 *            for validating incoming data
	 * @param session
	 *            HttpSession object is used for holding the required value into
	 *            the session's variables.
	 * @param redirectAttributes
	 *            to map model attributes
	 * @param model
	 *            to map model attributes
	 * @return bookingPayment or booking view.
	 */
	@RequestMapping(value = { "/manager/bookingPayment" }, method = RequestMethod.GET)
	public ModelAndView bookingPayment(@Valid @ModelAttribute("issueCycleData") IssueCycleDTO issueCycleDTO,
			BindingResult result, HttpSession session, RedirectAttributes redirectAttributes, Model model) {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			redirectAttributes.addFlashAttribute("issueCycleErrorMessage", "Please enter valid data !");
			return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);

		} else {
			redirectAttributes.addFlashAttribute("issueCycleData", issueCycleDTO);
			Calendar cal = bookingService.calculateExpectedIn(issueCycleDTO);
			session.setAttribute("expectedIn", new Timestamp(cal.getTimeInMillis()));
			User user = userService.userDetails(issueCycleDTO.getUserId());

			if (null == user) {
				logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE, "Invalid UserId...");
				return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
			}

			Double fare = bookingService.calculateFare(user, issueCycleDTO.getExpectedInTime());
			Double discount = bookingService.calculateDiscount(user, fare);
			fare = fare - discount;
			model.addAttribute(ModelAttributeConstant.BASE_FARE, Math.ceil(fare));

			if (user.getIsApproved() && user.getEnabled()) {
				logger.info("User approved for booking.");
				return new ModelAndView(ViewConstant.BOOKING_PAYMENT);
			} else {
				logger.info("User not approved for booking. Transaction cancelled.");
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
						"It seems your verification is still pending. you need to get approved to book a bicycle.");
				return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
			}
		}
	}

	/**
	 * This method returns Payment view to the manager if the user has not
	 * returned his bicycle in time. An extra 10% charge is added per hour.
	 * 
	 * @param receiveCycleDTO
	 *            receive booking id
	 * @param result
	 *            for validating incoming data
	 * @param redirectAttributes
	 *            to map model attributes
	 * @return Payment View.
	 */
	@RequestMapping(value = "/manager/receiveCycle", method = RequestMethod.POST)
	public ModelAndView receiveBicyclePayment(
			@Valid @ModelAttribute("receiveCycleData") ReceiveCycleDTO receiveCycleDTO, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE, "Enter a valid Booking Id");
			return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
		}
		Booking booking = bookingService.getBookingById(receiveCycleDTO.getBookingId());

		if (null == booking) {
			logger.info("Booking doesn't exist. Transaction cancelled.");
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
					"Your Booking Id is Incorrect!!");
			return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
		} else {
			logger.info("Booking details are valid");

			if (null != booking.getBiCycleId()) {
				logger.info("Booking has been issued.");
				if (booking.getIsOpen()) {
					logger.info("Booking is open.");
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_DETAILS, booking);
					long time = bookingService.calculateRidingTime(booking);
					long actualTime = time - 600000; // we are giving 10 minute
														// relaxation.
					if (actualTime <= 0) {
						logger.info("Bicycle returned within expected time.");
						redirectAttributes.addFlashAttribute("currentTime", new Timestamp(new Date().getTime()));
						return new ModelAndView("redirect:/manager/receiveBicycle");
					} else {
						logger.info("Bicycle was not returned within expected time.");

						long hour = bookingService.calculateTotalRideTime(actualTime);
						double baseRate = rateGroupService.getBaseRate(booking.getUser()).getBaseRateBean()
								.getBaseRate();
						double discount = rateGroupService.getBaseRate(booking.getUser()).getDiscount();
						double fare = bookingService.calculateActualFare(hour, baseRate, discount);
						redirectAttributes.addFlashAttribute("fare", Math.ceil(fare));

						return new ModelAndView("redirect:/manager/receiveBicyclePayment");
					}
				} else {
					logger.info("Booking has already been closed. Transaction closed.");
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
							"Your Booking is already received!!");
					return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
				}
			} else {
				logger.info("This booking has not been used. Transaction cancelled.");
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
						"You have not been assigned any bicycle on this booking Id!!");
				return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
			}
		}
	}

	/**
	 * This method returns Booking Details view to the manager if the user has
	 * returned his bicycle in time.
	 * 
	 * @param receiveCycleDTO
	 *            receive booking id
	 * @param bindingResult
	 *            for validating incoming data
	 * @param auth
	 *            to get current logged in user details
	 * @param redirectAttributes
	 *            to map model attributes
	 * @return Booking Details View
	 */
	@RequestMapping(value = "/manager/receiveBicycle", method = RequestMethod.POST)
	public ModelAndView receiveBicycleDetails(
			@Valid @ModelAttribute("receiveCycleData") ReceiveCycleDTO receiveCycleDTO, BindingResult bindingResult,
			Authentication auth, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
		}
		double fare = 0.0;
		Booking book = bookingService.getBookingById(receiveCycleDTO.getBookingId());

		if (null == book) {
			logger.info("Booking doesn't exist. Transaction cancelled");
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
					"Your Booking Id is Incorrect!!");
		} else {
			logger.info("Booking data is valid.");
			if (book.getIsOpen()) {
				logger.info("Booking will be closed.");
				Booking booking = bookingService.receiveBicycle(receiveCycleDTO.getBookingId(), fare, auth);

				if (null == booking) {
					logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
							"Your receive request cannot be processed. Try again Later!!");
				} else {
					logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_SUCCESS,
							"Your receive request processed successfully!!");
				}
			} else {
				logger.info("Booking has already been received. Transaction cancelled.");
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
						"Your Booking is already received!!");
			}
		}
		return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
	}

	/**
	 * This method is executed to update the booking details on return of the
	 * bicycle. A wallet transaction is also created since the user has to pay
	 * for using the service over specified time.
	 * 
	 * @param receiveBicyclePaymentDTO
	 *            payment amount and mode
	 * @param bindingResult
	 *            for validating incoming data
	 * @param auth
	 *            to get current logged in user details
	 * @param redirectAttributes
	 *            to map model attributes
	 * @return Booking View
	 */
	@RequestMapping(value = "/manager/receiveCyclePayment", method = RequestMethod.POST)
	public ModelAndView paymentOnBicycleReceive(
			@ModelAttribute("receiveCyclePaymentData") ReceiveBicyclePaymentDTO receiveBicyclePaymentDTO,
			BindingResult bindingResult, Authentication auth, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
		}
		Booking book = bookingService.getBookingById(receiveBicyclePaymentDTO.getBookingId());

		if (null == book) {
			logger.info("Booking doesn't exist. Transaction cancelled.");
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
					"Your Booking Id is Incorrect!!");
		} else {
			logger.info("Booking details are valid.");
			Wallet wallet = walletService.getWallet(book.getUser());
			WalletTransaction transaction = bookingService.saveReceiveBicyclePayment(receiveBicyclePaymentDTO, wallet);

			if (null == transaction) {
				logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
						"Your receive request cannot be processed due to low balance. Try again Later!!");
			} else {
				logger.info("Payment successful.");
				Booking booking = bookingService.receiveBicycle(receiveBicyclePaymentDTO.getBookingId(),
						receiveBicyclePaymentDTO.getFare(), auth);

				if (null == booking) {
					logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
							"Your receive request cannot be processed. Try again Later!!");
				} else {
					logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_SUCCESS,
							"Your receive request processed successfully!!");
				}
			}
		}
		return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
	}

	/**
	 * This method is used to map the receive bicycle along with payment request
	 * and simply render the receiveBicyclePayment view related to current
	 * booking of the user.
	 * 
	 * @return receiveBicyclePayment view
	 */
	@RequestMapping(value = "/manager/receiveBicyclePayment", method = RequestMethod.GET)
	public ModelAndView receiveBicyclePayment() {
		return new ModelAndView(ViewConstant.RECEIVE_BICYCLE_PAYMENT);
	}

	/**
	 * This method is used to map the receive bicycle and simply render the
	 * receiveBicycle view related to current booking of the user.
	 * 
	 * @return receiveBicycle view
	 */
	@RequestMapping(value = "/manager/receiveBicycle", method = RequestMethod.GET)
	public ModelAndView receiveBicycle() {
		return new ModelAndView(ViewConstant.RECEIVE_BICYCLE);
	}

	/**
	 * This method is used to map the close booking request
	 * 
	 * @param receiveCycleDTO
	 *            this parameter holds the receive bicycle data
	 * @param result
	 *            for validating incoming data
	 * @param redirectAttributes
	 *            to map model attributes
	 * @return booking view
	 */
	@RequestMapping(value = { "/manager/closeBooking" }, method = RequestMethod.POST)
	public @ResponseBody String closeCurrentBooking(@Valid @ModelAttribute ReceiveCycleDTO receiveCycleDTO,
			BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return "Invalid Booking Id";
		}

		Booking booking = bookingService.getBookingById(receiveCycleDTO.getBookingId());

		if (null != booking) {
			logger.info("Booking data is valid");

			if (!booking.getIsOpen()) {
				logger.info("This booking is already closed.");
				return "This booking has already been closed";
			} else if (null != booking.getBiCycleId()) {
				logger.info("Issued bicycle has not been returned yet. Transaction cancelled.");
				return "You have been issued a bicycle. Please return bicycle before closing the booking..!";
			} else {
				logger.info("Bicycle has not been issued for this booking");

				if (null != bookingService.closeBooking(booking)) {
					logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
					return "Your booking has been successfully closed";
				} else {
					logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
					return "Your booking status is not valid..";
				}
			}
		} else {
			logger.info("Booking doesn't exist. Transaction cancelled");
			return "Your booking status is not valid..";
		}
	}

	/**
	 * This method is used to map the request for view the current running
	 * bookings and simply render the view along with the result.
	 * 
	 * @return bookingInUse view.
	 */
	@RequestMapping(value = { "admin/bookingInUse", "manager/bookingInUse" }, method = RequestMethod.GET)
	public ModelAndView runningBookings() {
		List<Booking> runningBookings = bookingService.getRunningBooking(true);
		return new ModelAndView(ViewConstant.RUNNING_BOOKING, ModelAttributeConstant.BOOKINGS, runningBookings);
	}
}
