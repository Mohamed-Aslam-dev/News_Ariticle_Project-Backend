package com.ilayangudi_news_posting.services;

import java.io.IOException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.servicerepo.UserPageServiceRepository;

@Service
public class UserPageServiceImpl implements UserPageServiceRepository {

	@Autowired
	private UserRegisterDataRepository userRegisterDataRepo;

	@Autowired
	private NewsImageAndVideoFile newsFileStore;

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

}
