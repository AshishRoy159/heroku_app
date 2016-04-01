package com.mindfire.bicyclesharing.component;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.dto.UserBookingDTO;
import com.mindfire.bicyclesharing.dto.UserBookingPaymentDTO;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.BookingRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointRepository;
import com.mindfire.bicyclesharing.repository.WalletRepository;

@Component
public class UserBookingComponent {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private PickUpPointRepository pickUpPointRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private BookingComponent bookingComponent;

	public Booking setUserBookingDetails(UserBookingDTO userBookingDTO, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Booking userBooking = new Booking();
		userBooking.setBookingTime(new Timestamp(System.currentTimeMillis()));
		userBooking
				.setExpectedOut(Timestamp.valueOf(userBookingDTO.getBookingTime().replace("/", "-").concat(":00.000")));
		userBooking
				.setExpectedIn(Timestamp.valueOf(userBookingDTO.getReturnTime().replace("/", "-").concat(":00.000")));
		userBooking.setUser(currentUser.getUser());
		userBooking.setIsOpen(false);
		userBooking.setPickedUpFrom(pickUpPointRepository.findByPickUpPointId(userBookingDTO.getPickUpPoint()));

		bookingRepository.save(userBooking);
		return userBooking;
	}

	public WalletTransaction mapUserBookingPayment(UserBookingPaymentDTO userBookingPaymentDTO,
			Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Wallet userWallet = walletRepository.findByUser(currentUser.getUser());
		String paymentType = "ONLINE BOOKING";
		if (userBookingPaymentDTO.getMode().equals("wallet")) {
			if (userWallet.getBalance() < userBookingPaymentDTO.getFare()) {
				return null;
			} else {
				userWallet.setBalance(userWallet.getBalance() - userBookingPaymentDTO.getFare());
				walletRepository.save(userWallet);
				return bookingComponent.userBookingWalletTransaction(userBookingPaymentDTO, userWallet, paymentType);
			}
		} else {
			return bookingComponent.userBookingWalletTransaction(userBookingPaymentDTO, userWallet, paymentType);
		}
	}
}
