package com.ilayangudi_news_posting.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	public ResponseEntity<?> uploadNews(
	        @RequestPart("newsData") String newsDataJson,
	        @RequestPart(value = "file", required = false) MultipartFile file, Principal principal) throws Exception {

	    ObjectMapper mapper = new ObjectMapper();
	    NewsDataDTO newsDataDto = mapper.readValue(newsDataJson, NewsDataDTO.class);
	    
	 // Manual validation
	    Set<ConstraintViolation<NewsDataDTO>> violations = validator.validate(newsDataDto);
	    if (!violations.isEmpty()) {
	        StringBuilder errors = new StringBuilder();
	        for (ConstraintViolation<NewsDataDTO> violation : violations) {
	            errors.append(violation.getMessage()).append("\n");
	        }
	        return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
	    }

	    newsService.addANewsData(newsDataDto, file, principal);

	    return ResponseEntity.ok("செய்தி வெற்றிகரமாக சேமிக்கப்பட்டது!");
	}
	
	@GetMapping(value = "/home", produces = "application/json" )
	public ResponseEntity<List<NewsResponseDTO>> getNewsDataFromHomePage() {
	    List<NewsResponseDTO> latestNews = newsService.getNewsDataFromHomePage();
	    return ResponseEntity.ok(latestNews);
	}
	
	

}
