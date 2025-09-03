package com.ilayangudi_news_posting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ilayangudi_news_posting.dto.LoginRequestDTO;
import com.ilayangudi_news_posting.dto.UserRegisterDTO;
import com.ilayangudi_news_posting.servicerepo.UserRegisterDataServiceRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class UserRegisterDataController {
	
	@Autowired
	private UserRegisterDataServiceRepository userServiceRepo;
	
	@PostMapping("/new-user")
	public ResponseEntity<String> addNewUser(@Valid @RequestBody UserRegisterDTO userDto){
		
		userServiceRepo.addNewUser(userDto);
		
		return new ResponseEntity<String>("புதிய பயனர் விவரம் சேகரிக்கப்பட்டது", HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/user-login")
	public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest){
		
		Boolean isValid = userServiceRepo.loginUser(loginRequest);
		
		if(isValid) {
	        return ResponseEntity.ok("உள்நுழைவு வெற்றிகரமாக நடைபெற்றது"); // Success in Tamil
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body("உள்நுழைவு தோல்வி. சரியான விவரங்களை உள்ளிடவும்"); // Failed in Tamil
	    }
		
	}

}
