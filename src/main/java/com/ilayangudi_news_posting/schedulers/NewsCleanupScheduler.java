package com.ilayangudi_news_posting.schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.entity.NewsReports;
import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.enums.ReportStatus;
import com.ilayangudi_news_posting.enums.UserAccountStatus;
import com.ilayangudi_news_posting.message_services.EmailSenderService;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.NewsReportRepository;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
public class NewsCleanupScheduler {

	@Autowired
	private NewsReportRepository newsReportRepo;

	@Autowired
	private NewsDataRepository newsDataRepo;

	@Autowired
	private UserRegisterDataRepository userRegisterDataRepo;
	
	@Autowired
	private EmailSenderService emailService;

	// Runs every 2 minutes (for testing). Change to "0 0 * * * *" for hourly run.
	@Scheduled(cron = "0 */2 * * * *", zone = "Asia/Kolkata")
	@Scheduled(cron = "0 0 * * * *") // every 1 hour
	@Transactional
	public void deleteReviewedNewsAfter12Hours() {
		log.info("🕒 Scheduler Triggered: Cleaning up reviewed news...");

		LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(5); // test: 5 mins (use .minusHours(12) in prod)
		List<NewsReports> expiredReports = newsReportRepo.findByReasonAndReviewedAtBefore(ReportStatus.REVIEWED,
				cutoffTime);

		if (expiredReports.isEmpty()) {
			log.info("✅ No reviewed news found older than cutoff time ({})", cutoffTime);
			return;
		}

		List<NewsData> expiredNewsList = expiredReports.stream().map(NewsReports::getNews).toList();

		if (expiredNewsList.isEmpty()) {
			log.info("⚠️ No news linked to expired reports. Skipping deletion.");
			return;
		}

		// 🔹 Suspend all distinct authors who posted these news
		expiredNewsList.stream().map(NewsData::getAuthor).distinct().forEach(authorEmail -> {
			Optional<UserRegisterData> optionalUser = userRegisterDataRepo.findByEmailId(authorEmail);

			if (optionalUser.isPresent()) {
				UserRegisterData user = optionalUser.get();
				user.setAccountStatus(UserAccountStatus.SUSPENDED);
				userRegisterDataRepo.save(user);
				user.setSuspendedAt(LocalDateTime.now());
				emailService.sendUserSuspensionMail(user.getEmailId(), user.getUserName());
				log.info("🚫 Suspended user: {}", authorEmail);
			} else {
				log.warn("⚠️ No registered user found for author email: {}", authorEmail);
			}
		});

		// 🔹 Delete the news data
		expiredNewsList.forEach(news -> log.info("🗞 News ID: {} | Title: {} | Author: {}", news.getsNo(),
				news.getNewsTitle(), news.getAuthor()));

		newsDataRepo.deleteAll(expiredNewsList);
		log.info("🗑️ Auto-deleted {} reviewed news older than 12 hours", expiredNewsList.size());
	}

	// 🕒 Runs every 1 hour to reactivate suspended users after 5 days
	@Scheduled(cron = "0 */2 * * * *", zone = "Asia/Kolkata")
	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Kolkata")
	@Transactional
	public void reactivateSuspendedUsersAfter5Days() {
		log.info("🕒 Scheduler Triggered: Checking for users to reactivate...");

		LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(5);;
		List<UserRegisterData> suspendedUsers = userRegisterDataRepo
				.findByAccountStatusAndSuspendedAtBefore(UserAccountStatus.SUSPENDED, cutoffTime);

		if (suspendedUsers.isEmpty()) {
			log.info("✅ No suspended users found older than 5 days.");
			return;
		}

		suspendedUsers.forEach(user -> {
			user.setAccountStatus(UserAccountStatus.ACTIVE);
			user.setSuspendedAt(null);
			userRegisterDataRepo.save(user);
			emailService.sendUserReactivationMail(user.getEmailId(), user.getUserName());
			log.info("✅ Reactivated user: {}", user.getEmailId());
		});

		log.info("🎉 Reactivated {} users who were suspended more than 5 days ago.", suspendedUsers.size());
	}

}
