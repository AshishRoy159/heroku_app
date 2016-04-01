package com.mindfire.bicyclesharing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.TransferResponse;

@Repository
public interface TransferResponseRepository extends JpaRepository<TransferResponse, Long> {
	
	public List<TransferResponse> findByPickUpPoint(PickUpPoint pickUpPoint);

}
