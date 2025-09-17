package com.ilayangudi_news_posting.services;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.servicerepo.UserPageServiceRepository;

@Service
public class UserPageServiceImpl implements UserPageServiceRepository {

	@Autowired
	private UserRegisterDataRepository userRegisterDataRepo;

	@Autowired
	private NewsImageAndVideoFile newsFileStore;
	
	@Autowired
	private NewsDataRepository newsDataRepository;

	@Override
	public void changeUserProfilePicture(MultipartFile newProfile, Principal principal) {

		try {
			String userEmail = principal.getName();

			UserRegisterData userDatas = userRegisterDataRepo.findByEmailId(userEmail)
					.orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

			if (newProfile != null && !newProfile.isEmpty()) {
				String uploadDir = "userProfilePics";
				String imagePath = newsFileStore.getNewsImageAndVideoFilepath(newProfile, uploadDir);
				userDatas.setProfilePicUrl(imagePath);
			}

			userRegisterDataRepo.save(userDatas); // ✅ update existing user
		} catch (IOException e) {
			// log & rethrow as runtime so global handler can catch
			throw new RuntimeException("Error while saving profile pic", e);
		}

	}
	
	@Override
	public boolean deleteUserProfilePicture(Principal principal) {
		
		String userEmail = principal.getName();
	    UserRegisterData user = userRegisterDataRepo
	        .findByEmailId(userEmail)
	        .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

	    if (user.getProfilePicUrl() != null) {
	    	
	    	// ✅ Delete from Supabase bucket
	        newsFileStore.deleteFileFromSupabase(user.getProfilePicUrl());
	    	
	        user.setProfilePicUrl(null);
	        userRegisterDataRepo.save(user);
	        return true; // profile deleted
	    }
	    return false; // already no profile pic

	}
	
	@Override
	public List<NewsResponseDTO> getLastOneMonthPublishedNewsData(Principal principal) {
		LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
		List<NewsData> lastOneMonthPublishedNews = newsDataRepository.findUserPublishedNewsLastMonth(oneMonthAgo,
				principal.getName());

		return getNewsDatas(lastOneMonthPublishedNews);
	}

	@Override
	public List<NewsResponseDTO> getLastOneMonthArchievedNewsData(Principal principal) {
		LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
		List<NewsData> lastOneMonthArchivedNews = newsDataRepository.findUserArchievedNewsLastMonth(oneMonthAgo,
				principal.getName());

		return getNewsDatas(lastOneMonthArchivedNews);
	}

	@Override
	public List<NewsResponseDTO> getLastOneMonthDraftNewsData(Principal principal) {
		LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
		List<NewsData> lastOneMonthDraftNews = newsDataRepository.findUserDraftNewsLastMonth(oneMonthAgo,
				principal.getName());

		return getNewsDatas(lastOneMonthDraftNews);
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
				// already List<String> irukku
				imageUrls = news.getImageOrVideoUrl();
			} else {
				imageUrls = null;
//	            throw new RuntimeException("Image or Video not found for this news");
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
