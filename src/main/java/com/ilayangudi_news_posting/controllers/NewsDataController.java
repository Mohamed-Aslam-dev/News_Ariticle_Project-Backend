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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilayangudi_news_posting.entity.NewsEngagedStatus;
import com.ilayangudi_news_posting.request_dto.NewsDataDTO;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.servicerepo.NewsDataServiceRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/news")
public class NewsDataController {

	@Autowired
	private NewsDataServiceRepository newsService;

	@Autowired
	private Validator validator;

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
			return new ResponseEntity<>("அதிகபட்சமாக 3 கோப்புகள்(images/videos) மட்டுமே அனுப்ப முடியும்", HttpStatus.BAD_REQUEST);
		}

		newsService.addANewsData(newsDataDto, files, principal);

		return ResponseEntity.ok("செய்தி வெற்றிகரமாக சேமிக்கப்பட்டது!");
	}

	@GetMapping(value = "/home", produces = "application/json")
	public ResponseEntity<List<NewsResponseDTO>> getNewsDataFromHomePage() {
		List<NewsResponseDTO> latestNews = newsService.getNewsDataFromHomePage();
		return ResponseEntity.ok(latestNews);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<NewsResponseDTO>> getLastOneMonthNewsData(){
		List<NewsResponseDTO> lastOneMonthNews = newsService.getLastOneMonthNewsData();
		return ResponseEntity.ok(lastOneMonthNews);
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
	public ResponseEntity<NewsEngagedStatus> addLike(@PathVariable Long id) {
		return ResponseEntity.ok(newsService.addNewsLike(id));
	}

	// 👎 Unlike increment
	@PatchMapping("/{id}/unlike")
	public ResponseEntity<NewsEngagedStatus> addUnLike(@PathVariable Long id) {
		return ResponseEntity.ok(newsService.addNewsUnLike(id));
	}

	// 👀 Views increment
	@PatchMapping("/{id}/views")
	public ResponseEntity<NewsEngagedStatus> addView(@PathVariable Long id) {
		return ResponseEntity.ok(newsService.addNewsViews(id));
	}

}
