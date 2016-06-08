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
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mindfire.bicyclesharing.dto.BookingPaymentDTO;
import com.mindfire.bicyclesharing.dto.PaymentAtPickUpPointDTO;
import com.mindfire.bicyclesharing.dto.ReceiveBicyclePaymentDTO;
import com.mindfire.bicyclesharing.dto.UserBookingPaymentDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.BookingTransaction;
import com.mindfire.bicyclesharing.model.Booking_;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.BiCycleRepository;
import com.mindfire.bicyclesharing.repository.BookingRepository;
import com.mindfire.bicyclesharing.repository.BookingTransactionRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointRepository;
import com.mindfire.bicyclesharing.repository.UserRepository;
import com.mindfire.bicyclesharing.repository.WalletRepository;
import com.mindfire.bicyclesharing.repository.WalletTransactionRepository;
import com.mindfire.bicyclesharing.security.CurrentUser;

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

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private BiCycleRepository bicycleRepository;

	@Autowired
	private PickUpPointManagerRepository managerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private BookingTransactionRepository bookingTransactionRepository;

	@Autowired
	private WalletTransactionRepository walletTransactionRepository;

	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;

	@Autowired
	private PickUpPointRepository pickUpPointRepository;

	/**
	 * This method is used for receiving the data from IssueCycleDTO object and
	 * set the data to the corresponding entity class
	 * 
	 * @param authentication
	 *            to get current logged in user details
	 * @param bookingPaymentDTO
	 *            the data from the view
	 * @param session
	 *            for session data
	 * @return Booking object
	 */
	@Transactional
	public Booking mapNewBooking(Authentication authentication, BookingPaymentDTO bookingPaymentDTO,
			HttpSession session) {
		User user = userRepository.findByUserId(bookingPaymentDTO.getUserId());
		Wallet userWallet = walletRepository.findByUser(user);
		WalletTransaction walletTransaction = new WalletTransaction();
		String transactionType = "BOOKING";

		if (bookingPaymentDTO.getMode().equals("wallet")) {
			logger.info("User has chosen to pay from wallet.");

			if (userWallet.getBalance() < bookingPaymentDTO.getAmount()) {
				logger.info("User has insufficient balance in wallet.");
				return null;
			} else {
				logger.info("Sufficient balane available in wallet.");
				userWallet.setBalance(userWallet.getBalance() - bookingPaymentDTO.getAmount());

				try {
					walletRepository.save(userWallet);
					logger.info("User's wallet updated after payment.");
				} catch (DataIntegrityViolationException dataIntegrityViolationException) {
					throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
				}
				logger.info("Payment successful.");
				walletTransaction = createWalletTransaction(bookingPaymentDTO.getAmount(), bookingPaymentDTO.getMode(),
						transactionType, userWallet);
			}
		} else {
			logger.info("Payment successful.");
			walletTransaction = createWalletTransaction(bookingPaymentDTO.getAmount(), bookingPaymentDTO.getMode(),
					transactionType, userWallet);
		}
		return createNewBooking(authentication, bookingPaymentDTO, walletTransaction, session);
	}

	/**
	 * This method is used to create a wallet transaction details.
	 * 
	 * @param amount
	 *            fare
	 * @param mode
	 *            payment mode
	 * @param type
	 *            payment type
	 * @param userWallet
	 *            this object contains UserWallet information.
	 * @return WalletTransaction object
	 */
	private WalletTransaction createWalletTransaction(Double amount, String mode, String type, Wallet userWallet) {
		WalletTransaction walletTransaction = new WalletTransaction();

		walletTransaction.setAmount(amount);
		walletTransaction.setMode(mode);
		walletTransaction.setType(type);
		walletTransaction.setWallet(userWallet);

		try {
			walletTransactionRepository.save(walletTransaction);
			logger.info("New transaction details added to database.");
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		return walletTransaction;
	}

	/**
	 * This method is used to create new Booking for user.
	 * 
	 * @param authentication
	 *            this object is used for retrieving the current user details.
	 * @param bookingPaymentDTO
	 *            this object is used for holding the booking related data..
	 * @param walletTransaction
	 *            the wallet transaction details for this booking
	 * @param session
	 *            HttpSession object is used for holding the required value into
	 *            the session variable.
	 * @return Booking object
	 */
	private Booking createNewBooking(Authentication authentication, BookingPaymentDTO bookingPaymentDTO,
			WalletTransaction walletTransaction, HttpSession session) {
		Booking newBooking = new Booking();
		BookingTransaction bookingTransaction = new BookingTransaction();
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

		newBooking.setActualOut(new Timestamp(System.currentTimeMillis()));
		newBooking.setBiCycleId(bicycleRepository.findByBiCycleId(bookingPaymentDTO.getBicycleId()));
		newBooking.setBookingTime(new Timestamp(System.currentTimeMillis()));
		newBooking.setExpectedIn((Timestamp) session.getAttribute("expectedIn"));
		newBooking.setExpectedOut(new Timestamp(System.currentTimeMillis()));
		newBooking.setIsOpen(true);
		newBooking.setPickedUpFrom(
				managerRepository.findByUser(userRepository.findByUserId(currentUser.getUserId())).getPickUpPoint());
		newBooking.setUser(userRepository.findByUserId(bookingPaymentDTO.getUserId()));
		newBooking.setFare(bookingPaymentDTO.getAmount());

		try {
			bookingRepository.save(newBooking);
			logger.info("New booking record created.");
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		bookingTransaction.setBooking(newBooking);
		bookingTransaction.setTransaction(walletTransaction);

		try {
			bookingTransactionRepository.save(bookingTransaction);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		return newBooking;
	}

	/**
	 * This method is used for mapping receive bicycle data and update into the
	 * booking details field along with the booking status.
	 * 
	 * @param id
	 *            this id is booking id
	 * @param fare
	 *            total fare amount
	 * @param authentication
	 *            to get current logged in user details
	 * @return Booking object
	 */
	@Transactional
	public Booking mapReceiveBicycle(Long id, double fare, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Booking booking = bookingRepository.findByBookingId(id);

		booking.setActualIn(new Timestamp(System.currentTimeMillis()));
		booking.setReturnedAt(pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint());
		booking.setIsOpen(false);
		booking.setIsUsed(true);
		booking.setFare(booking.getFare() + fare);

		try {
			bookingRepository.save(booking);
			logger.info("Successfully updated booking details.");
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		BiCycle biCycle = bicycleRepository.findByBiCycleId(booking.getBiCycleId().getBiCycleId());
		biCycle.setCurrentLocation(booking.getReturnedAt());
		biCycle.setIsAvailable(true);

		try {
			bicycleRepository.save(biCycle);
			logger.info("Successfully updated bicycle details.");
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		PickUpPoint pickUpPoint = pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint();
		pickUpPoint.setCurrentAvailability(
				bicycleRepository.findByCurrentLocationAndIsAvailable(pickUpPoint, true).size());

		try {
			pickUpPointRepository.save(pickUpPoint);
			logger.info("Successfully updated pickUp point details.");
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		return booking;
	}

	/**
	 * This method is used for mapping the receive bicycle payment data at the
	 * time of bicycle receive.
	 * 
	 * @param receiveBicyclePaymentDTO
	 *            receive bicycle payment data
	 * @param userWallet
	 *            UserWallet object related to corresponding user.
	 * @return WalletTransaction object
	 */
	@Transactional
	public WalletTransaction mapReceiveBicyclePayment(ReceiveBicyclePaymentDTO receiveBicyclePaymentDTO,
			Wallet userWallet) {
		String transactionType = "RECEIVEBICYCLE";
		WalletTransaction walletTransaction = new WalletTransaction();
		BookingTransaction bookingTransaction = new BookingTransaction();

		if (receiveBicyclePaymentDTO.getMode().equals("cash")) {
			logger.info("User paid by cash.");
			walletTransaction = createWalletTransaction(receiveBicyclePaymentDTO.getFare(),
					receiveBicyclePaymentDTO.getMode(), transactionType, userWallet);
			logger.info("Payment successful.");
		} else if (userWallet.getBalance() < receiveBicyclePaymentDTO.getFare()) {
			logger.info("User has insufficient balance in wallet.");
			return null;
		} else {
			userWallet.setBalance(userWallet.getBalance() - receiveBicyclePaymentDTO.getFare());

			try {
				walletRepository.save(userWallet);
				logger.info("Updated user's wallet details.");
			} catch (DataIntegrityViolationException dataIntegrityViolationException) {
				throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
			}
			walletTransaction = createWalletTransaction(receiveBicyclePaymentDTO.getFare(),
					receiveBicyclePaymentDTO.getMode(), transactionType, userWallet);
			logger.info("Added wallet transaction details to database.");
			logger.info("Payment successful.");
		}

		bookingTransaction.setBooking(bookingRepository.findByBookingId(receiveBicyclePaymentDTO.getBookingId()));
		bookingTransaction.setTransaction(walletTransaction);

		try {
			bookingTransactionRepository.save(bookingTransaction);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		return walletTransaction;
	}

	/**
	 * This method is used for creating a wallet transaction for user booking.
	 * 
	 * @param userBookingPaymentDTO
	 *            User Booking Payment data
	 * @param userWallet
	 *            UserWallet object related to corresponding user.
	 * @param type
	 *            payment Type
	 * @return WalletTransaction object
	 */
	public WalletTransaction userBookingWalletTransaction(UserBookingPaymentDTO userBookingPaymentDTO,
			Wallet userWallet, String type) {
		return createWalletTransaction(userBookingPaymentDTO.getFare(), userBookingPaymentDTO.getMode(), type,
				userWallet);
	}

	/**
	 * This method is used for creating a wallet transaction for user booking.
	 * 
	 * @param paymentAtPickUpPointDTO
	 *            User Booking Payment data
	 * 
	 * @return WalletTransaction object
	 */
	public WalletTransaction mapUserPaymentTransaction(PaymentAtPickUpPointDTO paymentAtPickUpPointDTO) {
		String paymentType = "ONLINE BOOKING";
		String mode = "cash";
		Wallet userWallet = walletRepository
				.findByUser(bookingRepository.findByBookingId(paymentAtPickUpPointDTO.getBookingId()).getUser());
		WalletTransaction walletTransaction = createWalletTransaction(paymentAtPickUpPointDTO.getFare(), mode,
				paymentType, userWallet);
		BookingTransaction bookingTransaction = new BookingTransaction();

		bookingTransaction.setBooking(bookingRepository.findByBookingId(paymentAtPickUpPointDTO.getBookingId()));
		bookingTransaction.setTransaction(walletTransaction);

		try {
			bookingTransactionRepository.save(bookingTransaction);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		return walletTransaction;
	}

	/**
	 * This method is used for getting all booking details for particular user
	 * where booking status is false
	 * 
	 * @param input
	 *            {@link DataTablesInput} object
	 * @param user
	 *            {@link User}
	 * @param isUsed
	 *            Boolean value
	 * @return {@link Booking} List
	 */
	public DataTablesOutput<Booking> getAllBookingByUser(DataTablesInput input, User user, Boolean isUsed) {
		return bookingRepository.findAll(input, getClosedBookings(user, isUsed));
	}

	/**
	 * This method is used for closing the booking based on the booking id
	 * 
	 * @param booking
	 *            the current booking object
	 * @return {@link Booking} object
	 */
	@Transactional
	public Booking mapCloseBooking(Booking booking) {
		BookingTransaction bookingTransaction;
		Wallet wallet;

		if (null != booking && booking.getIsOpen()) {
			logger.info("Booking is valid for close request.");
			booking.setIsOpen(false);
			booking.setIsUsed(false);

			try {
				logger.info("Booking closed.");
				booking = bookingRepository.save(booking);
			} catch (DataIntegrityViolationException dataIntegrityViolationException) {
				return null;
			}
		} else {
			logger.info("Booking is already closed.");
			return null;
		}

		bookingTransaction = bookingTransactionRepository.findByBooking(booking);

		if (null == bookingTransaction) {
			return booking;
		} else {
			wallet = walletRepository.findByUser(booking.getUser());

			wallet.setBalance(wallet.getBalance() + bookingTransaction.getTransaction().getAmount());

			try {
				walletRepository.save(wallet);
			} catch (DataIntegrityViolationException dataIntegrityViolationException) {
				return null;
			}
			createWalletTransaction(bookingTransaction.getTransaction().getAmount(), "wallet", "REFUND", wallet);
		}
		return booking;
	}

	/**
	 * This method is used for mapping the existing booking details.
	 * 
	 * @param isOpen
	 *            boolean value
	 * @param user
	 *            User object
	 * @return {@link Booking} object
	 */
	public Booking mapExistingBooking(Boolean isOpen, User user) {
		return bookingRepository.findByIsOpenAndUser(isOpen, user);
	}

	/**
	 * This method is used to map the running bookings based on their isOpen
	 * status.
	 * 
	 * @param isOpen
	 *            Boolean value for check their status
	 * @return {@link Booking} List
	 */
	public List<Booking> mapRunningBooking(Boolean isOpen) {
		return bookingRepository.findAllByIsOpenAndBiCycleIdIsNotNull(isOpen);
	}

	/**
	 * This method is used to get all booking details
	 * 
	 * @param isUsed
	 *            booking status
	 * @param input
	 *            {@link DataTablesInput} object
	 * @return {@link Booking} {@link DataTablesOutput}
	 */
	public DataTablesOutput<Booking> getAllBookings(DataTablesInput input, Boolean isUsed) {
		return bookingRepository.findAll(input, getUsedSpecification(isUsed));
	}

	/**
	 * This method is used to create specification for fetching only used
	 * bookings.
	 * 
	 * @param isUsed
	 *            booking status
	 * @return {@link Booking} {@link Specification}
	 */
	private Specification<Booking> getUsedSpecification(Boolean isUsed) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Booking_.isUsed), isUsed));
			return predicate;
		};
	}

	private Specification<Booking> getClosedBookings(User user, Boolean isUsed) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Booking_.user), user));
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Booking_.isUsed), isUsed));
			return predicate;
		};
	}
}
