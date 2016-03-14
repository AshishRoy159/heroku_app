package com.mindfire.bicyclesharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mindfire.bicyclesharing.model.PasswordResetToken;
import com.mindfire.bicyclesharing.model.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	PasswordResetToken findByToken(String token);

	PasswordResetToken findByUser(User user);
}
