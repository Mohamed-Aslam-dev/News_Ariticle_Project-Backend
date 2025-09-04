package com.ilayangudi_news_posting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ForgetPasswordDto {

	@NotBlank(message = "பயனர் மின்னஞ்சல் அல்லது மொபைல் எண்ணை உள்ளிடவும்")
	private String emailOrMobileNo;
	@NotBlank(message = "புதிய கடவுச்சொல்லை உள்ளிடவும்")
	@Size(min = 6, message = "கடவுச்சொல் குறைந்தது 6 எழுத்துகள் இருக்க வேண்டும்")
	private String newPassword;

	public ForgetPasswordDto() {

	}

	public ForgetPasswordDto(
			@NotBlank(message = "பயனர் மின்னஞ்சல் அல்லது மொபைல் எண்ணை உள்ளிடவும்") String emailOrMobileNo,
			@NotBlank(message = "புதிய கடவுச்சொல்லை உள்ளிடவும்") @Size(min = 6, message = "கடவுச்சொல் குறைந்தது 6 எழுத்துகள் இருக்க வேண்டும்") String newPassword) {
		super();
		this.emailOrMobileNo = emailOrMobileNo;
		this.newPassword = newPassword;
	}

	public String getEmailOrMobileNo() {
		return emailOrMobileNo;
	}

	public void setEmailOrMobileNo(String emailOrMobileNo) {
		this.emailOrMobileNo = emailOrMobileNo;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "ForgetPasswordDto [emailOrMobileNo=" + emailOrMobileNo + ", newPassword=" + newPassword + "]";
	}

}
