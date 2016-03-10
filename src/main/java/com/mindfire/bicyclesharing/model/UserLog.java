package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the user_logs database table.
 * 
 */
@Entity
@Table(name = "user_logs")
@NamedQuery(name = "UserLog.findAll", query = "SELECT u FROM UserLog u")
public class UserLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "login_time")
	private Timestamp loginTime;

	@Column(name = "logout_time")
	private Timestamp logoutTime;

	@Column(name = "pick_up_point_id")
	private String pickUpPointId;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public UserLog() {
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the loginTime
	 */
	public Timestamp getLoginTime() {
		return loginTime;
	}

	/**
	 * @param loginTime
	 *            the loginTime to set
	 */
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * @return the logoutTime
	 */
	public Timestamp getLogoutTime() {
		return logoutTime;
	}

	/**
	 * @param logoutTime
	 *            the logoutTime to set
	 */
	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}

	/**
	 * @return the pickUpPointId
	 */
	public String getPickUpPointId() {
		return pickUpPointId;
	}

	/**
	 * @param pickUpPointId
	 *            the pickUpPointId to set
	 */
	public void setPickUpPointId(String pickUpPointId) {
		this.pickUpPointId = pickUpPointId;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
