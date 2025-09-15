package com.ilayangudi_news_posting.message_services;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OtpData {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(unique = true)
	private String email;
	private String otp;
	private LocalDateTime createdAt;

	public OtpData() {

	}

	public OtpData(String email, String otp, LocalDateTime createdAt) {
		super();
		this.email = email;
		this.otp = otp;
		this.createdAt = createdAt;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "OtpData [email=" + email + ", otp=" + otp + ", createdAt=" + createdAt + "]";
	}

}
