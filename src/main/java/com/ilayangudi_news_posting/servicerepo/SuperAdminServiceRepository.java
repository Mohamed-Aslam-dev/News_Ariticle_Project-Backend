package com.ilayangudi_news_posting.servicerepo;

import java.util.List;

import com.ilayangudi_news_posting.response_dto.SuperAdminAllDataResponse;
import com.ilayangudi_news_posting.response_dto.SuperAdminReportsResponse;

public interface SuperAdminServiceRepository {
	
	public SuperAdminAllDataResponse getAllDatasForSuperAdmin(String userName);
	
	public List<SuperAdminReportsResponse> getAllReportsDataForSuperAdmin(String userName);

}
