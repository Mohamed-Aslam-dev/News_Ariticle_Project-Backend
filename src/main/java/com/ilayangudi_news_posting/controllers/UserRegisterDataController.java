package com.ilayangudi_news_posting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ilayangudi_news_posting.configuration.AuthService;
import com.ilayangudi_news_posting.configuration.JwtUtil;
import com.ilayangudi_news_posting.dto.ForgetPasswordDto;
import com.ilayangudi_news_posting.dto.LoginRequestDTO;
import com.ilayangudi_news_posting.dto.UserRegisterDTO;
import com.ilayangudi_news_posting.servicerepo.UserRegisterDataServiceRepository;

import jakarta.validation.Valid;

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
	
	@PostMapping("/new-user")
	public ResponseEntity<String> addNewUser(@Valid @RequestBody UserRegisterDTO userDto){
		
		userServiceRepo.addNewUser(userDto);
		
		return new ResponseEntity<String>("புதிய பயனர் விவரம் சேகரிக்கப்பட்டது", HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/user-login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            // Authenticate
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmailOrPhone(), request.getPassword())
            );

            // Load UserDetails
            UserDetails userDetails = authService.loadUserByUsername(request.getEmailOrPhone());

            // Generate JWT
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("உள்நுழைவதில் சிக்கல் உங்களுடைய மின்னஞ்சல்/தொலைபேசி எண் அல்லது கட்வுச்சொல்லை சரிபார்க்கவும்");
        }
    }
	
	@PostMapping("/user-forgetpassword")
	public ResponseEntity<String> forgetPassword(@RequestBody ForgetPasswordDto forgetPasswordDto){
		
		boolean isValid = userServiceRepo.forgetPassword(forgetPasswordDto);
		
		if(isValid) {
			return ResponseEntity.ok("உங்களுடைய கடவுச்சொல் மாற்றப்பட்டது");
		}
		else {
			return ResponseEntity.status(401).body("உங்களுடைய மின்னஞ்சல்/தொலைபேசி எண் சரிபார்க்கவும் அல்லது புதிய கணக்கை தொடங்கவும்");
		}
		
	}

}
