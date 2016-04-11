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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mindfire.bicyclesharing.dto.WalletBalanceDTO;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.service.UserService;
import com.mindfire.bicyclesharing.service.WalletService;
import com.mindfire.bicyclesharing.service.WalletTransactionService;

/**
 * This class contains all the Request Mappings related to the wallet balance
 * and transactions from manager section.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class WalletController {

	@Autowired
	private WalletService walletService;

	@Autowired
	private WalletTransactionService walletTransactionService;

	@Autowired
	private UserService userService;

	/**
	 * This method is used to map the add wallet balance request. Simply render
	 * the addWalletBalance view
	 * 
	 * @return addWalletBalance view
	 */
	@RequestMapping(value = { "/manager/addWalletBalance" }, method = RequestMethod.GET)
	public ModelAndView getWalletView() {
		return new ModelAndView("addWalletBalance");
	}

	/**
	 * This method is used to add balance to the User's wallet.
	 * 
	 * @param walletBalanceDTO
	 *            to receive the incoming data
	 * @param result
	 *            for validating incoming data
	 * @param redirectAttributes
	 *            to map the model attributes
	 * @return addWalletBalance view
	 */
	@RequestMapping(value = "/manager/wallet", method = RequestMethod.POST)
	public ModelAndView addWalletBalance(@Valid @ModelAttribute("addWalletBalance") WalletBalanceDTO walletBalanceDTO,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Balance must be between 100 and 999!!!");
			return new ModelAndView("redirect:addWalletBalance");

		}
		User user = userService.userDetails(walletBalanceDTO.getUserId());
		if (user == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "User not found!!!");
			return new ModelAndView("redirect:addWalletBalance");

		} else if (walletService.addBalance(walletBalanceDTO) == 1) {
			walletTransactionService.createWalletTransaction(walletBalanceDTO);
			redirectAttributes.addFlashAttribute("successMessage", "Successfully Added!!!");
			return new ModelAndView("redirect:addWalletBalance");
		}
		redirectAttributes.addFlashAttribute("errorMessage", "Oops... Operation failed!!");
		return new ModelAndView("redirect:addWalletBalance");
	}

}
