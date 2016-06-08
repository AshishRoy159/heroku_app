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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.Wallet;

/**
 * Repository for {@link Wallet} Entity used for CRUD operation on User.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

	/**
	 * This method is used to update the balance in Wallet of the respective
	 * User
	 * 
	 * @param balance
	 *            the amount to be updated
	 * @param user
	 *            User object
	 * @return Integer 0 or 1
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Wallet w set w.balance =:balance where w.user =:user")
	public int updateBalance(@Param("balance") Double balance, @Param("user") User user);

	/**
	 * This method is used to find the Wallet corresponding to a User
	 * 
	 * @param user
	 *            User object
	 * @return Wallet object
	 */
	public Wallet findByUser(User user);
}
