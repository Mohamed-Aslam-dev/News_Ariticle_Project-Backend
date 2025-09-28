package com.ilayangudi_news_posting.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.ilayangudi_news_posting.enums.UserAccountStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class UserRegisterData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String profilePicUrl;
	private String userName;
	private String emailId;
	private String userMobileNumber;
	private String password;
	private String role;
	private String resetToken;
	@Enumerated(EnumType.STRING)
	private UserAccountStatus accountStatus;
	private LocalDateTime suspendedAt;
	private LocalDateTime bannedAt;
	private LocalDateTime resetTokenExpiry;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public UserRegisterData() {

	}

	public UserRegisterData(Long id, String profilePicUrl, String userName, String emailId, String userMobileNumber,
			String password, String role, String resetToken, UserAccountStatus accountStatus, LocalDateTime suspendedAt,
			LocalDateTime bannedAt, LocalDateTime resetTokenExpiry, Date createdAt) {
		super();
		this.id = id;
		this.profilePicUrl = profilePicUrl;
		this.userName = userName;
		this.emailId = emailId;
		this.userMobileNumber = userMobileNumber;
		this.password = password;
		this.role = role;
		this.resetToken = resetToken;
		this.accountStatus = accountStatus;
		this.suspendedAt = suspendedAt;
		this.bannedAt = bannedAt;
		this.resetTokenExpiry = resetTokenExpiry;
		this.createdAt = createdAt;
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

	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public LocalDateTime getResetTokenExpiry() {
		return resetTokenExpiry;
	}

	public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
		this.resetTokenExpiry = resetTokenExpiry;
	}
	
	public UserAccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(UserAccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	public LocalDateTime getSuspendedAt() {
		return suspendedAt;
	}

	public void setSuspendedAt(LocalDateTime suspendedAt) {
		this.suspendedAt = suspendedAt;
	}

	public LocalDateTime getBannedAt() {
		return bannedAt;
	}

	public void setBannedAt(LocalDateTime bannedAt) {
		this.bannedAt = bannedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "UserRegisterData [id=" + id + ", profilePicUrl=" + profilePicUrl + ", userName=" + userName
				+ ", emailId=" + emailId + ", userMobileNumber=" + userMobileNumber + ", password=" + password
				+ ", role=" + role + ", resetToken=" + resetToken + ", accountStatus=" + accountStatus
				+ ", suspendedAt=" + suspendedAt + ", bannedAt=" + bannedAt + ", resetTokenExpiry=" + resetTokenExpiry
				+ ", createdAt=" + createdAt + "]";
	}

}
