package com.mindfire.bicyclesharing.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.model.PickUpPointManager;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;

@Component
public class PickUpPointManagerComponent {
    
	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;
	
	public List<PickUpPointManager> getAllManager(){
		return pickUpPointManagerRepository.findAll();
	}
}
