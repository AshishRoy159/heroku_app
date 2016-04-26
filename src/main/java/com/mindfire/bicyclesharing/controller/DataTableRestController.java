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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.service.BookingService;
import com.mindfire.bicyclesharing.service.UserService;

/**
 * UserRestController contains the mappings related to the users
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@RestController
public class DataTableRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private BookingService bookingService;

	/**
	 * This method is used to map requests for fetching all user's details.
	 * 
	 * @param input
	 *            {@link DataTablesInput} object
	 * @return {@link User} DataTable
	 */
	@JsonView(DataTablesOutput.View.class)
	@RequestMapping(value = { "/data/userList" }, method = RequestMethod.GET)
	public DataTablesOutput<User> userList(@Valid DataTablesInput input) {
		return userService.findAllUsers(input);
	}

	/**
	 * This method is used to map requests for fetching all used booking
	 * details.
	 * 
	 * @param input
	 *            {@link DataTablesInput} object
	 * @return {@link Booking} {@link DataTablesOutput}
	 */
	@JsonView(DataTablesOutput.View.class)
	@RequestMapping(value = "/data/usedBookings", method = RequestMethod.GET)
	public DataTablesOutput<Booking> getBookings(@Valid DataTablesInput input) {
		return bookingService.getAllBookingDetails(input, true);
	}
}
