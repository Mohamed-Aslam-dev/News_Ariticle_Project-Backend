package com.ilayangudi_news_posting.servicerepo;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.ilayangudi_news_posting.request_dto.NewsDataDTO;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;

public interface NewsDataServiceRepository {
	
	public void addANewsData(NewsDataDTO newsDataDto, MultipartFile file, Principal principal);
	
	List<NewsResponseDTO> getNewsDataFromHomePage();

}
