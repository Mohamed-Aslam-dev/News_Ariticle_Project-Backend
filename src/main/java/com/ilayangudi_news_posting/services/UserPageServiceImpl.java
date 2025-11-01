package com.ilayangudi_news_posting.services;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Ilayangudi_news.exceptions.UserNotFoundException;
import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.response_dto.UserDetailsResponseDTO;
import com.ilayangudi_news_posting.servicerepo.UserPageServiceRepository;

import jakarta.transaction.Transactional;

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
		UserRegisterData user = userRegisterDataRepo.findByEmailId(userEmail)
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
	public UserDetailsResponseDTO getUserDetails(Principal principal) {

		UserDetailsResponseDTO userDetails = userRegisterDataRepo.getUserDetails(principal.getName())
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + principal.getName()));

		// ✅ If profilePicUrl exists, convert it to signed URL
		if (userDetails.getProfilePicUrl() != null && !userDetails.getProfilePicUrl().isEmpty()) {
			String signedUrl = newsFileStore.generateSignedUrl(userDetails.getProfilePicUrl(), 3600); // 1 hour
			userDetails.setProfilePicUrl(signedUrl);
		}

		return userDetails;
	}

	@Override
	public void updateUserDetails(Principal principal, UserDetailsResponseDTO updatedUser) {

		UserRegisterData existing = userRegisterDataRepo.findByEmailId(principal.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		// 🔹 Update username
		if (updatedUser.getUserName() != null && !updatedUser.getUserName().isBlank()) {
			existing.setUserName(updatedUser.getUserName());
		}

		// 🔹 Update email only if changed and not duplicate
		if (updatedUser.getEmailId() != null && !updatedUser.getEmailId().equals(existing.getEmailId())) {
			if (userRegisterDataRepo.existsByEmailId(updatedUser.getEmailId())) {
				throw new RuntimeException("இந்த மின்னஞ்சல் ஏற்கனவே பதிவு செய்யப்பட்டுள்ளது");
			}
			existing.setEmailId(updatedUser.getEmailId());
		}

		// 🔹 Update mobile only if changed and not duplicate
		if (updatedUser.getUserMobileNumber() != null
				&& !updatedUser.getUserMobileNumber().equals(existing.getUserMobileNumber())) {
			if (userRegisterDataRepo.existsByUserMobileNumber(updatedUser.getUserMobileNumber())) {
				throw new RuntimeException("இந்த மொபைல் எண் ஏற்கனவே பதிவு செய்யப்பட்டுள்ளது");
			}
			existing.setUserMobileNumber(updatedUser.getUserMobileNumber());
		}

		userRegisterDataRepo.save(existing);
	}

	@Override
	@Transactional
	public boolean deleteUserData(Principal principal) {
	    String userEmail = principal.getName();
	    UserRegisterData user = userRegisterDataRepo.findByEmailId(userEmail)
	        .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

	    userRegisterDataRepo.deleteById(user.getId());
	    return true;
	}
	
	@Override
	public List<NewsResponseDTO> getLastOneMonthPublishedNewsData(Principal principal) {
		LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
		return newsDataRepository.findUserPublishedNewsLastMonth(oneMonthAgo, principal.getName());
	}

	@Override
	public List<NewsResponseDTO> getLastOneMonthArchievedNewsData(Principal principal) {
		LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
		return newsDataRepository.findUserArchievedNewsLastMonth(oneMonthAgo, principal.getName());
	}

	@Override
	public List<NewsResponseDTO> getLastOneMonthDraftNewsData(Principal principal) {
		LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
		return newsDataRepository.findUserDraftNewsLastMonth(oneMonthAgo, principal.getName());

	}

}
