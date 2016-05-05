package com.mindfire.bicyclesharing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mindfire.bicyclesharing.component.UserBookingComponent;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.service.BookingService;

public class BookingServiceTest {

	@InjectMocks
	private BookingService bookingService;

	@Mock
	private UserBookingComponent bookingComponent;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFetchUserDetails() {
		Booking booking = new Booking();
		booking.setBookingId(1l);

		when(bookingComponent.getBooking(1l)).thenReturn(booking);

		Booking testBooking = bookingService.getBookingById(1l);
		verify(bookingComponent).getBooking(1l);
		// assertEquals(1l, testUser.getUserId().longValue());

		assertThat(testBooking.getBookingId(), is(1l));
	}
}
