package com.mindfire.bicyclesharing.service;

import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.message.MessageResponse;
import com.plivo.helper.exception.PlivoException;

@Service
public class SMSService {

	public void sendMessage(String recipientNumber, String message){
		String authId = "MAY2VINJFJY2UXMZQ2ZJ";
        String authToken = "MDk2NGY0ZjlkZDFmOTk3ZDFmYjdmM2JkM2JkYjFk";
        RestAPI api = new RestAPI(authId, authToken, "v1");

        LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("src", "917809112166"); // Sender's phone number with country code
        parameters.put("dst", recipientNumber); // Receiver's phone number with country code
        parameters.put("text", message); // Your SMS text message
        // Send Unicode text
        //parameters.put("text", "こんにちは、元気ですか？"); // Your SMS text message - Japanese
        //parameters.put("text", "Ce est texte généré aléatoirement"); // Your SMS text message - French
        parameters.put("url", "https://api.plivo.com/v1/Account/MAY2VINJFJY2UXMZQ2ZJ/Message/"); // The URL to which with the status of the message is sent
        parameters.put("method", "POST"); // The method used to call the url

        try {
            // Send the message
            MessageResponse msgResponse = api.sendMessage(parameters);

            // Print the response
            System.out.println(msgResponse);
            // Print the Api ID
            System.out.println("Api ID : " + msgResponse.apiId);
            // Print the Response Message
            System.out.println("Message : " + msgResponse.message);

            if (msgResponse.serverCode == 202) {
                // Print the Message UUID
                System.out.println("Message UUID : " + msgResponse.messageUuids.get(0).toString());
            } else {
                System.out.println(msgResponse.error);
            }
        } catch (PlivoException e) {
            System.out.println(e.getLocalizedMessage());
        }
	}
}
