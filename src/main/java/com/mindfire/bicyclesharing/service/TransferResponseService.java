package com.mindfire.bicyclesharing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.component.TransferResponseComponent;
import com.mindfire.bicyclesharing.dto.TransferRensponseDTO;
import com.mindfire.bicyclesharing.model.TransferResponse;

@Service
public class TransferResponseService {

	@Autowired
	private TransferResponseComponent transferResponseComponent;

	public TransferResponse addNewResponse(TransferRensponseDTO transferResponseDTO, CurrentUser currentUser) {
		return transferResponseComponent.mapNewTransferResponse(transferResponseDTO, currentUser);

	}

	public List<TransferResponse> findResponses(CurrentUser currentUser) {
		return transferResponseComponent.getResponses(currentUser);
	}

}
