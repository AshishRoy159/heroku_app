package com.mindfire.bicyclesharing.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.VerificationTokenComponent;
import com.mindfire.bicyclesharing.model.VerificationToken;

@Service
public class VerificationTokenService {

	@Autowired
	private VerificationTokenComponent verificationTokenComponent;
	
	public VerificationToken getVerificationToken(String token){
		return verificationTokenComponent.mapVerificationToken(token);
	}
	
	/**
	 * This method is used for generating new verification token for the user
	 * 
	 * @param existingVerificationToken
	 *            the existing expired token
	 * @return VerificationToken object
	 */
	public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
		VerificationToken vToken = verificationTokenComponent.mapVerificationToken(existingVerificationToken);
		vToken.updateToken(UUID.randomUUID().toString());
		return verificationTokenComponent.saveVerificationToken(vToken);
	}
}
