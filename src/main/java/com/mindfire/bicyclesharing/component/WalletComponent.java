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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.WalletBalanceDTO;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.repository.UserRepository;
import com.mindfire.bicyclesharing.repository.WalletRepository;

/**
 * WalletComponent class is used to get the data from the WalletBalanceDTO class
 * and set the data to the corresponding Entity classes
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class WalletComponent {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * This method is used for receiving data from WalletBalanceDTO and set the
	 * data to corresponding entity class.
	 * 
	 * @param walletBalanceDTO
	 *            the data from the view
	 * @return Integer 0 or 1
	 */
	public int mapWalletBalance(WalletBalanceDTO walletBalanceDTO) {
		User user = userRepository.findByUserId(walletBalanceDTO.getUserId());
		Wallet wallet = walletRepository.findByUser(user);
		Double balance = wallet.getBalance() + walletBalanceDTO.getBalance();

		try {
			logger.info("Updated user's wallet details.");
			return walletRepository.updateBalance(balance, user);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return 0;
		}
	}

	/**
	 * This method is used to get wallet details of an user
	 * 
	 * @param user
	 *            the concerned user
	 * @return {@link Wallet} object
	 */
	public Wallet findWalletByUser(User user) {
		return walletRepository.findByUser(user);
	}
}
