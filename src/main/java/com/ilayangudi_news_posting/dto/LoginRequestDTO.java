package com.ilayangudi_news_posting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {

	@NotBlank(message = "பயனர் மின்னஞ்சல் அல்லது மொபைல் எண்ணை உள்ளிடவும்")
	private String emailOrPhone;
	@NotBlank(message = "கடவுச்சொல் காலியாக இருக்கக்கூடாது")
	@Size(min = 6, message = "கடவுச்சொல் குறைந்தது 6 எழுத்துகள் இருக்க வேண்டும்")
	private String password;

	public LoginRequestDTO() {

	}

	public LoginRequestDTO(@NotBlank(message = "பயனர் மின்னஞ்சல் அல்லது மொபைல் எண்ணை உள்ளிடவும்") String emailOrPhone,
			@NotBlank(message = "கடவுச்சொல் காலியாக இருக்கக்கூடாது") @Size(min = 6, message = "கடவுச்சொல் குறைந்தது 6 எழுத்துகள் இருக்க வேண்டும்") String password) {

		this.emailOrPhone = emailOrPhone;
		this.password = password;
	}

	public String getEmailOrPhone() {
		return emailOrPhone;
	}

	public void setEmailOrPhone(String emailOrPhone) {
		this.emailOrPhone = emailOrPhone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginRequestDTO [emailOrPhone=" + emailOrPhone + ", password=" + password + "]";
	}

}
