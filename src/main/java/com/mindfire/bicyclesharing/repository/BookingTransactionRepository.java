package com.mindfire.bicyclesharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mindfire.bicyclesharing.model.BookingTransaction;

public interface BookingTransactionRepository extends JpaRepository<BookingTransaction, Long> {

}
