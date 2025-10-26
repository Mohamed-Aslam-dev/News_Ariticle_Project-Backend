package com.ilayangudi_news_posting.response_dto;

import com.ilayangudi_news_posting.enums.UserAccountStatus;

public class UserDetailsResponseDTO {

	private Long id;
	private String profilePicUrl;
	private String userName;
	private String emailId;
	private String userMobileNumber;
	private UserAccountStatus accountStatus;

	public UserDetailsResponseDTO() {

	}

	public UserDetailsResponseDTO(Long id, String profilePicUrl, String userName, String emailId,
			String userMobileNumber, UserAccountStatus accountStatus) {
		this.id = id;
		this.profilePicUrl = profilePicUrl;
		this.userName = userName;
		this.emailId = emailId;
		this.userMobileNumber = userMobileNumber;
		this.accountStatus = accountStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
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

	public UserAccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(UserAccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	@Override
	public String toString() {
		return "UserDetailsResponseDTO [id=" + id + ", profilePicUrl=" + profilePicUrl + ", userName=" + userName
				+ ", emailId=" + emailId + ", userMobileNumber=" + userMobileNumber + ", accountStatus=" + accountStatus
				+ "]";
	}

}
