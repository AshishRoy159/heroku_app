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
import com.mindfire.bicyclesharing.dto.IssueCycleDTO;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.PickUpPointManager;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.repository.BiCycleRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.UserRepository;
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
	private BookingService bookingService;

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
	@RequestMapping(value = { "/issueCycle" }, method = RequestMethod.POST)
	public ModelAndView issueBicycle(RedirectAttributes redirectAttributes,
			@ModelAttribute("issueCycleData") IssueCycleDTO issueCycleDTO, Authentication auth) {
		Booking booking = bookingService.addNewBooking(auth, issueCycleDTO);
		if (null == booking) {
			redirectAttributes.addFlashAttribute("bookingFailure", "Booking Failed.. Please Try Again.");
			return new ModelAndView("redirect:/manager/booking");
		} else {
			redirectAttributes.addFlashAttribute("bookingSuccess",
					"Booking Successfull. Your Booking Id is : " + booking.getBookingId());
			return new ModelAndView("redirect:/manager/booking");
		}
	}
}
