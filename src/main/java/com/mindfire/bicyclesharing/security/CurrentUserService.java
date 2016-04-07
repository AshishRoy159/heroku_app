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

package com.mindfire.bicyclesharing.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.PickUpPointManagerComponent;
import com.mindfire.bicyclesharing.component.TransferComponent;
import com.mindfire.bicyclesharing.component.TransferRequestComponent;

/**
 * CurrentUserService class contains methods for current user related security
 * operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class CurrentUserService {

	@Autowired
	private PickUpPointManagerComponent pickUpPointManagerComponent;

	@Autowired
	private TransferComponent transferComponent;

	@Autowired
	private TransferRequestComponent transferRequestComponent;

	/**
	 * This method is used to restrict access to user profile from other users
	 * 
	 * @param currentUser
	 *            to get current logged in user
	 * @param userId
	 *            id of the user for user profile view
	 * @return {@link Boolean} true or false
	 */
	public boolean canAccessUser(CurrentUser currentUser, Long userId) {
		return currentUser != null
				&& (currentUser.getRole().getUserRole().equals("ADMIN") || currentUser.getUserId().equals(userId));
	}

	/**
	 * This method is used to restrict access to send transfer page from manager
	 * other than from sending pickup point
	 * 
	 * @param currentUser
	 *            to get current logged in user
	 * @param id
	 *            the id of the transfer record
	 * @return {@link Boolean} true or false
	 */
	public boolean canAccessManagerSender(CurrentUser currentUser, Long id) {
		return currentUser != null && (pickUpPointManagerComponent.getPickUpPointManagerByUser(currentUser.getUser())
				.getPickUpPoint().equals(transferComponent.getTransferDetails(id).getTransferredFrom()));
	}

	/**
	 * This method is used to restrict access to receive transfer page from
	 * manager other than from receiving pickup point
	 * 
	 * @param currentUser
	 *            to get current logged in user
	 * @param id
	 *            id of the transfer record
	 * @return {@link Boolean} true or false
	 */
	public boolean canAccessManagerReceiver(CurrentUser currentUser, Long id) {
		return currentUser != null && (pickUpPointManagerComponent.getPickUpPointManagerByUser(currentUser.getUser())
				.getPickUpPoint().equals(transferComponent.getTransferDetails(id).getTransferredTo()));
	}

	/**
	 * This method is used to restrict manager to respond to transfer requests
	 * from their current pickup point.
	 * 
	 * @param currentUser
	 *            to get current logged in user
	 * @param requestId
	 *            id of the transfer request record
	 * @return {@link Boolean} true or false
	 */
	public boolean canAccessManagerResponse(CurrentUser currentUser, Long requestId) {
		return currentUser != null && !(pickUpPointManagerComponent.getPickUpPointManagerByUser(currentUser.getUser())
				.getPickUpPoint().equals(transferRequestComponent.getTransferRequest(requestId).getPickUpPoint()));
	}
}
