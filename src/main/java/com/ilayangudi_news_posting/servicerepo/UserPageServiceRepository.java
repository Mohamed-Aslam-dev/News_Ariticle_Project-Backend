package com.ilayangudi_news_posting.servicerepo;

import java.security.Principal;
import org.springframework.web.multipart.MultipartFile;

public interface UserPageServiceRepository {

	void changeUserProfilePicture(MultipartFile newProfile, Principal principal);
	
	boolean deleteUserProfilePicture(Principal principal);
	
}
