package com.ilayangudi_news_posting.servicerepo;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.response_dto.UserDetailsResponseDTO;

public interface UserPageServiceRepository {

	void changeUserProfilePicture(MultipartFile newProfile, Principal principal);
	
	boolean deleteUserProfilePicture(Principal principal);
	
	public List<NewsResponseDTO> getLastOneMonthPublishedNewsData(Principal principal);

	public List<NewsResponseDTO> getLastOneMonthDraftNewsData(Principal principal);

	public List<NewsResponseDTO> getLastOneMonthArchievedNewsData(Principal principal);
	
	public UserDetailsResponseDTO getUserDetails(Principal principal);
	
	public String updateUserDetails(Principal principal, UserDetailsResponseDTO updatedUser);
	
	boolean deleteUserData(Principal principal);
	
	public void confirmEmailChange(String verifiedEmail);

	
}
