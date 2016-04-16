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

import javax.validation.Valid;

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
import com.mindfire.bicyclesharing.service.RateGroupService;
import com.mindfire.bicyclesharing.service.UserService;
import com.mindfire.bicyclesharing.service.WalletService;

import javassist.NotFoundException;

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
	 * @return index or user payment view
	 */
	@RequestMapping(value = "/user/booking", method = RequestMethod.POST)
	public ModelAndView userBooking(@ModelAttribute("userBookingData") UserBookingDTO userBookingDTO,
			RedirectAttributes redirectAttributes, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Timestamp bookingTime = Timestamp.valueOf(userBookingDTO.getBookingTime().replace("/", "-").concat(":00.000"));
		Timestamp returnTime = Timestamp.valueOf(userBookingDTO.getReturnTime().replace("/", "-").concat(":00.000"));
		if (bookingTime.after(returnTime) || bookingTime.before(new Timestamp(System.currentTimeMillis()))) {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
					"Please Enter Valid booking date and time..!!");
			return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.INDEX);
		} else {
			Booking existingBooking = bookingSevice.getBookingDetails(true, currentUser.getUser());
			if (null == existingBooking) {
				Booking userBooking = bookingSevice.saveUserBookingDetails(userBookingDTO, authentication);
				if (null == userBooking) {
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
							"oops..!! Booking Failed.");
					return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.INDEX);
				} else {
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_SUCCESS,
							"Your Booking is successfully completed..Please Choose your payment.");
					long actualTime = (userBooking.getExpectedIn().getTime() - userBooking.getExpectedOut().getTime());
					long hour = bookingSevice.calculateTotalRideTime(actualTime);
					double baseRate = rateGroupService.getBaseRate(userBooking.getUser()).getBaseRateBean()
							.getBaseRate();
					double discount = rateGroupService.getBaseRate(userBooking.getUser()).getDiscount();
					double fare = bookingSevice.calculateActualFare(hour, baseRate, discount);
					if (fare == 0.0) {
						fare = baseRate - (baseRate * (discount / 100));
					}
					redirectAttributes.addFlashAttribute("fare", fare);
					redirectAttributes.addFlashAttribute("userBookingDetails", userBooking);
					return new ModelAndView("redirect:/user/userPayment");
				}
			} else {
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
						"You have a boooking still open!!");
				return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.INDEX);
			}
		}

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
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Booking booking = bookingSevice.getBookingById(userBookingPaymentDTO.getBookingId());

		if (userBookingPaymentDTO.getMode().equals("cash")) {
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_DETAILS, booking);

			try {
				eventPublisher.publishEvent(new BookingSuccessEvent(currentUser.getUser(), booking));
			} catch (Exception me) {
				System.out.println(me.getMessage());
			}

			return new ModelAndView("redirect:/user/printUserBookingDetails");
		} else {
			WalletTransaction walletTransaction = walletService.saveUserBookingPayment(userBookingPaymentDTO,
					authentication);

			if (null == walletTransaction) {
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
						"Your Payment is Not successfully completed Due to low balance");
				return new ModelAndView(ViewConstant.REDIRECT + ViewConstant.INDEX);
			} else {

				try {
					eventPublisher.publishEvent(new BookingSuccessEvent(currentUser.getUser(), booking));
				} catch (Exception me) {
					System.out.println(me.getMessage());
				}

				redirectAttributes.addFlashAttribute(ModelAttributeConstant.MESSAGE,
						"Your Payment is successfully completed");
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_DETAILS,
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
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Invalid data !!");
			return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
		} else {
			CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
			Booking booking = bookingSevice.getBookingById(issueCycleForOnlineDTO.getBookingId());
			if (null == booking) {
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE, "Invalid data !!");
				return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
			}
			Booking openBooking = bookingSevice.getBookingDetails(true, booking.getUser());
			if (null == openBooking) {
				redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
						"Your Booking Id is not valid !!");
				return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
			} else {
				// giving 30(1800000 millisecond) minute relaxation for taking
				// bicycle before booking time.
				if (!openBooking.getExpectedOut().before(new Timestamp(System.currentTimeMillis() + 1800000))
						|| !openBooking.getExpectedIn().after(new Timestamp(System.currentTimeMillis()))) {
					redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
							"Your expected out time is:-" + openBooking.getExpectedOut());
					return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
				} else {
					if (openBooking.getPickedUpFrom().getPickUpPointId() != pickupPointManagerService
							.getPickupPointManager(currentUser.getUser()).getPickUpPoint().getPickUpPointId()) {
						redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
								"Your PickUp place is Not valid..!");
						return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
					} else {
						if (openBooking.getBiCycleId() == null) {
							if (openBooking.getFare() == 0) {
								long actualTime = (openBooking.getExpectedIn().getTime()
										- openBooking.getExpectedOut().getTime());
								long hour = bookingSevice.calculateTotalRideTime(actualTime);
								double baseRate = rateGroupService.getBaseRate(openBooking.getUser()).getBaseRateBean()
										.getBaseRate();
								double discount = rateGroupService.getBaseRate(openBooking.getUser()).getDiscount();
								double fare = bookingSevice.calculateActualFare(hour, baseRate, discount);
								if (fare == 0.0) {
									fare = baseRate - (baseRate * (discount / 100));
								}
								redirectAttributes.addFlashAttribute("fare", fare);
								redirectAttributes.addFlashAttribute("bicycleId",
										issueCycleForOnlineDTO.getBicycleId());
								redirectAttributes.addFlashAttribute("bookingStatus", openBooking);
								return new ModelAndView("redirect:/manager/paymentAtPickupPoint");
							} else {
								Booking bookingDetails = bookingSevice.updateIssueBicycleDetails(issueCycleForOnlineDTO,
										openBooking.getFare());
								if (null == bookingDetails) {
									return new ModelAndView(ViewConstant.BOOKING,
											ModelAttributeConstant.BOOKING_FAILURE,
											"Sorry ..!! Your Operation Failed.");
								} else {
									redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_DETAILS,
											bookingDetails);
									return new ModelAndView("redirect:/manager/printIssueBicycleDetails");
								}
							}
						} else {
							redirectAttributes.addFlashAttribute(ModelAttributeConstant.ERROR_MESSAGE,
									"You already taken bicycle on this booking Id:- " + openBooking.getBookingId());
							return new ModelAndView(ViewConstant.REDIRECT_TO_MANAGER_BOOKING);
						}
					}
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
		return new ModelAndView(ViewConstant.PAYMENT_AT_PICKUP_POINT);
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
			redirectAttributes.addFlashAttribute(ModelAttributeConstant.BOOKING_FAILURE,
					"Sorry ..!! Your Operation Failed.");
			return new ModelAndView(ViewConstant.BOOKING);
		} else {

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
	 * @throws NotFoundException
	 */
	@PostAuthorize("@currentUserService.canAccessUser(principal, #id)")
	@RequestMapping(value = "/user/bookingHistory/{id}", method = RequestMethod.GET)
	public ModelAndView userBookingHistory(Model model, @PathVariable("id") Long id) throws NotFoundException {
		User user = userService.userDetails(id);
		if(user == null){
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		model.addAttribute(ModelAttributeConstant.USER, user);
		model.addAttribute("bookingHistory", bookingSevice.getAllBooking(user, false));

		return new ModelAndView(ViewConstant.USER_BOOKIG_HISTORY);
	}
}
