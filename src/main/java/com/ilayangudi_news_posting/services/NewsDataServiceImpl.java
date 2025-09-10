package com.ilayangudi_news_posting.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ilayangudi_news_posting.dto.NewsDataDTO;
import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.servicerepo.NewsDataServiceRepository;

@Service
public class NewsDataServiceImpl implements NewsDataServiceRepository{

	@Autowired
	private NewsDataRepository newsDataRepository;
	
	@Autowired
	private NewsImageAndVideoFile newsFileStore;
	
	public void addANewsData(NewsDataDTO newsDataDto, MultipartFile file) {
	    try {
	    	String uploadDir = "D:/Users/newsUploads/";
	        String imagePath = newsFileStore.getNewsImageAndVideoFilepath(file, uploadDir);

	        NewsData newsData = new NewsData();
	        newsData.setNewsTitle(newsDataDto.getNewsTitle());
	        newsData.setNewsDescription(newsDataDto.getNewsDescription());
	        newsData.setImageOrVideoUrl(imagePath);

	        newsDataRepository.save(newsData);

	    } catch (IOException e) {
	        // log & rethrow as runtime so global handler can catch
	        throw new RuntimeException("Error while saving news file", e);
	    }
	}


}
