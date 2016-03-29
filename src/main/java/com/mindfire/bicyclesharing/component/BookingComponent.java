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

package com.mindfire.bicyclesharing.component;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.dto.IssueCycleDTO;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.repository.BiCycleRepository;
import com.mindfire.bicyclesharing.repository.BookingRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.UserRepository;

/**
 * BookingComponent class is used to get the data from the IssueCycleDTO class
 * and set the data to the corresponding Entity class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class BookingComponent {

	@Autowired
	private BiCycleRepository bicycleRepository;

	@Autowired
	private PickUpPointManagerRepository managerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookingRepository bookingRepository;

	/**
	 * This method is used for receiving the data from IssueCycleDTO object and
	 * set the data to the corresponding entity class
	 * 
	 * @param authentication
	 * @param issueCycleDTO
	 *            the data from the view
	 * @return Booking object
	 */
	public Booking mapNewBooking(Authentication authentication, IssueCycleDTO issueCycleDTO) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Booking newBooking = new Booking();
		newBooking.setActualOut(new Timestamp(System.currentTimeMillis()));
		System.out.println(issueCycleDTO.getBicycleId());
		newBooking.setBiCycleId(bicycleRepository.findByBiCycleId(issueCycleDTO.getBicycleId()));
		newBooking.setBookingTime(new Timestamp(System.currentTimeMillis()));
		newBooking.setExpectedIn(
				Timestamp.valueOf(issueCycleDTO.getExpectedInTime().replace("/", "-").concat(":00.0000")));
		newBooking.setExpectedOut(new Timestamp(System.currentTimeMillis()));
		newBooking.setIsOpen(true);
		newBooking.setPickedUpFrom(
				managerRepository.findByUser(userRepository.findByUserId(currentUser.getUserId())).getPickUpPoint());
		newBooking.setUser(userRepository.findByUserId(issueCycleDTO.getUserId()));

		Booking bookSuccess = bookingRepository.save(newBooking);

		return bookSuccess;
	}
}
