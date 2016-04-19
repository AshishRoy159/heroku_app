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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mindfire.bicyclesharing.constant.ViewConstant;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.service.UserService;
import com.mindfire.bicyclesharing.service.WalletService;
import com.mindfire.bicyclesharing.service.WalletTransactionService;

/**
 * This class contains all the Request Mappings related to the wallet
 * transaction and sends the control to the service.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Controller
public class WalletTransactionController {

	@Autowired
	private WalletTransactionService walletTransactionService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private UserService userService;

	/**
	 * This method used to map the request walletTransaction for geting the
	 * wallet transaction based on the user wallet.
	 * 
	 * @param model
	 *            used for sending the data to the view.
	 * @param id
	 *            User id
	 * @return walletTransaction view
	 */
	@PostAuthorize("@currentUserService.canAccessUser(principal, #id)")
	@RequestMapping(value = "/user/walletTransaction/{id}", method = RequestMethod.GET)
	public ModelAndView walletTransactionDetails(Model model, @PathVariable("id") Long id) {

		if (userService.userDetails(id) == null) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
		Wallet wallet = walletService.getWallet(userService.userDetails(id));
		model.addAttribute("wallet", wallet);
		model.addAttribute("walletTransactions", walletTransactionService.getAllTransactionByWallet(wallet));
		return new ModelAndView(ViewConstant.WALLET_TRANSACTION);
	}
}
