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

package com.mindfire.bicyclesharing.service;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.message.MessageResponse;
import com.plivo.helper.exception.PlivoException;

/**
 * The SMSService class is used to send SMS alert to user on booking.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class SMSService {

	Logger logger = Logger.getLogger(getClass());

	/**
	 * This method is used to send SMS alert to user on booking.
	 * 
	 * @param recipientNumber
	 *            the user's phone number
	 * @param message
	 *            the message to send
	 */
	public void sendMessage(String recipientNumber, String message) {
		String authId = "MAY2VINJFJY2UXMZQ2ZJ";
		String authToken = "MDk2NGY0ZjlkZDFmOTk3ZDFmYjdmM2JkM2JkYjFk";
		RestAPI api = new RestAPI(authId, authToken, "v1");
		LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();

		// Sender's phone number with country code
		parameters.put("src", "917809112166");

		// Receiver's phone number with country code
		parameters.put("dst", recipientNumber);

		// Your SMS text message
		parameters.put("text", message);

		// The URL to which with the status of the message is sent
		parameters.put("url", "https://api.plivo.com/v1/Account/MAY2VINJFJY2UXMZQ2ZJ/Message/");
		parameters.put("method", "POST"); // The method used to call the url

		try {
			// Send the message
			MessageResponse msgResponse = api.sendMessage(parameters);

			// Log the response
			logger.info(msgResponse);

			// Log the Api ID
			logger.info("Api ID : " + msgResponse.apiId);

			// Log the Response Message
			logger.info("Message : " + msgResponse.message);

			if (msgResponse.serverCode == 202) {
				// Log the Message UUID
				logger.info("Message UUID : " + msgResponse.messageUuids.get(0).toString());
			} else {
				logger.info(msgResponse.error);
			}
		} catch (PlivoException e) {
			logger.error(e.getLocalizedMessage());
		}
	}
}
