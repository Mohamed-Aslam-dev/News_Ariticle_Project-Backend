package com.ilayangudi_news_posting.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ForgetPasswordDto {

	@NotBlank(message = "Reset Token அவசியம்")
	private String resetToken;
	@NotBlank(message = "புதிய கடவுச்சொல்லை உள்ளிடவும்")
	@Size(min = 6, message = "கடவுச்சொல் குறைந்தது 6 எழுத்துகள் இருக்க வேண்டும்")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$", message = "கடவுச்சொல் குறைந்தது 1 பெரிய எழுத்து, 1 சிறிய எழுத்து, 1 எண் மற்றும் 1–3 சிறப்பு எழுத்துக்கள் கொண்டிருக்க வேண்டும்")
	private String newPassword;

	public ForgetPasswordDto() {

	}

	public ForgetPasswordDto(@NotBlank(message = "Reset Token அவசியம்") String resetToken,
			@NotBlank(message = "புதிய கடவுச்சொல்லை உள்ளிடவும்") @Size(min = 6, message = "கடவுச்சொல் குறைந்தது 6 எழுத்துகள் இருக்க வேண்டும்") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$", message = "கடவுச்சொல் குறைந்தது 1 பெரிய எழுத்து, 1 சிறிய எழுத்து, 1 எண் மற்றும் 1–3 சிறப்பு எழுத்துக்கள் கொண்டிருக்க வேண்டும்") String newPassword) {
		this.resetToken = resetToken;
		this.newPassword = newPassword;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "ForgetPasswordDto [resetToken=" + resetToken + ", newPassword=" + newPassword + "]";
	}

}
