package com.ilayangudi_news_posting.request_dto;

public class OtpVerificationRequestDTO {

	private String email;
	private String otp;

	public OtpVerificationRequestDTO() {

	}

	public OtpVerificationRequestDTO(String email, String otp) {

		this.email = email;
		this.otp = otp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "OtpVerificationRequestDTO [email=" + email + ", otp=" + otp + "]";
	}

}
