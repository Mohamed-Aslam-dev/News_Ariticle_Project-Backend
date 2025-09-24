package com.ilayangudi_news_posting.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.request_dto.NewsDataDTO;
import com.ilayangudi_news_posting.request_dto.NewsReportDTO;
import com.ilayangudi_news_posting.response_dto.ApiResponse;
import com.ilayangudi_news_posting.response_dto.LikeResponseDTO;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.response_dto.UnlikeResponseDTO;
import com.ilayangudi_news_posting.response_dto.ViewedResponseDTO;
import com.ilayangudi_news_posting.servicerepo.NewsDataServiceRepository;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/news")
public class NewsDataController {

	@Autowired
	private NewsDataServiceRepository newsService;

	@Autowired
	private Validator validator;
	
	@Autowired
	private NewsImageAndVideoFile newsFileStore;


	@PostMapping(value = "/upload")
	public ResponseEntity<?> uploadNews(@RequestPart("newsData") String newsDataJson,
			@RequestPart(value = "file", required = false) MultipartFile[] files, Principal principal)
			throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		NewsDataDTO newsDataDto = mapper.readValue(newsDataJson, NewsDataDTO.class);

		// ✅ Manual validation
		Set<ConstraintViolation<NewsDataDTO>> violations = validator.validate(newsDataDto);
		if (!violations.isEmpty()) {
			List<String> errors = new ArrayList<>();
			for (ConstraintViolation<NewsDataDTO> violation : violations) {
				errors.add(violation.getMessage());
			}
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}

		// ✅ File validations
		if (files != null && files.length > 3) {
			return new ResponseEntity<>("அதிகபட்சமாக 3 கோப்புகள்(images/videos) மட்டுமே அனுப்ப முடியும்",
					HttpStatus.BAD_REQUEST);
		}

		newsService.addANewsData(newsDataDto, files, principal);

		return ResponseEntity.ok("செய்தி வெற்றிகரமாக சேமிக்கப்பட்டது!");
	}

	@GetMapping(value = "/home", produces = "application/json")
	public ResponseEntity<?> getNewsDataFromHomePage() {
		List<NewsResponseDTO> latestNews = newsService.getNewsDataFromHomePage();
		if (latestNews.isEmpty()) {
			return ResponseEntity.ok(new ApiResponse<>("எந்த செய்தியும் இல்லை", null));
		}
		
		// ✅ Generate signed URLs separately if needed
	    latestNews.forEach(news -> {
	        if (news.getImageOrVideoUrl() != null && !news.getImageOrVideoUrl().isEmpty()) {
	            List<String> urls = newsFileStore.generateSignedUrls(news.getImageOrVideoUrl(), 60);
	            news.setImageOrVideoUrl(urls);
	        }
	    });

		return ResponseEntity.ok(new ApiResponse<>("வெற்றி", latestNews));
	}

	@GetMapping("/all")
	public ResponseEntity<?> getLastOneMonthNewsData(Principal principal) {
		List<NewsResponseDTO> lastOneMonthNews = newsService.getLastOneMonthNewsData(principal.getName());
		if (lastOneMonthNews.isEmpty()) {
			return ResponseEntity.ok(new ApiResponse<>("எந்த செய்தியும் இல்லை", null));
		}
		
		// ✅ Generate signed URLs separately if needed
		lastOneMonthNews.forEach(news -> {
	        if (news.getImageOrVideoUrl() != null && !news.getImageOrVideoUrl().isEmpty()) {
	            List<String> urls = newsFileStore.generateSignedUrls(news.getImageOrVideoUrl(), 60);
	            news.setImageOrVideoUrl(urls);
	        }
	    });

		return ResponseEntity.ok(new ApiResponse<>("வெற்றி", lastOneMonthNews));
	}


//	@PutMapping("/update-news/{id}")
//	public ResponseEntity<?> updateNewsData(@PathVariable Long id,
//	                                        @Valid @RequestBody NewsDataDTO dto) {
//	    NewsData existing = newsDataRepository.findById(id)
//	            .orElseThrow(() -> new RuntimeException("News not found with id: " + id));
//
//	    // ✅ Update only fields from DTO
//	    existing.setNewsTitle(dto.getNewsTitle());
//	    existing.setNewsDescription(dto.getNewsDescription());
//	    existing.setCategory(dto.getCategory());
//	    existing.setTags(dto.getTags());
//	    existing.setStatus(dto.getStatus());
//
//	    // Auto fields (views, likes, reports, createdAt) untouched
//	    newsDataRepository.save(existing);
//
//	    return ResponseEntity.ok("News updated successfully!");
//	}

	// 👍 Like increment
	@PatchMapping("/{id}/like")
	public ResponseEntity<LikeResponseDTO> toggleLike(@PathVariable Long id, Principal principal) {
		return ResponseEntity.ok(newsService.toggleLike(id, principal.getName()));
	}

	// 👎 Unlike increment
	@PatchMapping("/{id}/unlike")
	public ResponseEntity<UnlikeResponseDTO> addUnLike(@PathVariable Long id, Principal principal) {
		return ResponseEntity.ok(newsService.toggleUnLike(id, principal.getName()));
	}

	// 👀 Views increment
	@PatchMapping("/{id}/views")
	public ResponseEntity<ViewedResponseDTO> addView(@PathVariable Long id, Principal principal) {
		return ResponseEntity.ok(newsService.addView(id, principal.getName()));
	}
	
	@PostMapping("/{id}/report")
	public ResponseEntity<?> addReport(@PathVariable Long id, @Valid @RequestBody NewsReportDTO newsReportData, Principal principal){
	    
		System.out.println("DEBUG Content = " + newsReportData.getReportContent());
	    boolean isReported = newsService.addNewsReport(id, newsReportData, principal);
	    if(!isReported) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                             .body("You have already reported this news!");
	    }
	    
	    return ResponseEntity.ok("Your report has been saved successfully");
	}

	@PatchMapping("/post/{id}/archived")
	public ResponseEntity<String> postMoveToArchive(@PathVariable Long id, Principal principal) {
		boolean isArchived = newsService.newsPostMoveToArchive(id, principal);

		if (isArchived) {
			return ResponseEntity.ok("செய்தி வெற்றிகரமாக காப்பகத்திற்கு(Archived) மாற்றப்பட்டது.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("இது நீங்கள் உருவாக்கிய செய்தி அல்ல அல்லது செய்தி இல்லை.");
		}
	}

	@PatchMapping("/post/{id}/draft")
	public ResponseEntity<String> postMoveToDraft(@PathVariable Long id, Principal principal) {
		boolean isDrafted = newsService.newsPostMoveToDraft(id, principal);

		if (isDrafted) {
			return ResponseEntity.ok("செய்தி வெற்றிகரமாக வரைவு (Draft) நிலைக்கு மாற்றப்பட்டது.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("இது நீங்கள் உருவாக்கிய செய்தி அல்ல அல்லது செய்தி இல்லை.");
		}
	}

	@PatchMapping("/post/{id}/published")
	public ResponseEntity<String> postMoveToPublished(@PathVariable Long id, Principal principal) {
		boolean isPublished = newsService.newsPostMoveToPublished(id, principal);

		if (isPublished) {
			return ResponseEntity.ok("செய்தி வெற்றிகரமாக வெளியிடப்பட்ட (Published) நிலைக்கு மாற்றப்பட்டது.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("இது நீங்கள் உருவாக்கிய செய்தி அல்ல அல்லது செய்தி இல்லை.");
		}
	}

}
