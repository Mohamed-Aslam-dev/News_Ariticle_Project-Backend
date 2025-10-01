package com.ilayangudi_news_posting.request_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EmailVerifiedRequestDTO {

	@NotBlank(message = "மின்னஞ்சல் காலியாக இருக்கக்கூடாது")
	@Email(message = "சரியான மின்னஞ்சல் முகவரியை உள்ளிடவும்")
	private String email;
	@NotBlank(message = "மொபைல் எண் காலியாக இருக்கக்கூடாது")
	@Pattern(regexp = "^[0-9]{10}$", message = "மொபைல் எண் 10 இலக்கமாக இருக்க வேண்டும்")
	private String mobileNumber;

	public EmailVerifiedRequestDTO() {

	}

	public EmailVerifiedRequestDTO(String email, String mobileNumber) {

		this.email = email;
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String toString() {
		return "EmailVerifiedRequestDTO [email=" + email + ", mobileNumber=" + mobileNumber + "]";
	}

}
