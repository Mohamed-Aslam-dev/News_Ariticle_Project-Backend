package com.ilayangudi_news_posting.controllers;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ilayangudi_news_posting.enums.ReportStatus;
import com.ilayangudi_news_posting.enums.UserAccountStatus;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.response_dto.UserDetailsResponseDTO;
import com.ilayangudi_news_posting.servicerepo.SuperAdminServiceRepository;

@RestController
@RequestMapping("/super/v1")
public class SuperAdminController {

	@Autowired
	private SuperAdminServiceRepository superAdminServiceRepo;

	@Autowired
	private NewsImageAndVideoFile newsFileStore;

	@GetMapping("/auth/user-data")
	public ResponseEntity<?> getAllDatasForSuperAdmin(Principal principal) {

		return ResponseEntity.ok(superAdminServiceRepo.getAllDatasForSuperAdmin(principal.getName()));

	}

	@GetMapping("/auth/user-reports")
	public ResponseEntity<?> getAllReportDatasForSuperAdmin(Principal principal) {

		return ResponseEntity.ok(superAdminServiceRepo.getAllReportsDataForSuperAdmin(principal.getName()));

	}

	@PatchMapping("/auth/{id}/status")
	public ResponseEntity<?> changeUserReportStatus(@PathVariable Long id, @RequestParam ReportStatus status,
			Principal principal) {

		boolean isChanged = superAdminServiceRepo.changeReportStatusFromSuperAdmin(id, status, principal.getName());

		if (!isChanged) {
			return ResponseEntity.ok("Doesn't change to Report Status");
		}

		return ResponseEntity.ok("Change To Report Status " + status);
	}

	@GetMapping("/auth/search-by-news")
	public ResponseEntity<?> searchByNews(@RequestParam String query) {
		List<NewsResponseDTO> results = superAdminServiceRepo.searchNews(query);

		if (results.isEmpty()) {
			return ResponseEntity.status(404).body("No matching news found for: " + query);
		}

		// ✅ Generate signed URLs separately if needed
		results.forEach(news -> {
			if (news.getImageOrVideoUrl() != null && !news.getImageOrVideoUrl().isEmpty()) {
				List<String> urls = newsFileStore.generateSignedUrls(news.getImageOrVideoUrl(), 60);
				news.setImageOrVideoUrl(urls);
			}

			if (news.getAuthorProfileUrl() != null && !news.getAuthorProfileUrl().isEmpty()) {
				String profileUrl = newsFileStore.generateSignedUrl(news.getAuthorProfileUrl(), 60);
				news.setAuthorProfileUrl(profileUrl);
			}
		});

		return ResponseEntity.ok(results);
	}

	@DeleteMapping("/auth/delete-news/{id}")
	public ResponseEntity<String> deleteNews(@PathVariable Long id, @RequestParam(required = false) String reason,
			Principal principal) {
		boolean deleted = superAdminServiceRepo.deleteNewsData(id, principal, reason);

		if (deleted) {
			return ResponseEntity.ok("No " + id + " செய்தி வெற்றிகரமாக நீக்கப்பட்டது!");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete news. Please try again.");
		}
	}

	@GetMapping("/auth/search-by-user")
	public ResponseEntity<?> searchUser(@RequestParam String keyword, Principal principal) {
		List<UserDetailsResponseDTO> users = superAdminServiceRepo.searchUser(keyword, principal);
		return ResponseEntity.ok(users);
	}

	@PatchMapping("/auth/{id}/user-status")
	public ResponseEntity<?> changeUserStatus(@PathVariable Long id, @RequestParam UserAccountStatus status,
			Principal principal, @RequestParam String reason) {

		String message = superAdminServiceRepo.changeAccountStatusFromSuperAdmin(id, status, principal.getName(),
				reason);

		return ResponseEntity.ok(message);
	}

}
