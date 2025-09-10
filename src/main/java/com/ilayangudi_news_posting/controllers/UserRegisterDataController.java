package com.ilayangudi_news_posting.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
import com.ilayangudi_news_posting.configuration.JwtUtil;
import com.ilayangudi_news_posting.dto.ForgetPasswordDto;
import com.ilayangudi_news_posting.dto.ForgetPasswordRequestDTO;
import com.ilayangudi_news_posting.dto.LoginRequestDTO;
import com.ilayangudi_news_posting.dto.UserRegisterDTO;
import com.ilayangudi_news_posting.servicerepo.UserRegisterDataServiceRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

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

	@PostMapping("/new-user")
	public ResponseEntity<String> addNewUser(
	        @RequestPart("userRegisterData") String userDataJson,
	        @RequestPart(value = "profilePic", required = false) MultipartFile profilePic
	) throws Exception {

	    ObjectMapper mapper = new ObjectMapper();
	    UserRegisterDTO userRegisterData = mapper.readValue(userDataJson, UserRegisterDTO.class);

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

	    return new ResponseEntity<>("புதிய பயனர் விவரம் சேகரிக்கப்பட்டது", HttpStatus.ACCEPTED);
	}


	@PostMapping("/user-login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
		try {
			// Authenticate
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmailOrPhone(), request.getPassword()));

			// Load UserDetails
			UserDetails userDetails = authService.loadUserByUsername(request.getEmailOrPhone());

			// Generate JWT
			String token = jwtUtil.generateToken(userDetails.getUsername());

			return ResponseEntity.ok(token);
		} catch (Exception e) {
			return ResponseEntity.status(401)
					.body("உள்நுழைவதில் சிக்கல் உங்களுடைய மின்னஞ்சல்/தொலைபேசி எண் அல்லது கட்வுச்சொல்லை சரிபார்க்கவும்");
		}
	}

	@PostMapping("/forget-password/request")
	public ResponseEntity<String> forgetPasswordRequest(@Valid @RequestBody ForgetPasswordRequestDTO forgetPasswordRequest) {

		boolean exists = userServiceRepo.generateResetToken(forgetPasswordRequest);

		if (exists) {
			return ResponseEntity.ok("பயனர் உள்ளது, கடவுச்சொல்லை மாற்றும் பக்கத்திற்கு செல்லவும்");
		} else {
			return ResponseEntity.status(404).body(
					"உங்கள் மின்னஞ்சல்/தொலைபேசி எண் கிடைக்கவில்லை, நீங்கள் மீண்டும் உள்நுழைவு பக்கம் வழியாக உள்நுழையவும்");
		}

	}

	@PostMapping("/forget-password/reset")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody ForgetPasswordDto forgetPasswordDto) {
		boolean success = userServiceRepo.resetPasswordWithToken(forgetPasswordDto);

		if (success) {
			return ResponseEntity.ok("உங்களுடைய கடவுச்சொல் வெற்றிகரமாக மாற்றப்பட்டது");
		} else {
			return ResponseEntity.status(400).body("தவறான அல்லது காலாவதியான Token");
		}
	}

}
