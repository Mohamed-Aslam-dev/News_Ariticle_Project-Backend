package com.ilayangudi_news_posting.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ilayangudi_news.exceptions.UnauthorizedAccessException;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.NewsReportRepository;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.response_dto.SuperAdminAllDataResponse;
import com.ilayangudi_news_posting.response_dto.SuperAdminReportsResponse;
import com.ilayangudi_news_posting.servicerepo.SuperAdminServiceRepository;

@Service
public class SuperAdminServiceImpl implements SuperAdminServiceRepository {

	@Autowired
	private NewsDataRepository newsDataRepo; // whole super admin data fetch this repository

	@Autowired
	private UserRegisterDataRepository userRegisterDataRepo;

	@Autowired
	private NewsReportRepository newsReportRepo;

	@Override
	public SuperAdminAllDataResponse getAllDatasForSuperAdmin(String userName) {

		boolean isValid = userRegisterDataRepo.existsByEmailIdAndRole(userName, "SUPER_ADMIN");

		if (!isValid) {
			throw new UnauthorizedAccessException("சூப்பர் அட்மின் தரவை அணுக உங்களுக்கு அனுமதி இல்லை");
		}

		return newsDataRepo.findAllDatasResponseForSuperAdmin();
	}

	@Override
	public List<SuperAdminReportsResponse> getAllReportsDataForSuperAdmin(String userName) {
		boolean isValid = userRegisterDataRepo.existsByEmailIdAndRole(userName, "SUPER_ADMIN");

		if (!isValid) {
			throw new UnauthorizedAccessException("சூப்பர் அட்மின் தரவை அணுக உங்களுக்கு அனுமதி இல்லை");
		}

		return newsReportRepo.findReportsDetailForSuperAdmin();
	}

}
