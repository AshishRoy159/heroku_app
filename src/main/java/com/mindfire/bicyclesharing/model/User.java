/*
 * Copyright 2016 Mindfire Solutions
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the users database table.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Entity
@Table(name = "users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	private String email;

	private Boolean enabled;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "is_approved")
	private Boolean isApproved;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "mobile_no")
	private Long mobileNo;

	private String password;

	@ManyToOne
	@JoinColumn(name = "rate_group_id")
	private RateGroup rateGroup;

	@Column(name = "regisration_time", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp registrationTime;

	@Column(name = "user_address")
	private String userAddress;

	// bi-directional many-to-one association to Booking
	@OneToMany(mappedBy = "user")
	private List<Booking> bookings;

	// bi-directional many-to-one association to UserLog
	@OneToMany(mappedBy = "user")
	private List<UserLog> userLogs;

	// bi-directional many-to-one association to ProofDetail
	@ManyToOne
	@JoinColumn(name = "proof_id")
	private ProofDetail proofDetail;

	// bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	// bi-directional many-to-one association to VerificationToken
	@OneToMany(mappedBy = "user")
	private List<VerificationToken> verificationTokens;

	// bi-directional many-to-one association to Wallet
	@OneToMany(mappedBy = "user")
	private List<Wallet> wallets;

	public User() {
		this.enabled = false;
		this.isApproved = false;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Boolean getIsApproved() {
		return this.isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RateGroup getRateGroup() {
		return rateGroup;
	}

	public void setRateGroup(RateGroup rateGroup) {
		this.rateGroup = rateGroup;
	}

	public Timestamp getRegistrationTime() {
		return this.registrationTime;
	}

	public void setRegistrationTime(Timestamp registrationTime) {
		this.registrationTime = registrationTime;
	}

	public String getUserAddress() {
		return this.userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public List<Booking> getBookings() {
		return this.bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public Booking addBooking(Booking booking) {
		getBookings().add(booking);
		booking.setUser(this);

		return booking;
	}

	public Booking removeBooking(Booking booking) {
		getBookings().remove(booking);
		booking.setUser(null);

		return booking;
	}

	public List<UserLog> getUserLogs() {
		return this.userLogs;
	}

	public void setUserLogs(List<UserLog> userLogs) {
		this.userLogs = userLogs;
	}

	public UserLog addUserLog(UserLog userLog) {
		getUserLogs().add(userLog);
		userLog.setUser(this);

		return userLog;
	}

	public UserLog removeUserLog(UserLog userLog) {
		getUserLogs().remove(userLog);
		userLog.setUser(null);

		return userLog;
	}

	public ProofDetail getProofDetail() {
		return this.proofDetail;
	}

	public void setProofDetail(ProofDetail proofDetail) {
		this.proofDetail = proofDetail;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<VerificationToken> getVerificationTokens() {
		return this.verificationTokens;
	}

	public void setVerificationTokens(List<VerificationToken> verificationTokens) {
		this.verificationTokens = verificationTokens;
	}

	public VerificationToken addVerificationToken(VerificationToken verificationToken) {
		getVerificationTokens().add(verificationToken);
		verificationToken.setUser(this);

		return verificationToken;
	}

	public VerificationToken removeVerificationToken(VerificationToken verificationToken) {
		getVerificationTokens().remove(verificationToken);
		verificationToken.setUser(null);

		return verificationToken;
	}

	public List<Wallet> getWallets() {
		return this.wallets;
	}

	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}

	public Wallet addWallet(Wallet wallet) {
		getWallets().add(wallet);
		wallet.setUser(this);

		return wallet;
	}

	public Wallet removeWallet(Wallet wallet) {
		getWallets().remove(wallet);
		wallet.setUser(null);

		return wallet;
	}

}