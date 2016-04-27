package com.mindfire.bicyclesharing.model;

import java.sql.Timestamp;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

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
