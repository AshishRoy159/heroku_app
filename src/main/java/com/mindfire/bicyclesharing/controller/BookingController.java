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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.dto.BookingPaymentDTO;
import com.mindfire.bicyclesharing.dto.IssueCycleDTO;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.PickUpPointManager;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.repository.BiCycleRepository;
import com.mindfire.bicyclesharing.repository.BookingRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointRepository;
import com.mindfire.bicyclesharing.repository.UserRepository;
import com.mindfire.bicyclesharing.service.BiCycleService;
import com.mindfire.bicyclesharing.service.BookingService;

/**
 * BookingController contains all the mappings related to the Booking.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class BookingController {

	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BiCycleRepository biCycleRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private BiCycleService biCycleService;

	@Autowired
	private PickUpPointRepository pickUpPointRepository;

	/**
	 * This method is used to map the booking of the bicycles request. Simply
	 * render the booking view
	 * 
	 * @param model
	 *            to map the model attributes
	 * @param id
	 *            userId of the respective manager
	 * @return booking view
	 */
	@PostAuthorize("hasAuthority('MANAGER')")
	@RequestMapping(value = { "/manager/booking" }, method = RequestMethod.GET)
	public ModelAndView getBookingView(Model model, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Long userId = currentUser.getUserId();
		User manager = userRepository.findByUserId(userId);
		PickUpPointManager pickUpPoint = pickUpPointManagerRepository.findByUser(manager);
		List<BiCycle> biCycles = biCycleRepository.findByCurrentLocationAndIsAvailable(pickUpPoint.getPickUpPoint(),
				true);
		model.addAttribute("biCycles", biCycles);
		return new ModelAndView("booking");
	}

	/**
	 * This method is used to map the issueCycle request and send the data to
	 * booking service. Simply render the payment view
	 * 
	 * @param redirectAttributes
	 *            this is used for showing message on the view.
	 * @param issueCycleDTO
	 * @param auth
	 *            this is used for retrieving the currentUser
	 * @return payment or booking view
	 */
	@RequestMapping(value = { "/issueCyclePayment" }, method = RequestMethod.POST)
	public ModelAndView issueBicycle(RedirectAttributes redirectAttributes,
			@ModelAttribute("issueCyclePaymentData") BookingPaymentDTO bookingPaymentDTO, Authentication auth,
			HttpSession session) {
		CurrentUser currentUser = (CurrentUser) auth.getPrincipal();
		User user = userRepository.findByUserId(bookingPaymentDTO.getUserId());
		Booking existingBooking = bookingRepository.findByUserAndIsOpen(user, true);
		if (null != existingBooking) {
			redirectAttributes.addFlashAttribute("bookingFailure", "Oops..!! Your booking status is open..!");
			return new ModelAndView("redirect:/manager/booking");
		}

		Booking booking = bookingService.addNewBooking(auth, bookingPaymentDTO, session);
		if (null == booking) {
			redirectAttributes.addFlashAttribute("bookingFailure", "Oops..!! You do not have sufficient balance");
			return new ModelAndView("redirect:/manager/booking");
		} else {
			biCycleService.updateBiCycleDetails(bookingPaymentDTO.getBicycleId());
			PickUpPoint pickUpPoint = pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint();
			pickUpPoint.setCurrentAvailability(pickUpPoint.getCurrentAvailability() - 1);
			pickUpPointRepository.save(pickUpPoint);
			redirectAttributes.addFlashAttribute("bookingSuccess",
					"Booking Successfull. Your Booking Id is : " + booking.getBookingId());
			return new ModelAndView("redirect:/manager/booking");
		}
	}

	/**
	 * This method is used to map the bookingPayment request and simply render
	 * the booking payment view related to current booking of the user.
	 * 
	 * @param issueCycleDTO
	 *            IssueCycleDTO object for issue BiCycle data
	 * @param session
	 *            HttpSession object is used for holding the required value into
	 *            the session's variables.
	 * @param model
	 *            Model object
	 * @param redirectAttributes
	 * @return bookingPayment or booking view.
	 */
	@RequestMapping(value = { "/bookingPayment" }, method = RequestMethod.GET)
	public ModelAndView bookingPayment(@ModelAttribute("issueCycleData") IssueCycleDTO issueCycleDTO,
			HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("issueCycleData", issueCycleDTO);
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.HOUR, issueCycleDTO.getExpectedInTime());

		session.setAttribute("expectedIn", new Timestamp(cal.getTimeInMillis()));
		User user = userRepository.findByUserId(issueCycleDTO.getUserId());
		Double fare = (user.getRateGroup().getBaseRate() * issueCycleDTO.getExpectedInTime());
		model.addAttribute("baseFare", fare);
		if (user.getIsApproved() == true && user.getEnabled() == true) {
			return new ModelAndView("bookingPayment");
		} else {
			redirectAttributes.addFlashAttribute("bookingFailure",
					"It seems your verification is still pending. you need to get approved to book a bicycle.");
			return new ModelAndView("redirect:/manager/booking");
		}
	}
}
