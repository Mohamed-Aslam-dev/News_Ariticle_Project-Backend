package com.ilayangudi_news_posting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserRegisterData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userName;
	private String emailId;
	private String userMobileNumber;
	private String password;
	private String role;

	public UserRegisterData() {

	}

	public UserRegisterData(Long id, String userName, String emailId, String userMobileNumber, String password,
			String role) {

		this.id = id;
		this.userName = userName;
		this.emailId = emailId;
		this.userMobileNumber = userMobileNumber;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserRegisterData [id=" + id + ", userName=" + userName + ", emailId=" + emailId + ", userMobileNumber="
				+ userMobileNumber + ", password=" + password + ", role=" + role + "]";
	}

}
