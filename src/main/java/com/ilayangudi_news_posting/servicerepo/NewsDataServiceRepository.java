package com.ilayangudi_news_posting.servicerepo;

import org.springframework.web.multipart.MultipartFile;

import com.ilayangudi_news_posting.dto.NewsDataDTO;

public interface NewsDataServiceRepository {
	
	public void addANewsData(NewsDataDTO newsDataDto, MultipartFile file);

}
