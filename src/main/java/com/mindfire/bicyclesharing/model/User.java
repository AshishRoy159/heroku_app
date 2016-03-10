package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the users database table.
 * 
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

	@Column(name = "rate_group_id")
	private String rateGroupId;

	@Column(name = "regisration_time")
	private Timestamp regisrationTime;

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
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the isApproved
	 */
	public Boolean getIsApproved() {
		return isApproved;
	}

	/**
	 * @param isApproved
	 *            the isApproved to set
	 */
	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the mobileNo
	 */
	public Long getMobileNo() {
		return mobileNo;
	}

	/**
	 * @param mobileNo
	 *            the mobileNo to set
	 */
	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the rateGroupId
	 */
	public String getRateGroupId() {
		return rateGroupId;
	}

	/**
	 * @param rateGroupId
	 *            the rateGroupId to set
	 */
	public void setRateGroupId(String rateGroupId) {
		this.rateGroupId = rateGroupId;
	}

	/**
	 * @return the regisrationTime
	 */
	public Timestamp getRegisrationTime() {
		return regisrationTime;
	}

	/**
	 * @param regisrationTime
	 *            the regisrationTime to set
	 */
	public void setRegisrationTime(Timestamp regisrationTime) {
		this.regisrationTime = regisrationTime;
	}

	/**
	 * @return the userAddress
	 */
	public String getUserAddress() {
		return userAddress;
	}

	/**
	 * @param userAddress
	 *            the userAddress to set
	 */
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	/**
	 * @return the bookings
	 */
	public List<Booking> getBookings() {
		return bookings;
	}

	/**
	 * @param bookings
	 *            the bookings to set
	 */
	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	/**
	 * 
	 * @param booking
	 *            set the booking to the current user
	 * @return booking
	 */
	public Booking addBooking(Booking booking) {
		getBookings().add(booking);
		booking.setUser(this);

		return booking;
	}

	/**
	 * 
	 * @param booking
	 *            remove booking when current user is removed
	 * @return
	 */
	public Booking removeBooking(Booking booking) {
		getBookings().remove(booking);
		booking.setUser(null);

		return booking;
	}

	/**
	 * @return the userLogs
	 */
	public List<UserLog> getUserLogs() {
		return userLogs;
	}

	/**
	 * @param userLogs
	 *            the userLogs to set
	 */
	public void setUserLogs(List<UserLog> userLogs) {
		this.userLogs = userLogs;
	}

	/**
	 * @param userLog
	 *            add user log related to the current user
	 * @return userLog
	 */
	public UserLog addUserLog(UserLog userLog) {
		getUserLogs().add(userLog);
		userLog.setUser(this);

		return userLog;
	}

	/**
	 * @param userLog
	 *            remove user log related to the current user
	 * @return userLog
	 */
	public UserLog removeUserLog(UserLog userLog) {
		getUserLogs().remove(userLog);
		userLog.setUser(null);

		return userLog;
	}

	/**
	 * @return the proofDetail
	 */
	public ProofDetail getProofDetail() {
		return proofDetail;
	}

	/**
	 * @param proofDetail
	 *            the proofDetail to set
	 */
	public void setProofDetail(ProofDetail proofDetail) {
		this.proofDetail = proofDetail;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the verificationTokens
	 */
	public List<VerificationToken> getVerificationTokens() {
		return verificationTokens;
	}

	/**
	 * @param verificationTokens
	 *            the verificationTokens to set
	 */
	public void setVerificationTokens(List<VerificationToken> verificationTokens) {
		this.verificationTokens = verificationTokens;
	}

	/**
	 * 
	 * @param verificationToken
	 *            set the current user to the verification token
	 * @return verificationToken
	 */
	public VerificationToken addVerificationToken(VerificationToken verificationToken) {
		getVerificationTokens().add(verificationToken);
		verificationToken.setUser(this);

		return verificationToken;
	}

	/**
	 * 
	 * @param verificationToken
	 *            set the current user to null and remove the verification token
	 * @return verificationToken
	 */
	public VerificationToken removeVerificationToken(VerificationToken verificationToken) {
		getVerificationTokens().remove(verificationToken);
		verificationToken.setUser(null);

		return verificationToken;
	}

	/**
	 * @return the wallets
	 */
	public List<Wallet> getWallets() {
		return wallets;
	}

	/**
	 * @param wallets
	 *            the wallets to set
	 */
	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}

	/**
	 * 
	 * @param wallet
	 *            set wallet to the current user
	 * @return wallet
	 */
	public Wallet addWallet(Wallet wallet) {
		getWallets().add(wallet);
		wallet.setUser(this);

		return wallet;
	}

	/**
	 * 
	 * @param wallet
	 *            remove wallet when the current user is deleted
	 * @return wallet
	 */
	public Wallet removeWallet(Wallet wallet) {
		getWallets().remove(wallet);
		wallet.setUser(null);

		return wallet;
	}
}
