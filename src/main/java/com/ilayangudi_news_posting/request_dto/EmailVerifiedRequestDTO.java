package com.ilayangudi_news_posting.request_dto;

public class EmailVerifiedRequestDTO {

	private String email;
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
