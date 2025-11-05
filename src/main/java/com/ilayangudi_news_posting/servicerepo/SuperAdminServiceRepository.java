package com.ilayangudi_news_posting.servicerepo;

import java.security.Principal;
import java.util.List;

import com.ilayangudi_news_posting.enums.ReportStatus;
import com.ilayangudi_news_posting.enums.UserAccountStatus;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.response_dto.SuperAdminAllDataResponse;
import com.ilayangudi_news_posting.response_dto.SuperAdminReportsResponse;
import com.ilayangudi_news_posting.response_dto.UserDetailsResponseDTO;

public interface SuperAdminServiceRepository {

	public SuperAdminAllDataResponse getAllDatasForSuperAdmin(String userName);

	public List<SuperAdminReportsResponse> getAllReportsDataForSuperAdmin(String userName);

	public boolean changeReportStatusFromSuperAdmin(Long id, ReportStatus status, String userName);

	public List<NewsResponseDTO> searchNews(String keyword);

	public boolean deleteNewsData(Long id, Principal principal, String reason);

	public List<UserDetailsResponseDTO> searchUser(String keyword, Principal principal);
	
	public String changeAccountStatusFromSuperAdmin(Long id, UserAccountStatus status, String userName, String reason);

}
