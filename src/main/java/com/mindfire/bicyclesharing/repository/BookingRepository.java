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

package com.mindfire.bicyclesharing.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.User;

/**
 * Repository for {@link Booking} Entity used for CRUD operation on Booking.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface BookingRepository extends DataTablesRepository<Booking, Long> {

	/**
	 * This method is used for Booking status is open or not.
	 * 
	 * @param user
	 *            User object
	 * @param open
	 *            Boolean value
	 * @param isUsed
	 *            Boolean value
	 * @return Booking
	 */
	public List<Booking> findByUserAndIsOpenAndIsUsed(User user, Boolean open, Boolean isUsed);

	/**
	 * This method is used to get current open booking of an user.
	 * 
	 * @param open
	 *            booking status
	 * @param user
	 *            the concerned user
	 * @return {@link Booking} object
	 */
	public Booking findByIsOpenAndUser(Boolean open, User user);

	/**
	 * This method is used to find booking details using bookingId.
	 * 
	 * @param id
	 *            bookingId
	 * @return Booking Object
	 */
	public Booking findByBookingId(Long id);

	/**
	 * This method is used for find all Booking.
	 * 
	 * @param user
	 *            User object
	 * @return {@link Booking} List
	 */
	public List<Booking> findAllByUser(User user);

	/**
	 * This method is used for find all booking based on booking status.
	 * 
	 * @param isOpen
	 *            Boolean value
	 * @return {@link Booking} List
	 */
	public List<Booking> findByIsOpen(Boolean isOpen);

	/**
	 * This method is used to find the all booking based on the isOpen status
	 * and BiCycle is Not Null.
	 * 
	 * @param isOpen
	 *            Boolean value
	 * @return {@link Booking} List
	 */
	public List<Booking> findAllByIsOpenAndBiCycleIdIsNotNull(Boolean isOpen);

	/**
	 * This method is used to find the all booking based on the isOpen status
	 * and BiCycle is Null.
	 * 
	 * @param isOpen
	 *            Boolean value
	 * @return {@link Booking} List
	 */
	public List<Booking> findAllByIsOpenAndBiCycleIdIsNull(Boolean isOpen);
}
