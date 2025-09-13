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
import com.ilayangudi_news_posting.entity.NewsEngagedStatus;
import com.ilayangudi_news_posting.enums.NewsStatus;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.NewsStatusRepository;
import com.ilayangudi_news_posting.request_dto.NewsDataDTO;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.servicerepo.NewsDataServiceRepository;

@Service
public class NewsDataServiceImpl implements NewsDataServiceRepository{

	@Autowired
	private NewsDataRepository newsDataRepository;
	
	@Autowired
	private NewsStatusRepository newsStatusRepository;
	
	@Autowired
	private NewsImageAndVideoFile newsFileStore;
	
	public void addANewsData(NewsDataDTO newsDataDto, MultipartFile[] files, Principal principal) {
	    try {
	        String uploadFolder = "newsUploads";
	        List<String> imagePaths = newsFileStore.getNewsImageAndVideoFilepaths(files, uploadFolder);

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
	     // ✅ Engagement (from NewsEngagedStatus)
	        if (news.getNewsEngagedStatus() != null) {
	            dto.setViews(news.getNewsEngagedStatus().getViews());
	            dto.setLikes(news.getNewsEngagedStatus().getLikes());
	            dto.setUnLikes(news.getNewsEngagedStatus().getUnLikes());
	        } else {
	            dto.setViews(0L);
	            dto.setLikes(0L);
	            dto.setUnLikes(0L);
	        }
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

	public NewsEngagedStatus addNewsLike(Long id) {
		NewsData news = newsDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        NewsEngagedStatus status = newsStatusRepository.findByNews(news)
                .orElse(new NewsEngagedStatus());

        status.setNews(news);
        status.setLikes(status.getLikes() + 1);
        return newsStatusRepository.save(status);
    }

    public NewsEngagedStatus addNewsUnLike(Long id) {
    	NewsData news = newsDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        NewsEngagedStatus status = newsStatusRepository.findByNews(news)
                .orElse(new NewsEngagedStatus());
        
        status.setNews(news);
        status.setUnLikes(status.getUnLikes() + 1);
        return newsStatusRepository.save(status);
    }

    public NewsEngagedStatus addNewsViews(Long id) {
    	NewsData news = newsDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        NewsEngagedStatus status = newsStatusRepository.findByNews(news)
                .orElse(new NewsEngagedStatus());
        
        status.setNews(news);
        status.setViews(status.getViews() + 1);
        return newsStatusRepository.save(status);
    }
	

}
