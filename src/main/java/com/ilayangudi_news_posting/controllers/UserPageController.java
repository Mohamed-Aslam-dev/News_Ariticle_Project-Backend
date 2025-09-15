package com.ilayangudi_news_posting.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ilayangudi_news_posting.servicerepo.UserPageServiceRepository;

@RestController
@RequestMapping("/user")
public class UserPageController {

	@Autowired
	private UserPageServiceRepository userPageServiceRepo;

	@PatchMapping("/mod/profile")
	public ResponseEntity<String> changeUserProfile(@RequestPart("newProfile") MultipartFile newUserProfile,
			Principal principal) {

		userPageServiceRepo.changeUserProfilePicture(newUserProfile, principal);

		return ResponseEntity.ok("உங்கள் சுயவிவர புகைபடம்(Profile) வெற்றிகரமாக புதுப்பிக்கப்பட்டது");
	}

	@DeleteMapping("/del/profile")
	public ResponseEntity<String> deleteUserProfile(Principal principal) {

		boolean isDeleted = userPageServiceRepo.deleteUserProfilePicture(principal);

		if (isDeleted) {
			return ResponseEntity.ok("உங்கள் சுயவிவர புகைபடம்(Profile) வெற்றிகரமாக நீக்கப்பட்டது");

		}

		return ResponseEntity.ok("ஏற்கனவே நீங்கள் சுயவிவர புகைபடம்(Profile) பதிவேற்றவில்லை");

	}

}
