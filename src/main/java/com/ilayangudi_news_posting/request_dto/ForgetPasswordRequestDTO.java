package com.ilayangudi_news_posting.request_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgetPasswordRequestDTO {

	@NotBlank(message = "மின்னஞ்சல் அல்லது மொபைல் எண் உள்ளிடவும்")
	@Email(message = "சரியான மின்னஞ்சல் முகவரியை உள்ளிடவும்")
	private String emailOrPhone;

	public ForgetPasswordRequestDTO() {

	}

	public ForgetPasswordRequestDTO(@NotBlank(message = "மின்னஞ்சல் அல்லது மொபைல் எண் உள்ளிடவும்") String emailOrPhone) {

		this.emailOrPhone = emailOrPhone;
	}

	public String getEmailOrPhone() {
		return emailOrPhone;
	}

	public void setEmailOrPhone(String emailOrPhone) {
		this.emailOrPhone = emailOrPhone;
	}

	@Override
	public String toString() {
		return "ForgetPasswordRequestDTO [emailOrPhone=" + emailOrPhone + "]";
	}

}
