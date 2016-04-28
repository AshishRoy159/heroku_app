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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.constant.CustomLoggerConstant;
import com.mindfire.bicyclesharing.constant.ModelAttributeConstant;
import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.dto.IssueCycleForOnlineDTO;
import com.mindfire.bicyclesharing.dto.PaymentAtPickUpPointDTO;
import com.mindfire.bicyclesharing.dto.UserBookingDTO;
import com.mindfire.bicyclesharing.dto.UserBookingPaymentDTO;
import com.mindfire.bicyclesharing.event.BookingSuccessEvent;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.security.CurrentUser;
import com.mindfire.bicyclesharing.service.BookingService;
import com.mindfire.bicyclesharing.service.PickUpPointManagerService;
import com.mindfire.bicyclesharing.service.PickUpPointService;
import com.mindfire.bicyclesharing.service.RateGroupService;
import com.mindfire.bicyclesharing.service.UserService;
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

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private BookingService bookingSevice;

	@Autowired
	private RateGroupService rateGroupService;

	@Autowired
	private UserService userService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private PickUpPointManagerService pickupPointManagerService;

	@Autowired
	private PickUpPointService pickUpPointService;

	/**
	 * This method maps the user booking requests and send the control and data
	 * to the corresponding classes
	 * 
	 * @param userBookingDTO
	 *            this parameter holds the user booking data.
	 * @param bindingResult
	 *            for validating incoming data
	 * @param redirectAttributes
	 *            this object redirect the required messages to the views.
	 * @param authentication
	 *            this object is used to retrieve the current user.
	 * @param session
	 *            to set session attribute
	 * @return index or user payment view
	 */
	@RequestMapping(value = "/user/booking", method = RequestMethod.POST)
	public ModelAndView userBooking(@Valid @ModelAttribute("userBookingData") UserBookingDTO userBookingDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes, Authentication authentication,
			HttpSession session) {

		if (bindingResult.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Invalid booking data");
			return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.INDEX);
		}

		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Timestamp bookingTime = Timestamp.valueOf(userBookingDTO.getBookingTime().replace("/", "-").concat(":00.000"));
		Timestamp returnTime = Timestamp.valueOf(userBookingDTO.getReturnTime().replace("/", "-").concat(":00.000"));

		if (currentUser.getUser().getIsApproved()) {
			if (bookingTime.after(returnTime) || bookingTime.before(new Timestamp(System.currentTimeMillis()))) {
				logger.info("Invalid booking date and time was selected. Transaction Cancelled.");
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
						"Please Enter Valid booking date and time..!!");
			} else {
				logger.info("Valid booking credentials.");
				Booking existingBooking = bookingSevice.getBookingDetails(true, currentUser.getUser());

				if (null == existingBooking) {
					logger.info("User doesn't have existing open bookings.");
					session.setAttribute("bookingData", userBookingDTO);
					logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_SUCCESS,
							"Your Booking is successfully completed..Please Choose your payment.");

					long actualTime = (returnTime.getTime() - bookingTime.getTime());
					long hour = bookingSevice.calculateTotalRideTime(actualTime);
					double baseRate = rateGroupService.getBaseRate(currentUser.getUser()).getBaseRateBean()
							.getBaseRate();
					double fare = bookingSevice.calculateFare(currentUser.getUser(), hour);
					double discount = bookingSevice.calculateDiscount(currentUser.getUser(), fare);

					if (fare == 0.0) {
						logger.info("Calculated fare is 0. Fare is set to baserate.");
						fare = baseRate - discount;
					} else {
						fare = fare - discount;
					}
					redirectAttributes.addFlashAttribute("fare", Math.ceil(fare));
					redirectAttributes.addFlashAttribute("userBookingDetails", userBookingDTO);
					redirectAttributes.addFlashAttribute("pickUpPoint",
							pickUpPointService.getPickupPointById(userBookingDTO.getPickUpPoint()));
					return new ModelAndView("redirect:/user/userPayment");
				} else {
					logger.info("User has an existing open booking. Transaction cancelled.");
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
							"You have a boooking still open!!");
				}
			}
		} else {
			logger.info("User is not approved for booking. Transaction cancelled.");
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Your account is not approved.");
		}
		return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.INDEX);
	}

	/**
	 * This method maps the request for payment page for user and simply render
	 * the view.
	 * 
	 * @return userBookingPayment view
	 */
	@RequestMapping(value = "/user/userPayment", method = RequestMethod.GET)
	public ModelAndView userPayment() {
		return new ModelAndView(ViewConstant.USER_BOOKING_PAYMENT);
	}

	/**
	 * This method is used for saving the user booking payment details.
	 * 
	 * @param userBookingPaymentDTO
	 *            this parameter holds user booking payment data
	 * @param bindingResult
	 *            for validating incoming data
	 * @param authentication
	 *            this is used for retrieving current user
	 * @param redirectAttributes
	 *            for displaying specific messages
	 * @param session
	 *            to get session attribute
	 * @return printUserBookingDetails view
	 */
	@RequestMapping(value = "/user/userBookingPayment", method = RequestMethod.POST)
	public ModelAndView userBookingPayment(
			@Valid @ModelAttribute("userBookingPaymentData") UserBookingPaymentDTO userBookingPaymentDTO,
			BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes,
			HttpSession session) {

		if (bindingResult.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.USER_BOOKING_PAYMENT);
		}

		UserBookingDTO userBookingDTO = (UserBookingDTO) session.getAttribute("bookingData");
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Booking booking = bookingSevice.saveUserBookingDetails(userBookingDTO, authentication);

		if (userBookingPaymentDTO.getMode().equals("cash")) {
			logger.info("Payment will be by cash at pickup point.");
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_DETAILS, booking);

			try {
				eventPublisher.publishEvent(new BookingSuccessEvent(currentUser.getUser(), booking));
			} catch (Exception me) {
				logger.error(me.getMessage());
			}
			return new ModelAndView("redirect:/user/printUserBookingDetails");
		} else {
			logger.info("Payment will be from wallet.");
			WalletTransaction walletTransaction = walletService.saveUserBookingPayment(userBookingPaymentDTO, booking,
					authentication);

			if (null == walletTransaction) {
				logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
						"Your Payment is Not successfully completed Due to low balance");
				return new ModelAndView(ViewConstant.USER_BOOKING_PAYMENT);
			} else {
				logger.info("Successful booking and payment.");

				try {
					eventPublisher.publishEvent(new BookingSuccessEvent(currentUser.getUser(), booking));
				} catch (Exception me) {
					System.out.println(me.getMessage());
				}
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.MESSAGE,
						"Your Payment is successfully completed");
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_DETAILS, booking);
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
		return new ModelAndView(ViewConstant.PRINT_BOOKING_DETAILS);
	}

	/**
	 * This method maps the request for printing issue bicycle details and
	 * simply renders the view.
	 * 
	 * @return printIssueBicycleDetails view
	 */
	@RequestMapping(value = "/manager/printIssueBicycleDetails", method = RequestMethod.GET)
	public ModelAndView printDetails() {
		return new ModelAndView(ViewConstant.PRINT_ISSUE_BICYCLE_DETAILS);
	}

	/**
	 * This method maps the request for issuing bicycle and sends the control
	 * and data to the corresponding classes.
	 * 
	 * @param issueCycleForOnlineDTO
	 *            this object holds issue bicycle related data
	 * @param result
	 *            for validating incoming data
	 * @param redirectAttributes
	 *            used for displaying required messages
	 * @param authentication
	 *            used for retrieving current user
	 * @return booking or printIssueBicycleDetails views
	 */
	@RequestMapping(value = "/manager/printBookingDetails", method = RequestMethod.POST)
	public ModelAndView issueOnlineBookingBicycle(
			@Valid @ModelAttribute("issueBookingData") IssueCycleForOnlineDTO issueCycleForOnlineDTO,
			BindingResult result, RedirectAttributes redirectAttributes, Authentication authentication) {

		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE, "Invalid data !!");
			return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
		} else {
			CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
			Booking booking = bookingSevice.getBookingById(issueCycleForOnlineDTO.getBookingId());

			if (null == booking) {
				logger.error(CustomLoggerConstant.TRANSACTION_FAILED);
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE, "Invalid data !!");
				return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
			}
			Booking openBooking = bookingSevice.getBookingDetails(true, booking.getUser());

			if (null == openBooking) {
				logger.info("Booking doesn't exist. Transaction cancelled.");
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
						"Your Booking Id is not valid !!");
			} else {
				logger.info("Booking details are valid.");

				// giving 30(1800000 millisecond) minute relaxation for taking
				// bicycle before booking time.
				if (!openBooking.getExpectedOut().before(new Timestamp(System.currentTimeMillis() + 1800000))
						|| !openBooking.getExpectedIn().after(new Timestamp(System.currentTimeMillis()))) {
					logger.info("User is too early. Transaction cancelled.");
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
							"Your expected out time is:-" + openBooking.getExpectedOut());
				} else {
					logger.info("User is on expected time.");

					if (openBooking.getPickedUpFrom().getPickUpPointId() != pickupPointManagerService
							.getPickupPointManager(currentUser.getUser()).getPickUpPoint().getPickUpPointId()) {
						logger.info("User is not at the same pickup point as booking.");
						redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
								"Your PickUp place is Not valid..!");
					} else {
						if (openBooking.getBiCycleId() == null) {
							logger.info("This booking has not been issued a bicycle.");

							if (openBooking.getFare() == 0) {
								logger.info("Payment for this booking is due");
								long actualTime = (openBooking.getExpectedIn().getTime()
										- openBooking.getExpectedOut().getTime());
								long hour = bookingSevice.calculateTotalRideTime(actualTime);
								double baseRate = rateGroupService.getBaseRate(openBooking.getUser()).getBaseRateBean()
										.getBaseRate();
								double fare = bookingSevice.calculateFare(openBooking.getUser(), hour);
								double discount = bookingSevice.calculateDiscount(openBooking.getUser(), fare);

								if (fare == 0.0) {
									fare = baseRate - discount;
									logger.info("Calculted fare is 0. Fare is set to base rate.");
								} else {
									logger.info("Fare calculated as per discount.");
									fare = fare - discount;
								}
								redirectAttributes.addFlashAttribute("fare", Math.ceil(fare));
								redirectAttributes.addFlashAttribute("bicycleId",
										issueCycleForOnlineDTO.getBicycleId());
								redirectAttributes.addFlashAttribute("bookingStatus", openBooking);
								logger.info("Redirected to payment page.");
								return new ModelAndView("redirect:/manager/paymentAtPickupPoint");
							} else {
								logger.info("User have already paid the fare amount.");
								Booking bookingDetails = bookingSevice.updateIssueBicycleDetails(issueCycleForOnlineDTO,
										openBooking.getFare());

								if (null == bookingDetails) {
									logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
									return new ModelAndView(ViewConstant.BOOKING,
											ModelAttributeConstant.BOOKING_FAILURE,
											"Sorry ..!! Your Operation Failed.");
								} else {
									logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
									redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_DETAILS,
											bookingDetails);
									return new ModelAndView("redirect:/manager/printIssueBicycleDetails");
								}
							}
						} else {
							logger.info("A bicycle has already been issued for this bicycle. Transaction cancelled.");
							redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
									"You already taken bicycle on this booking Id:- " + openBooking.getBookingId());
						}
					}
				}
			}
		}
		return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
	}

	/**
	 * This method maps the request for payment at pickup point and simply
	 * render the view.
	 * 
	 * @return paymentAtPickupPoint view
	 */
	@RequestMapping(value = "/manager/paymentAtPickupPoint", method = RequestMethod.GET)
	public ModelAndView paymentAtPickupPoint() {
		return new ModelAndView(ViewConstant.PAYMENT_AT_PICKUP_POINT);
	}

	/**
	 * This method maps the request for updating the booking details and
	 * printing the required receipt.
	 * 
	 * @param paymentAtPickUpPointDTO
	 *            this object holds the payment at pickup point related data.
	 * @param bindingResult
	 *            for validating incoming data
	 * @param redirectAttributes
	 *            this is used for retrieving current user..
	 * @return booking or printIssueBicycleDetails view
	 */
	@RequestMapping(value = "/manager/printBookingReceipt", method = RequestMethod.POST)
	public ModelAndView printBookingDetails(
			@Valid @ModelAttribute("bookingPaymentData") PaymentAtPickUpPointDTO paymentAtPickUpPointDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			logger.error(CustomLoggerConstant.BINDING_RESULT_HAS_ERRORS);
			return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
		}
		Booking bookingDetails = bookingSevice.updateIssueBicycleDetailsWithPayment(paymentAtPickUpPointDTO);

		if (null == bookingDetails) {
			logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
					"Sorry ..!! Your Operation Failed.");
			return new ModelAndView(ViewConstant.BOOKING);
		} else {
			logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
			bookingSevice.createUserPaymentTransaction(paymentAtPickUpPointDTO);
			redirectAttributes.addFlashAttribute("bookingDetails", bookingDetails);
			return new ModelAndView("redirect:/manager/printIssueBicycleDetails");
		}
	}

	/**
	 * This method is used to map the requests for viewing a user's booking
	 * history. Renders the userBookingHistory view.
	 * 
	 * @param model
	 *            to map the model attributes
	 * @param id
	 *            user's id
	 * @return userBookingHistory view
	 */
	@PostAuthorize("@currentUserService.canAccessUser(principal, #id)")
	@RequestMapping(value = "/user/bookingHistory/{id}", method = RequestMethod.GET)
	public ModelAndView userBookingHistory(Model model, @PathVariable("id") Long id) {
		User user = userService.userDetails(id);

		if (user == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		model.addAttribute(ModelAttributeConstant.USER, user);
		return new ModelAndView(ViewConstant.USER_BOOKIG_HISTORY);
	}

	/**
	 * This method is used to map the request current user booking and simply
	 * render the view along with the data.
	 * 
	 * @param model
	 *            to map the model attributes
	 * @param id
	 *            user id
	 * @return currentBooking view.
	 */
	@RequestMapping(value = "/user/currentBooking/{id}", method = RequestMethod.GET)
	public ModelAndView currentUserBooking(Model model, @PathVariable("id") Long id) {
		User user = userService.userDetails(id);

		if (user == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		model.addAttribute(ModelAttributeConstant.USER, user);
		model.addAttribute("currentBooking", bookingSevice.getBookingDetails(true, user));
		return new ModelAndView(ViewConstant.CURRENT_USER_BOOKING);
	}

	/**
	 * This method is used to map the request for close current booking and
	 * simply render the view currentBooking
	 * 
	 * @param bookingId
	 *            this parameter holds bookingId
	 * @param redirectAttributes
	 *            to map the redirectAttributes
	 * @return currentBooking view.
	 */
	@RequestMapping(value = { "/user/closeCurrentBooking/{id}" }, method = RequestMethod.GET)
	public ModelAndView closeCurrentBooking(@PathVariable("id") Long bookingId, RedirectAttributes redirectAttributes) {
		Booking booking = bookingSevice.getBookingById(bookingId);

		if (null != booking) {
			logger.info("Booking data is valid.");

			if (null != booking.getBiCycleId()) {
				logger.info("Issued bicycle has not been returned yet. Transaction cancelled.");
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.CLOSE_MESSAGE,
						"You have been issued a bicycle. Please return bicycle before closing the booking..!");
			} else {
				logger.info("Bicycle has not been issued for this booking.");

				if (null != bookingSevice.closeBooking(booking)) {
					logger.info(CustomLoggerConstant.TRANSACTION_COMPLETE);
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.CLOSE_MESSAGE,
							"Your booking has been successfully closed.");
				} else {
					logger.info(CustomLoggerConstant.TRANSACTION_FAILED);
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.CLOSE_MESSAGE,
							"Your booking status is not valid..");
				}
			}
		} else {
			logger.info("Booking doesn't exist. Transaction cancelled.");
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.CLOSE_MESSAGE,
					"Your booking status is not valid..");
		}
		return new ModelAndView(ViewConstant.REDIRECT + "/user/" + ViewConstant.CURRENT_USER_BOOKING + "/"
				+ booking.getUser().getUserId());
	}
}
