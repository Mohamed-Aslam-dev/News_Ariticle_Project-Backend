package com.ilayangudi_news_posting.servicerepo;

import com.ilayangudi_news_posting.response_dto.SuperAdminAllDataResponse;

public interface SuperAdminServiceRepository {
	
	public SuperAdminAllDataResponse getAllDatasForSuperAdmin(String userName);

}
