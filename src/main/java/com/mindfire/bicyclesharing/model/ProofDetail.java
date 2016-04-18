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
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the proof_details database table.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Entity
@Table(name = "proof_details")
@NamedQuery(name = "ProofDetail.findAll", query = "SELECT p FROM ProofDetail p")
public class ProofDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "proof_id")
	private Long proofId;

	private String document;

	@Column(name = "proof_no", unique = true)
	private String proofNo;

	@Column(name = "proof_type")
	private String proofType;

	// bi-directional many-to-one association to User
	@OneToMany(mappedBy = "proofDetail")
	private List<User> users;

	public ProofDetail() {
	}

	/**
	 * @return the proofId
	 */
	public Long getProofId() {
		return proofId;
	}

	/**
	 * @return the document
	 */
	public String getDocument() {
		return document;
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocument(String document) {
		this.document = document;
	}

	/**
	 * @return the proofNo
	 */
	public String getProofNo() {
		return proofNo;
	}

	/**
	 * @param proofNo
	 *            the proofNo to set
	 */
	public void setProofNo(String proofNo) {
		this.proofNo = proofNo;
	}

	/**
	 * @return the proofType
	 */
	public String getProofType() {
		return proofType;
	}

	/**
	 * @param proofType
	 *            the proofType to set
	 */
	public void setProofType(String proofType) {
		this.proofType = proofType;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * @param proofId
	 *            the proofId to set
	 */
	public void setProofId(Long proofId) {
		this.proofId = proofId;
	}

	/**
	 * @param user
	 *            add user's proof details to the current user
	 * @return user
	 */
	public User addUser(User user) {
		getUsers().add(user);
		user.setProofDetail(this);

		return user;
	}

	/**
	 * @param user
	 *            remove user's proof details related to the current user
	 * @return user
	 */
	public User removeUser(User user) {
		getUsers().remove(user);
		user.setProofDetail(null);

		return user;
	}
}
