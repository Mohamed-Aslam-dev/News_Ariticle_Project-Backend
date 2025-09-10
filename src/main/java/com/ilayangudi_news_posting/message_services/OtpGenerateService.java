package com.ilayangudi_news_posting.message_services;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class OtpGenerateService {
	
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int TOKEN_LENGTH = 6;
	
	public String generateToken() {
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(TOKEN_LENGTH);

		for (int i = 0; i < TOKEN_LENGTH; i++) {
			sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}
		return sb.toString();
	}

}
