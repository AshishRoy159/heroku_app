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

package com.mindfire.bicyclesharing.model;

import java.sql.Timestamp;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The meta model class for the {@link Booking} entity.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@StaticMetamodel(Booking.class)
public class Booking_ {
	public static volatile SingularAttribute<Booking, Long> bookingId;
	public static volatile SingularAttribute<Booking, Timestamp> actualIn;
	public static volatile SingularAttribute<Booking, Timestamp> actualOut;
	public static volatile SingularAttribute<Booking, BiCycle> biCycleId;
	public static volatile SingularAttribute<Booking, Timestamp> bookingTime;
	public static volatile SingularAttribute<Booking, Timestamp> expectedIn;
	public static volatile SingularAttribute<Booking, Timestamp> expectedOut;
	public static volatile SingularAttribute<Booking, Double> fare;
	public static volatile SingularAttribute<Booking, Boolean> isOpen;
	public static volatile SingularAttribute<Booking, Boolean> isUsed;
	public static volatile SingularAttribute<Booking, PickUpPoint> pickedUpFrom;
	public static volatile SingularAttribute<Booking, PickUpPoint> returnedAt;
	public static volatile SingularAttribute<Booking, User> user;
}
