package com.mindfire.bicyclesharing.controller;

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
import com.mindfire.bicyclesharing.dto.UserBookingDTO;
import com.mindfire.bicyclesharing.dto.UserBookingPaymentDTO;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.BookingRepository;
import com.mindfire.bicyclesharing.repository.RateGroupRepository;
import com.mindfire.bicyclesharing.repository.UserRepository;
import com.mindfire.bicyclesharing.service.BookingService;
import com.mindfire.bicyclesharing.service.WalletService;

@Controller
public class UserBookingController {

	@Autowired
	private BookingService bookingSevice;

	@Autowired
	private RateGroupRepository rateGroupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WalletService walletService;
	
	@Autowired
	private BookingRepository bookingRepository;

	@RequestMapping(value = "/user/booking", method = RequestMethod.POST)
	public ModelAndView userBooking(@ModelAttribute("userBookingData") UserBookingDTO userBookingDTO,
			RedirectAttributes redirectAttributes, Authentication authentication, Model model) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Booking existing = bookingRepository.findByUserAndIsOpen(currentUser.getUser(), true);
		if(null == existing){
			Booking userBooking = bookingSevice.saveUserBookingDetails(userBookingDTO, authentication);
			if (null == userBooking) {
				redirectAttributes.addFlashAttribute("errorMessage", "oops..!! Booking Failed.");
				return new ModelAndView("redirect:/index");
			} else {
				redirectAttributes.addFlashAttribute("bookingSuccess",
						"Your Booking is successfully completed..Please Choose your payment.");
				long actualTime = (userBooking.getExpectedIn().getTime() - userBooking.getExpectedOut().getTime());
				long hour = (actualTime / (60 * 1000)) / 60;
				double baseRate = rateGroupRepository.findByGroupType(
						userRepository.findByUserId(userBooking.getUser().getUserId()).getRateGroup().getGroupType())
						.getBaseRate();
				double fare = (hour * baseRate);
				model.addAttribute("fare", fare);
				return new ModelAndView("userBookingPayment", "userBookingDetails", userBooking);
			}
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "You have a boooking still open!!");
			return new ModelAndView("redirect:/index");
		}
		
	}

	@RequestMapping(value = "/user/userBookingPayment", method = RequestMethod.POST)
	public ModelAndView userBookingPayment(
			@ModelAttribute("userBookingPaymentData") UserBookingPaymentDTO userBookingPaymentDTO,
			Authentication authentication, RedirectAttributes redirectAttributes) {
		if (userBookingPaymentDTO.getMode().equals("cash")) {
			redirectAttributes.addFlashAttribute("message", "Your booking is successfull");
			return new ModelAndView("redirect:/index");
		} else {
			WalletTransaction walletTransaction = walletService.saveUserBookingPayment(userBookingPaymentDTO,
					authentication);
			if(null == walletTransaction){
				redirectAttributes.addFlashAttribute("message","Your Payment is Not successfully completed Due to low balance");
				return new ModelAndView("redirect:/index");
			}else{
				redirectAttributes.addFlashAttribute("message","Your Payment is successfully completed");
				return new ModelAndView("redirect:/index");
			}
		}
	}
}
