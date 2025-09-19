package com.ilayangudi_news_posting.services;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.entity.NewsEngagedStatus;
import com.ilayangudi_news_posting.entity.NewsUserEngagement;
import com.ilayangudi_news_posting.enums.NewsStatus;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.NewsStatusRepository;
import com.ilayangudi_news_posting.repository.NewsUserEngagementRepository;
import com.ilayangudi_news_posting.request_dto.NewsDataDTO;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.servicerepo.NewsDataServiceRepository;

import jakarta.transaction.Transactional;

@Service
public class NewsDataServiceImpl implements NewsDataServiceRepository {

	@Autowired
	private NewsDataRepository newsDataRepository;

	@Autowired
	private NewsStatusRepository newsStatusRepository;

	@Autowired
	private NewsUserEngagementRepository newsUserEngagementRepo;

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

		return getNewsDatas(latestNews);
	}

	@Override
	public List<NewsResponseDTO> getLastOneMonthNewsData() {
		LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
		List<NewsData> lastOneMonthNews = newsDataRepository.findPublishedNewsLastMonth(oneMonthAgo);

		return getNewsDatas(lastOneMonthNews);
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

	@Transactional
	public NewsEngagedStatus toggleLike(Long newsId, String username) {
	    NewsData news = newsDataRepository.findById(newsId)
	            .orElseThrow(() -> new RuntimeException("News not found"));

	    NewsUserEngagement engagement = newsUserEngagementRepo.findByNewsAndUsername(news, username)
	            .orElseGet(() -> new NewsUserEngagement(null, username, false, false, false, news));

	    NewsEngagedStatus status = newsStatusRepository.findByNews(news)
	            .orElseGet(() -> {
	                NewsEngagedStatus s = new NewsEngagedStatus();
	                s.setNews(news);
	                s.setLikes(0L);
	                s.setUnLikes(0L);
	                s.setReports(0L);
	                s.setViews(0L);
	                return s;
	            });

	    // Toggle like
	    if (engagement.isLiked()) {
	        engagement.setLiked(false);
	        status.setLikes(Math.max(status.getLikes() - 1, 0));
	    } else {
	        engagement.setLiked(true);
	        engagement.setDisliked(false); // cannot dislike if liked
	        status.setLikes(status.getLikes() + 1);
	    }

	    newsUserEngagementRepo.save(engagement);
	    newsStatusRepository.save(status);
	    return status;
	}

	@Transactional
	public NewsEngagedStatus toggleUnLike(Long newsId, String username) {
	    NewsData news = newsDataRepository.findById(newsId)
	            .orElseThrow(() -> new RuntimeException("News not found"));

	    NewsUserEngagement engagement = newsUserEngagementRepo.findByNewsAndUsername(news, username)
	            .orElseGet(() -> new NewsUserEngagement(null, username, false, false, false, news));

	    NewsEngagedStatus status = newsStatusRepository.findByNews(news)
	            .orElseGet(() -> {
	                NewsEngagedStatus s = new NewsEngagedStatus();
	                s.setNews(news);
	                s.setLikes(0L);
	                s.setUnLikes(0L);
	                s.setReports(0L);
	                s.setViews(0L);
	                return s;
	            });

	    // Toggle unlike
	    if (engagement.isDisliked()) {
	        engagement.setDisliked(false);
	        status.setUnLikes(Math.max(status.getUnLikes() - 1, 0));
	    } else {
	        engagement.setDisliked(true);
	        engagement.setLiked(false); // cannot like if disliked
	        status.setUnLikes(status.getUnLikes() + 1);
	    }

	    newsUserEngagementRepo.save(engagement);
	    newsStatusRepository.save(status);
	    return status;
	}

	@Transactional
	public NewsEngagedStatus addView(Long newsId, String username) {
	    NewsData news = newsDataRepository.findById(newsId)
	            .orElseThrow(() -> new RuntimeException("News not found"));

	    NewsUserEngagement engagement = newsUserEngagementRepo.findByNewsAndUsername(news, username)
	            .orElseGet(() -> new NewsUserEngagement(null, username, false, false, false, news));

	    NewsEngagedStatus status = newsStatusRepository.findByNews(news)
	            .orElseGet(() -> {
	                NewsEngagedStatus s = new NewsEngagedStatus();
	                s.setNews(news);
	                s.setLikes(0L);
	                s.setUnLikes(0L);
	                s.setReports(0L);
	                s.setViews(0L);
	                return s;
	            });

	    // Add view only once per user
	    if (!engagement.isViewed()) {
	        engagement.setViewed(true);
	        status.setViews(status.getViews() + 1);
	    }

	    newsUserEngagementRepo.save(engagement);
	    newsStatusRepository.save(status);
	    return status;
	}


	public boolean newsPostMoveToArchive(Long id, Principal principal) {
		Optional<NewsData> optionalNews = newsDataRepository.findById(id);

		if (optionalNews.isEmpty()) {
			return false; // News not found
		}

		NewsData news = optionalNews.get();
		boolean isValidPostForLoginUser = news.getAuthor().equals(principal.getName());

		if (!isValidPostForLoginUser) {
			return false; // Not the logged-in user's post
		}

		news.setStatus(NewsStatus.ARCHIEVED); // spelling ARCHIVED
		newsDataRepository.save(news);

		return true;
	}

	public boolean newsPostMoveToDraft(Long id, Principal principal) {
		Optional<NewsData> optionalNews = newsDataRepository.findById(id);

		if (optionalNews.isEmpty()) {
			return false; // not found
		}

		NewsData news = optionalNews.get();

		boolean isValidPostForLoginUser = news.getAuthor().equals(principal.getName());

		if (!isValidPostForLoginUser) {
			return false; // Not the logged-in user's post
		}

		news.setStatus(NewsStatus.DRAFT);
		newsDataRepository.save(news);

		return true;
	}

	public boolean newsPostMoveToPublished(Long id, Principal principal) {
		Optional<NewsData> optionalNews = newsDataRepository.findById(id);

		if (optionalNews.isEmpty()) {
			return false; // not found
		}

		NewsData news = optionalNews.get();

		boolean isValidPostForLoginUser = news.getAuthor().equals(principal.getName());

		if (!isValidPostForLoginUser) {
			return false; // Not the logged-in user's post
		}

		news.setStatus(NewsStatus.PUBLISHED);
		newsDataRepository.save(news);

		return true;
	}

	private List<NewsResponseDTO> getNewsDatas(List<NewsData> newsList) {

		return newsList.stream().map(news -> {
			NewsResponseDTO dto = new NewsResponseDTO();
			dto.setsNo(news.getsNo());
			dto.setNewsTitle(news.getNewsTitle());
			dto.setNewsDescription(news.getNewsDescription());

			// ✅ Convert String -> List<String>
			List<String> imageUrls;
			if (news.getImageOrVideoUrl() != null && !news.getImageOrVideoUrl().isEmpty()) {
			    // original relative paths
			    imageUrls = newsFileStore.generateSignedUrls(news.getImageOrVideoUrl(), 60);
			} else {
			    imageUrls = null;
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

}
