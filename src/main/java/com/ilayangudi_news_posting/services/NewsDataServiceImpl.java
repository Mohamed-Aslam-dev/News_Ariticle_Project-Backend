package com.ilayangudi_news_posting.services;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.enums.NewsStatus;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.request_dto.NewsDataDTO;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.servicerepo.NewsDataServiceRepository;

@Service
public class NewsDataServiceImpl implements NewsDataServiceRepository{

	@Autowired
	private NewsDataRepository newsDataRepository;
	
	@Autowired
	private UserRegisterDataRepository userRegisterDataRepository;
	
	@Autowired
	private NewsImageAndVideoFile newsFileStore;
	
	public void addANewsData(NewsDataDTO newsDataDto, MultipartFile file, Principal principal) {
	    try {
	    	String uploadDir = "D:/Users/newsUploads/";
	        String imagePath = newsFileStore.getNewsImageAndVideoFilepath(file, uploadDir);

	        NewsData newsData = new NewsData();
	        newsData.setNewsTitle(newsDataDto.getNewsTitle());
	        newsData.setNewsDescription(newsDataDto.getNewsDescription());
	        newsData.setImageOrVideoUrl(imagePath);
	        newsData.setAuthor(principal.getName());
	        newsData.setCategory(newsDataDto.getCategory());
	        newsData.setTags(newsDataDto.getTags());
	        newsData.setStatus(newsDataDto.getStatus() != null ? newsDataDto.getStatus() : NewsStatus.PUBLISHED);

	        newsDataRepository.save(newsData);

	    } catch (IOException e) {
	        // log & rethrow as runtime so global handler can catch
	        throw new RuntimeException("Error while saving news file", e);
	    }
	}

	@Override
    public List<NewsResponseDTO> getNewsDataFromHomePage() {
        Pageable limit = PageRequest.of(0, 3);
        List<NewsData> latestNews = newsDataRepository.findTop3LatestNews(limit);

        return latestNews.stream().map(news -> {
            NewsResponseDTO dto = new NewsResponseDTO();
            dto.setNewsTitle(news.getNewsTitle());
            dto.setNewsDescription(news.getNewsDescription());
            dto.setImageOrVideoUrl(
                (news.getImageOrVideoUrl() != null && !news.getImageOrVideoUrl().isEmpty())
                    ? news.getImageOrVideoUrl()
                    : "/images/default-news.png"
            );
            dto.setAuthor(news.getAuthor());
            dto.setCategory(news.getCategory());
            dto.setTags(news.getTags().toString()); 
            dto.setStatus(news.getStatus().name());
            dto.setViews(news.getViews());
            dto.setLikes(news.getLikes());
            dto.setUnLikes(news.getUnLikes());
            dto.setCreatedAt(news.getCreatedAt());
            dto.setUpdatedAt(news.getUpdatedAt());
            return dto;
        }).toList();
    }

	

}
