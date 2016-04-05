package com.mindfire.bicyclesharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mindfire.bicyclesharing.model.BiCycleTransfer;

public interface BiCycleTransferRepository extends JpaRepository<BiCycleTransfer, Long> {

}
