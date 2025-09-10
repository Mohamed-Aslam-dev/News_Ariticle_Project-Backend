package com.ilayangudi_news_posting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {

	
	@NotBlank(message = "பயனர் பெயர் காலியாக இருக்கக்கூடாது")
	private String userName;
	@NotBlank(message = "மின்னஞ்சல் காலியாக இருக்கக்கூடாது")
	@Email(message = "சரியான மின்னஞ்சல் முகவரியை உள்ளிடவும்")
	private String emailId;
	@NotBlank(message = "மொபைல் எண் காலியாக இருக்கக்கூடாது")
	@Pattern(regexp = "^[0-9]{10}$", message = "மொபைல் எண் 10 இலக்கமாக இருக்க வேண்டும்")
	private String userMobileNumber;
	@NotBlank(message = "கடவுச்சொல் காலியாக இருக்கக்கூடாது")
	@Size(min = 6, message = "கடவுச்சொல் குறைந்தது 6 எழுத்துகள் இருக்க வேண்டும்")
	private String password;

	public UserRegisterDTO() {

	}

	public UserRegisterDTO(@NotBlank(message = "பயனர் பெயர் காலியாக இருக்கக்கூடாது") String userName,
			@NotBlank(message = "மின்னஞ்சல் காலியாக இருக்கக்கூடாது") @Email(message = "சரியான மின்னஞ்சல் முகவரியை உள்ளிடவும்") String emailId,
			@NotBlank(message = "மொபைல் எண் காலியாக இருக்கக்கூடாது") @Pattern(regexp = "^[0-9]{10}$", message = "மொபைல் எண் 10 இலக்கமாக இருக்க வேண்டும்") String userMobileNumber,
			@NotBlank(message = "கடவுச்சொல் காலியாக இருக்கக்கூடாது") @Size(min = 6, message = "கடவுச்சொல் குறைந்தது 6 எழுத்துகள் இருக்க வேண்டும்") String password) {
		super();
		this.userName = userName;
		this.emailId = emailId;
		this.userMobileNumber = userMobileNumber;
		this.password = password;
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

	@Override
	public String toString() {
		return "UserRegisterDTO [userName=" + userName + ", emailId=" + emailId + ", userMobileNumber="
				+ userMobileNumber + ", password=" + password + "]";
	}

}
