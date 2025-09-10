package com.ilayangudi_news_posting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilayangudi_news_posting.dto.NewsDataDTO;
import com.ilayangudi_news_posting.servicerepo.NewsDataServiceRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/news")
public class NewsDataController {
	
	@Autowired
    private NewsDataServiceRepository newsService;
	
	@PostMapping(value = "/upload")
	public ResponseEntity<?> uploadNews(
	        @RequestPart("newsData") String newsDataJson,
	        @RequestPart("file") MultipartFile file) throws Exception {

	    ObjectMapper mapper = new ObjectMapper();
	    NewsDataDTO newsDataDto = mapper.readValue(newsDataJson, NewsDataDTO.class);

	    newsService.addANewsData(newsDataDto, file);

	    return ResponseEntity.ok("செய்தி வெற்றிகரமாக சேமிக்கப்பட்டது!");
	}
	
	

}
