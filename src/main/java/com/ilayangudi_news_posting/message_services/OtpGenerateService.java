package com.ilayangudi_news_posting.message_services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ilayangudi_news_posting.repository.OtpRepository;
import com.ilayangudi_news_posting.request_dto.UserRegisterDTO;

@Service
public class OtpGenerateService {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int TOKEN_LENGTH = 6;

	@Autowired
	private OtpRepository otpRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailSenderService emailSenderService;

	public String generateToken() {
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(TOKEN_LENGTH);

		for (int i = 0; i < TOKEN_LENGTH; i++) {
			sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}
		return sb.toString();
	}

	public String generateOtp(String email) {
		String otp = String.valueOf(new SecureRandom().nextInt(900000) + 100000);
		// optional: encode
		String hashedOtp = passwordEncoder.encode(otp);

		OtpData otpData = otpRepo.findByEmail(email).orElseGet(OtpData::new);
		otpData.setEmail(email);
		otpData.setOtp(hashedOtp);
		otpData.setCreatedAt(LocalDateTime.now());
		otpRepo.save(otpData);

		// send email
		emailSenderService.sendEmailFromRegisterVerify(email, otp);
		return otp;
	}

	public boolean verifyOtp(String email, String otp) {
		Optional<OtpData> optional = otpRepo.findByEmail(email);
		if (optional.isEmpty())
			return false;

		OtpData data = optional.get();

		if (data.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
			otpRepo.delete(data);
			return false; // expired
		}

		boolean isValid = passwordEncoder.matches(otp, data.getOtp());

		if (isValid)
			otpRepo.delete(data); // one-time use

		return isValid;
	}

}
