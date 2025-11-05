package com.ilayangudi_news_posting.services;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Ilayangudi_news.exceptions.ResourcesNotFoundException;
import com.Ilayangudi_news.exceptions.UnauthorizedAccessException;
import com.Ilayangudi_news.exceptions.UserNotFoundException;
import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.entity.NewsReports;
import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.enums.ReportStatus;
import com.ilayangudi_news_posting.enums.UserAccountStatus;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.message_services.EmailSenderService;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.NewsReportRepository;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.response_dto.SuperAdminAllDataResponse;
import com.ilayangudi_news_posting.response_dto.SuperAdminReportsResponse;
import com.ilayangudi_news_posting.response_dto.UserDetailsResponseDTO;
import com.ilayangudi_news_posting.servicerepo.SuperAdminServiceRepository;

import jakarta.transaction.Transactional;
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

	@Autowired
	private NewsDataRepository newsDataRepository;

	@Autowired
	private NewsImageAndVideoFile newsFileStore;

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

			if (report == null || newsData == null) {
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

			if (report == null || newsData == null) {
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

		default -> {
			log.info("No action for status: {}", report.getReason());
		}
		}
	}

	public List<NewsResponseDTO> searchNews(String keyword) {
		List<NewsResponseDTO> result;

		// 🔹 If range given like "21-25"
		if (keyword.matches("\\d+-\\d+")) {
			String[] range = keyword.split("-");
			Long start = Long.parseLong(range[0]);
			Long end = Long.parseLong(range[1]);
			result = newsDataRepo.searchByIdRange(start, end);
		}
		// 🔹 If only one ID like "21"
		else if (keyword.matches("\\d+")) {
			Long id = Long.parseLong(keyword);
			result = newsDataRepo.searchById(id);
		}
		// 🔹 Else title search
		else {
			result = newsDataRepo.searchByTitle(keyword);
		}

		return result;
	}

	@Override
	@Transactional
	public boolean deleteNewsData(Long id, Principal principal, String reason) {
		String loggedInUser = principal.getName();

		boolean isSuperAdmin = userRegisterDataRepo.existsByEmailIdAndRole(loggedInUser, "SUPER_ADMIN");

		if (!isSuperAdmin) {
			throw new UnauthorizedAccessException("சூப்பர் அட்மின் தரவை அணுக உங்களுக்கு அனுமதி இல்லை");
		}

		// Step 1: Find the news by ID
		NewsData news = newsDataRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

		UserRegisterData newsAuthor = userRegisterDataRepo.findByEmailId(news.getAuthor())
				.orElseThrow(() -> new RuntimeException("News author not found"));

		// ✅ Step 3: Delete all files (image/video URLs)
		List<String> urls = news.getImageOrVideoUrl(); // 👈 Already a List<String>
		if (urls != null && !urls.isEmpty()) {
			for (String fileUrl : urls) {
				if (fileUrl != null && !fileUrl.isBlank()) {
					newsFileStore.deleteFileFromSupabase(fileUrl.trim());
				}
			}
		}

		// Step 3: Delete the news (cascades will handle related data)
		newsDataRepository.delete(news);

		emailSenderService.deleteNewsFromSuperAdmin(newsAuthor.getEmailId(), newsAuthor.getUserName(), news.getsNo(),
				news.getNewsTitle(), reason);

		return true;
	}

	public List<UserDetailsResponseDTO> searchUser(String keyword, Principal principal) {

		String loggedInUser = principal.getName();

		boolean isSuperAdmin = userRegisterDataRepo.existsByEmailIdAndRole(loggedInUser, "SUPER_ADMIN");
		if (!isSuperAdmin) {
			throw new UnauthorizedAccessException("சூப்பர் அட்மின் தரவை அணுக உங்களுக்கு அனுமதி இல்லை");
		}

		List<UserDetailsResponseDTO> users = userRegisterDataRepo.getUserDetailsBySuperAdmin(keyword);
		if (users.isEmpty()) {
			throw new UserNotFoundException("No users found for keyword: " + keyword);
		}

		// ✅ Add signed URL for profile pictures
		users.forEach(user -> {
			if (user.getProfilePicUrl() != null && !user.getProfilePicUrl().isEmpty()) {
				String signedUrl = newsFileStore.generateSignedUrl(user.getProfilePicUrl(), 3600);
				user.setProfilePicUrl(signedUrl);
			}
		});

		return users;
	}

	@Override
	public String changeAccountStatusFromSuperAdmin(Long id, UserAccountStatus status, String userName, String reason) {

		boolean isValid = userRegisterDataRepo.existsByEmailIdAndRole(userName, "SUPER_ADMIN");

		if (!isValid) {
			throw new UnauthorizedAccessException("சூப்பர் அட்மின் தரவை அணுக உங்களுக்கு அனுமதி இல்லை");
		}

		UserRegisterData user = userRegisterDataRepo.findById(id)
				.orElseThrow(() -> new ResourcesNotFoundException("User not found with id " + id));

		// ✅ Switch based on the new status (target)
		switch (status) {

		case ACTIVE -> {
			emailSenderService.sendUserReactivationMailFromSuperadmin(user.getEmailId(), user.getUserName(), reason);
			user.setAccountStatus(UserAccountStatus.ACTIVE);
			userRegisterDataRepo.save(user);
			return user.getUserName() + " என்பவருடைய கணக்கு வெற்றிகரமாக மீண்டும் செயல்படுத்தப்பட்டுள்ளது.";
		}

		case SUSPENDED -> {
			emailSenderService.sendUserSuspensionMailFromSuperadmin(user.getEmailId(), reason, user.getUserName()); 
			user.setAccountStatus(UserAccountStatus.SUSPENDED);
			userRegisterDataRepo.save(user);
			return user.getUserName() + " என்பவருடைய கணக்கு வெற்றிகரமாக தற்காலிகமாக முடக்கப்பட்டது.";
		}

		case BANNED -> {
			emailSenderService.sendUserBannedMailFromSuperadmin(user.getEmailId(), reason, user.getUserName());
			user.setAccountStatus(UserAccountStatus.BANNED);
			userRegisterDataRepo.save(user);
			return user.getUserName() + " என்பவருடைய கணக்கு வெற்றிகரமாக நிரந்தரமாக முடக்கப்பட்டது.";
		}

		default -> throw new IllegalArgumentException("தவறான கணக்கு நிலை (Invalid account status)");
		}
	}

}
