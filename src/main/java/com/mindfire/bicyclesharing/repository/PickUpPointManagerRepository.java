package com.mindfire.bicyclesharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindfire.bicyclesharing.model.PickUpPointManager;

@Repository
public interface PickUpPointManagerRepository extends JpaRepository<PickUpPointManager, Long> {

}
