package com.mindfire.bicyclesharing.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.dto.TransferRensponseDTO;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.TransferResponse;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.TransferRequestRepository;
import com.mindfire.bicyclesharing.repository.TransferResponseRepository;

@Component
public class TransferResponseComponent {

	@Autowired
	private TransferResponseRepository transferResponseRepository;

	@Autowired
	private TransferRequestRepository transferRequestRepository;

	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;

	public TransferResponse mapNewTransferResponse(TransferRensponseDTO transferRensponseDTO,
			Authentication authentication) {

		TransferResponse transferResponse = new TransferResponse();
		transferResponse.setRequest(transferRequestRepository.findByRequestId(transferRensponseDTO.getRequestId()));
		transferResponse.setQuantity(transferRensponseDTO.getQuantity());

		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

		transferResponse
				.setPickUpPoint(pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint());
		transferResponse.setManager(currentUser.getUser());

		return transferResponseRepository.save(transferResponse);
	}

	public List<TransferResponse> getResponses(CurrentUser currentUser) {
		PickUpPoint pickUpPoint = pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint();
		return transferResponseRepository.findByPickUpPoint(pickUpPoint);
	}

}
