package com.ilayangudi_news_posting.controllers;

import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilayangudi_news_posting.configuration.AuthService;
import com.ilayangudi_news_posting.configuration.CaptchaService;
import com.ilayangudi_news_posting.configuration.JwtUtil;
import com.ilayangudi_news_posting.configuration.LoginAttemptService;
import com.ilayangudi_news_posting.message_services.OtpGenerateService;
import com.ilayangudi_news_posting.request_dto.EmailVerifiedRequestDTO;
import com.ilayangudi_news_posting.request_dto.ForgetPasswordDto;
import com.ilayangudi_news_posting.request_dto.ForgetPasswordRequestDTO;
import com.ilayangudi_news_posting.request_dto.LoginRequestDTO;
import com.ilayangudi_news_posting.request_dto.OtpVerificationRequestDTO;
import com.ilayangudi_news_posting.request_dto.UserRegisterDTO;
import com.ilayangudi_news_posting.servicerepo.UserRegisterDataServiceRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserRegisterDataController {

	@Autowired
	private UserRegisterDataServiceRepository userServiceRepo;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private Validator validator;

	@Autowired
	private OtpGenerateService otpService;

	@Autowired
	private LoginAttemptService loginAttemptService;

	@Autowired
	private CaptchaService captchaService;

	@PostMapping("/new-user")
	public ResponseEntity<String> addNewUser(@RequestPart("userRegisterData") String userDataJson,
			@RequestPart(value = "profilePic", required = false) MultipartFile profilePic) throws Exception {

		log.info("➡️ New user registration request received. Data={}", userDataJson);

		ObjectMapper mapper = new ObjectMapper();
		UserRegisterDTO userRegisterData = mapper.readValue(userDataJson, UserRegisterDTO.class);

		if (!userRegisterData.isEmailVerified()) {

			log.warn("❌ Email not verified for user: {}", userRegisterData.getEmailId());

			return new ResponseEntity<>(
					"மின்னஞ்சல் சரிபார்ப்பு(verify) செய்யப்படவில்லை மின்னஞ்சல் கட்டத்தில்(box) உள்ள verify பொத்தானை அழுத்தி சரிபார்ப்பு செய்யவும்",
					HttpStatus.BAD_REQUEST);
		}

		// Manual validation
		Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(userRegisterData);
		if (!violations.isEmpty()) {
			StringBuilder errors = new StringBuilder();
			for (ConstraintViolation<UserRegisterDTO> violation : violations) {
				errors.append(violation.getMessage()).append("\n");
			}
			return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
		}

		userServiceRepo.addNewUser(userRegisterData, profilePic);

		return new ResponseEntity<>("புதிய பயனர்(User) விவரம் சேகரிக்கப்பட்டது", HttpStatus.ACCEPTED);
	}

	@PostMapping("/send-otp")
	public ResponseEntity<String> sendOtp(@RequestBody EmailVerifiedRequestDTO emailVerifiedRequest) {
		userServiceRepo.newUserEmailVerified(emailVerifiedRequest.getEmail(), emailVerifiedRequest.getMobileNumber());
		return ResponseEntity
				.ok(emailVerifiedRequest.getEmail() + " இந்த மின்னஞ்சலுக்கு ஒரு முறை கடவுச்சொல்(OTP) அனுப்பபட்டது");
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequestDTO otpVerificationRequest)
			throws Exception {
		boolean isValid = otpService.verifyOtp(otpVerificationRequest.getEmail(), otpVerificationRequest.getOtp());

		if (!isValid) {

			return new ResponseEntity<>("OTP தவறானது அல்லது காலாவதியானது.", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("மின்னஞ்சல்(Email) சரிபார்ப்பு(verify) வெற்றிகரமாக முடிந்தது.",
				HttpStatus.ACCEPTED);
	}

	@PostMapping("/user-login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request, HttpServletResponse resp) {
	    String key = request.getEmailOrPhone();
	    log.info("➡️ Login attempt for key={}", key);

	    // 1️⃣ First check if blocked (before password check)
	    if (loginAttemptService.isBlocked(key)) {
	        log.warn("🚫 Login blocked due to too many attempts. key={}", key);
	        return ResponseEntity.status(429).body("Too many failed attempts. Try again later.");
	    }

	    // 2️⃣ Check CAPTCHA if required
	    if (loginAttemptService.shouldShowCaptcha(key)) {
	        if (!captchaService.validate(request.getCaptchaResponse())) {
	            log.error("🔒 CAPTCHA verification failed. key={} | captchaResponse={}", key, request.getCaptchaResponse());
	            return ResponseEntity.status(400).body("CAPTCHA verification failed");
	        }
	        log.info("✅ CAPTCHA passed. key={}", key);
	    }

	    try {
	        authManager.authenticate(new UsernamePasswordAuthenticationToken(key, request.getPassword()));

	        // success -> reset attempts
	        loginAttemptService.loginSucceeded(key);
	        log.info("✅ Login successful. key={}", key);

	        UserDetails userDetails = authService.loadUserByUsername(key);

	        String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
	        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

	        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken).httpOnly(true)
	                .secure(false)  // on prod change to true https
	                .path("/").sameSite("Lax").maxAge(60 * 60 * 24 * 30).build();
	        resp.addHeader("Set-Cookie", refreshCookie.toString());

	        return ResponseEntity.ok(Map.of("accessToken", accessToken));

	    } catch (BadCredentialsException e) {
	        // ❌ Increment attempts after fail
	        loginAttemptService.loginFailed(key);

	        log.error("❌ Invalid credentials. key={} | error={}", key, e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("உள்நுழைவதில் சிக்கல், உங்கள் மின்னஞ்சல்/மொபைல் எண் அல்லது கடவுச்சொல்லை(Password) சரிபார்க்கவும்");
	    } catch (Exception e) {
	        log.error("⚠️ Unexpected error during login. key={} | error={}", key, e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("உள்நுழைவதில் சிக்கல், உங்கள் மின்னஞ்சல்/மொபைல் எண் அல்லது கடவுச்சொல்லை(Password) சரிபார்க்கவும்");
	    }
	}


	@PostMapping("/forget-password/request")
	public ResponseEntity<String> forgetPasswordRequest(
			@Valid @RequestBody ForgetPasswordRequestDTO forgetPasswordRequest) {

		boolean exists = userServiceRepo.generateResetToken(forgetPasswordRequest);

		if (exists) {
			return ResponseEntity.ok("OTP அனுப்பபட்டது , மின்னஞ்சலை(Email) பார்க்கவும்");
		} else {
			return ResponseEntity.status(404).body(
					"உங்கள் மின்னஞ்சல்/தொலைபேசி எண் கிடைக்கவில்லை, நீங்கள் மீண்டும் புதிய-பயனர் பதிவு பக்கம் வழியாக உள்நுழையவும்");
		}

	}

	@PostMapping("/forget-password/reset")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody ForgetPasswordDto forgetPasswordDto) {
		boolean success = userServiceRepo.resetPasswordWithToken(forgetPasswordDto);

		if (success) {
			return ResponseEntity.ok("உங்களுடைய கடவுச்சொல்(Password) வெற்றிகரமாக மாற்றப்பட்டது");
		} else {
			return ResponseEntity.status(400).body("தவறான அல்லது காலாவதியான OTP");
		}
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		String refreshToken = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("refreshToken")) {
					refreshToken = cookie.getValue();
				}
			}
		}

		if (refreshToken == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token");
		}

		String username;
		try {
			username = jwtUtil.extractUsername(refreshToken);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
		}

		if (!jwtUtil.validateToken(refreshToken, username)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
		}

		String newAccessToken = jwtUtil.generateAccessToken(username);

		return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse resp) {
		ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "").httpOnly(true).secure(true).path("/")
				.sameSite("Strict").maxAge(0) // expire immediately
				.build();
		resp.addHeader("Set-Cookie", deleteCookie.toString());
		return ResponseEntity.ok("Logged out successfully");
	}

}
