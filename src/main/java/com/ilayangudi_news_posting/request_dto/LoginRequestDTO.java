package com.ilayangudi_news_posting.request_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {

	@NotBlank(message = "பயனர் மின்னஞ்சல் அல்லது மொபைல் எண்ணை உள்ளிடவும்")
	@Email(message = "சரியான மின்னஞ்சல் முகவரியை உள்ளிடவும்")
	private String emailOrPhone;
	@NotBlank(message = "கடவுச்சொல் காலியாக இருக்கக்கூடாது")
	@Size(min = 6, message = "கடவுச்சொல் குறைந்தது 6 எழுத்துகள் இருக்க வேண்டும்")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$", message = "கடவுச்சொல் குறைந்தது 1 பெரிய எழுத்து, 1 சிறிய எழுத்து, 1 எண் மற்றும் 1–3 சிறப்பு எழுத்துக்கள் கொண்டிருக்க வேண்டும்")
	private String password;
	
	private String captchaResponse;

	public LoginRequestDTO() {

	}

	public LoginRequestDTO(
			@NotBlank(message = "பயனர் மின்னஞ்சல் அல்லது மொபைல் எண்ணை உள்ளிடவும்") @Email(message = "சரியான மின்னஞ்சல் முகவரியை உள்ளிடவும்") String emailOrPhone,
			@NotBlank(message = "கடவுச்சொல் காலியாக இருக்கக்கூடாது") @Size(min = 6, message = "கடவுச்சொல் குறைந்தது 6 எழுத்துகள் இருக்க வேண்டும்") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$", message = "கடவுச்சொல் குறைந்தது 1 பெரிய எழுத்து, 1 சிறிய எழுத்து, 1 எண் மற்றும் 1–3 சிறப்பு எழுத்துக்கள் கொண்டிருக்க வேண்டும்") String password,
			String captchaResponse) {
		super();
		this.emailOrPhone = emailOrPhone;
		this.password = password;
		this.captchaResponse = captchaResponse;
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

	public String getCaptchaResponse() {
		return captchaResponse;
	}

	public void setCaptchaResponse(String captchaResponse) {
		this.captchaResponse = captchaResponse;
	}

	@Override
	public String toString() {
		return "LoginRequestDTO [emailOrPhone=" + emailOrPhone + ", password=" + password + ", captchaResponse="
				+ captchaResponse + "]";
	}

}
