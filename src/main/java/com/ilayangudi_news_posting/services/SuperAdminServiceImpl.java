package com.ilayangudi_news_posting.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ilayangudi_news.exceptions.ResourcesNotFoundException;
import com.Ilayangudi_news.exceptions.UnauthorizedAccessException;
import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.entity.NewsReports;
import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.enums.ReportStatus;
import com.ilayangudi_news_posting.message_services.EmailSenderService;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.NewsReportRepository;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.response_dto.SuperAdminAllDataResponse;
import com.ilayangudi_news_posting.response_dto.SuperAdminReportsResponse;
import com.ilayangudi_news_posting.servicerepo.SuperAdminServiceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SuperAdminServiceImpl implements SuperAdminServiceRepository {

	@Autowired
	private NewsDataRepository newsDataRepo; // whole super admin data fetch this repository

	@Autowired
	private UserRegisterDataRepository userRegisterDataRepo;

	@Autowired
	private NewsReportRepository newsReportRepo;

	@Autowired
	private EmailSenderService emailSenderService;

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

	@Override
	public boolean changeReportStatusFromSuperAdmin(Long id, ReportStatus status, String userName) {

		boolean isValid = userRegisterDataRepo.existsByEmailIdAndRole(userName, "SUPER_ADMIN");

		if (!isValid) {
			throw new UnauthorizedAccessException("சூப்பர் அட்மின் தரவை அணுக உங்களுக்கு அனுமதி இல்லை");
		}

		NewsReports report = newsReportRepo.findById(id)
				.orElseThrow(() -> new ResourcesNotFoundException("Report not found with id " + id));

		report.setReason(status);

		sentReminderForReportsStausBased(report);

		newsReportRepo.save(report);

		return true;
	}

	private void sentReminderForReportsStausBased(NewsReports report) {
		NewsData newsData = report.getNews();

		UserRegisterData newsAuthor = userRegisterDataRepo.findByEmailId(newsData.getAuthor())
				.orElseThrow(() -> new RuntimeException("News author not found"));

		switch (report.getReason()) {
		case REVIEWED -> {
			
			if(report == null || newsData == null) {
				throw new RuntimeException("This message has been Already Deleted");
			}
			
			emailSenderService
					.sendEmailPostReportReminderFromReviewedStatus(newsAuthor.getEmailId(), newsAuthor.getUserName(),
							newsData.getsNo(), newsData.getNewsTitle(), report.getsNo(), report.getReportContent())
					.exceptionally(ex -> {
						log.error("Mail sending failed (REVIEWED): {}", ex.getMessage(), ex);
						return false;
					});

			report.setReviewedAt(LocalDateTime.now());
		}

		case REJECTED -> {
			
			if(report == null || newsData == null) {
				throw new RuntimeException("This message has been Already Deleted");
			}
			
			emailSenderService
					.sendEmailPostReportReminderFromRejectedStatus(newsAuthor.getEmailId(), newsAuthor.getUserName(),
							newsData.getsNo(), newsData.getNewsTitle(), report.getsNo(), report.getReportContent())
					.exceptionally(ex -> {
						log.error("Mail sending failed (Author - REJECTED): {}", ex.getMessage(), ex);
						return false;
					});

			emailSenderService
					.sendEmailPostReporterReminderFromRejectedStatus(report.getUserEmail(), report.getUserName(),
							newsData.getsNo(), newsData.getNewsTitle(), report.getsNo(), report.getReportContent())
					.exceptionally(ex -> {
						log.error("Mail sending failed (Reporter - REJECTED): {}", ex.getMessage(), ex);
						return false;
					});
			report.setReviewedAt(null);
		}
		
		case DELETED -> {
		    newsDataRepo.delete(newsData); // cascade deletes related reports
		    log.info("Deleted news {} along with its reports", newsData.getsNo());
		}

        default -> {
            log.info("No action for status: {}", report.getReason());
        }
		}
	}

}
