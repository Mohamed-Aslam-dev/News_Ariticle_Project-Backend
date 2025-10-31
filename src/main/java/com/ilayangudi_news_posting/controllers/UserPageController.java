package com.ilayangudi_news_posting.controllers;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.response_dto.ApiResponse;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.response_dto.UserDetailsResponseDTO;
import com.ilayangudi_news_posting.servicerepo.UserPageServiceRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserPageController {

	@Autowired
	private UserPageServiceRepository userPageServiceRepo;

	@Autowired
	private NewsImageAndVideoFile newsFileStore;

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

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("ஏற்கனவே நீங்கள் சுயவிவர புகைபடம்(Profile) பதிவேற்றவில்லை");
	}

	@GetMapping("/details")
	public ResponseEntity<UserDetailsResponseDTO> getUserDetails(Principal principal) {

		UserDetailsResponseDTO userDetails = userPageServiceRepo.getUserDetails(principal);

		return ResponseEntity.ok(userDetails);
	}

	@PatchMapping("/update-user-details")
	public ResponseEntity<String> updateUserDetails(Principal principal,
			@RequestBody UserDetailsResponseDTO updatedUser) {

		userPageServiceRepo.updateUserDetails(principal, updatedUser);
		return ResponseEntity.ok("User details updated successfully!");
	}

	@GetMapping("/news/published")
	public ResponseEntity<?> getLastOneMonthPublishedNewsData(Principal principal) {
		List<NewsResponseDTO> lastOneMonthPublishedNews = userPageServiceRepo
				.getLastOneMonthPublishedNewsData(principal);

		if (lastOneMonthPublishedNews.isEmpty()) {
			return ResponseEntity.ok(new ApiResponse<>(
					"நீங்கள் எந்த செய்தியும் வெளியிடப்படவில்லை / It looks like you haven’t published any posts.",
					null));
		}

		// ✅ Generate signed URLs separately if needed
		lastOneMonthPublishedNews.forEach(news -> {
			if (news.getImageOrVideoUrl() != null && !news.getImageOrVideoUrl().isEmpty()) {
				List<String> urls = newsFileStore.generateSignedUrls(news.getImageOrVideoUrl(), 60);
				news.setImageOrVideoUrl(urls);
			}

			if (news.getAuthorProfileUrl() != null && !news.getAuthorProfileUrl().isEmpty()) {
				String profileUrl = newsFileStore.generateSignedUrl(news.getAuthorProfileUrl(), 60);
				news.setAuthorProfileUrl(profileUrl);
			}
		});

		return ResponseEntity.ok(new ApiResponse<>("வெற்றி", lastOneMonthPublishedNews));
	}

	@GetMapping("/news/archived")
	public ResponseEntity<?> getLastOneMonthArchivedNewsData(Principal principal) {
		List<NewsResponseDTO> lastOneMonthArchievedNews = userPageServiceRepo
				.getLastOneMonthArchievedNewsData(principal);

		if (lastOneMonthArchievedNews.isEmpty()) {
			return ResponseEntity.ok(new ApiResponse<>(
					"நீங்கள் எந்த செய்தியும் காப்பக நிலையில் வைக்கவில்லை / It looks like you haven’t Archived any posts.",
					null));
		}

		// ✅ Generate signed URLs separately if needed
		lastOneMonthArchievedNews.forEach(news -> {
			if (news.getImageOrVideoUrl() != null && !news.getImageOrVideoUrl().isEmpty()) {
				List<String> urls = newsFileStore.generateSignedUrls(news.getImageOrVideoUrl(), 60);
				news.setImageOrVideoUrl(urls);
			}

			if (news.getAuthorProfileUrl() != null && !news.getAuthorProfileUrl().isEmpty()) {
				String profileUrl = newsFileStore.generateSignedUrl(news.getAuthorProfileUrl(), 60);
				news.setAuthorProfileUrl(profileUrl);
			}
		});

		return ResponseEntity.ok(new ApiResponse<>("வெற்றி", lastOneMonthArchievedNews));
	}

	@GetMapping("/news/draft")
	public ResponseEntity<?> getLastOneMonthDraftNewsData(Principal principal) {
		List<NewsResponseDTO> lastOneMonthDraftedNews = userPageServiceRepo.getLastOneMonthDraftNewsData(principal);

		if (lastOneMonthDraftedNews.isEmpty()) {
			return ResponseEntity.ok(new ApiResponse<>(
					"நீங்கள் எந்த செய்தியும் வரைவு நிலையில் வைக்கவில்லை / It looks like you haven’t Drafted any posts.",
					null));
		}

		// ✅ Generate signed URLs separately if needed
		lastOneMonthDraftedNews.forEach(news -> {
			if (news.getImageOrVideoUrl() != null && !news.getImageOrVideoUrl().isEmpty()) {
				List<String> urls = newsFileStore.generateSignedUrls(news.getImageOrVideoUrl(), 60);
				news.setImageOrVideoUrl(urls);
			}

			if (news.getAuthorProfileUrl() != null && !news.getAuthorProfileUrl().isEmpty()) {
				String profileUrl = newsFileStore.generateSignedUrl(news.getAuthorProfileUrl(), 60);
				news.setAuthorProfileUrl(profileUrl);
			}
		});

		return ResponseEntity.ok(new ApiResponse<>("வெற்றி", lastOneMonthDraftedNews));
	}

}
