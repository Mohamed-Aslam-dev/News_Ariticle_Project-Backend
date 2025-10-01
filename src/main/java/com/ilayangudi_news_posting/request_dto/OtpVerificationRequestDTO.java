package com.ilayangudi_news_posting.request_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OtpVerificationRequestDTO {

	@NotBlank(message = "மின்னஞ்சல் காலியாக இருக்கக்கூடாது")
	@Email(message = "சரியான மின்னஞ்சல் முகவரியை உள்ளிடவும்")
	private String email;
	@NotBlank(message = "OTP காலியாக இருக்கக்கூடாது")
	@Pattern(regexp = "^[0-9]{6}$", message = "OTP 6 இலக்க எண்களாக இருக்க வேண்டும்")
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
