package com.mindfire.bicyclesharing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.model.PickUpPointManager;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;

@Service
public class PickUpPointManagerService {

	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;

	public PickUpPointManager setPickUpPointToManager(PickUpPointManager pickUpPointManager) {
		return pickUpPointManagerRepository.save(pickUpPointManager);
	}
}
