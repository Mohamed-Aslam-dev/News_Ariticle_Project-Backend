package com.ilayangudi_news_posting.services;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
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
	private NewsImageAndVideoFile newsFileStore;
	
	public void addANewsData(NewsDataDTO newsDataDto, MultipartFile[] files, Principal principal) {
	    try {
	        String uploadDir = "D:/Users/newsUploads/";
	        List<String> imagePaths = newsFileStore.getNewsImageAndVideoFilepaths(files, uploadDir);

	        NewsData newsData = new NewsData();
	        newsData.setNewsTitle(newsDataDto.getNewsTitle());
	        newsData.setNewsDescription(newsDataDto.getNewsDescription());
	        newsData.setImageOrVideoUrl(imagePaths); // ✅ multiple files
	        newsData.setAuthor(principal.getName());
	        newsData.setCategory(newsDataDto.getCategory());
	        newsData.setTags(newsDataDto.getTags());
	        newsData.setStatus(newsDataDto.getStatus() != null ? newsDataDto.getStatus() : NewsStatus.PUBLISHED);

	        newsDataRepository.save(newsData);

	    } catch (IOException e) {
	        throw new RuntimeException("Error while saving news file", e);
	    }
	}
	
	@Override
	public List<NewsResponseDTO> getNewsDataFromHomePage() {
	    Pageable limit = PageRequest.of(0, 4);
	    List<NewsData> latestNews = newsDataRepository.findTop4LatestNews(limit);

	    return latestNews.stream().map(news -> {
	        NewsResponseDTO dto = new NewsResponseDTO();
	        dto.setsNo(news.getsNo());
	        dto.setNewsTitle(news.getNewsTitle());
	        dto.setNewsDescription(news.getNewsDescription());

	        // ✅ Convert String -> List<String>
	        List<String> imageUrls;
	        if (news.getImageOrVideoUrl() != null && !news.getImageOrVideoUrl().isEmpty()) {
	            // already List<String> irukku
	            imageUrls = news.getImageOrVideoUrl();
	        } else {
	            imageUrls = List.of("/images/default-news.png");
	        }
	        dto.setImageOrVideoUrl(imageUrls);

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

	@Override
	public NewsDataDTO getNewsForEdit(Long sNo) {
	    NewsData news = newsDataRepository.findById(sNo)
	            .orElseThrow(() -> new RuntimeException("News not found with id: " + sNo));

	    // Convert Entity -> DTO
	    NewsDataDTO dto = new NewsDataDTO();
	    dto.setNewsTitle(news.getNewsTitle());
	    dto.setNewsDescription(news.getNewsDescription());
	    dto.setCategory(news.getCategory());
	    dto.setTags(news.getTags());
	    dto.setStatus(news.getStatus());
	    return dto;
	}


}
