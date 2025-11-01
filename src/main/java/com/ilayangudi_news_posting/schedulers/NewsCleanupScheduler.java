package com.ilayangudi_news_posting.schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

	// 🕒 Runs every 2 mins for testing; hourly in prod
	@Scheduled(cron = "0 */2 * * * *", zone = "Asia/Kolkata")
	@Transactional
	public synchronized void deleteReviewedNewsAfter12Hours() {
		log.info("🕒 Scheduler Triggered: Cleaning up reviewed news...");

		LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(5); // test mode
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

		// 🔹 Suspend all distinct authors
		expiredNewsList.stream().map(NewsData::getAuthor).distinct().forEach(authorEmail -> {
			Optional<UserRegisterData> optionalUser = userRegisterDataRepo.findByEmailId(authorEmail);

			if (optionalUser.isPresent()) {
				UserRegisterData user = optionalUser.get();

				user.setAccountStatus(UserAccountStatus.SUSPENDED);
				user.setSuspendedAt(LocalDateTime.now());
				userRegisterDataRepo.save(user);

				List<NewsData> authorNews = expiredNewsList.stream()
						.filter(news -> authorEmail.equals(news.getAuthor())).toList();

				// 📨 Send mails safely for each news
				authorNews.forEach(news -> {
					try {
						emailService.sendUserSuspensionMail(user.getEmailId(), news.getNewsTitle(), // Using news title
																									// for readability
								user.getUserName());
					} catch (Exception e) {
						log.error("❌ Failed to send suspension mail for user: {} | News: {}", user.getEmailId(),
								news.getNewsTitle(), e);
					}
				});

				log.info("🚫 Suspended user: {} ({} expired news)", authorEmail, authorNews.size());
			} else {
				log.warn("⚠️ No registered user found for author email: {}", authorEmail);
			}
		});

		// 🔹 Delete expired news safely
		expiredNewsList.forEach(news -> {
			try {
				log.info("🗞 Deleting News ID: {} | Title: {} | Author: {}", news.getsNo(), news.getNewsTitle(),
						news.getAuthor());

				if (newsDataRepo.existsById(news.getsNo())) {
					newsDataRepo.deleteById(news.getsNo());
				} else {
					log.warn("⚠️ News already deleted: {}", news.getsNo());
				}

			} catch (ObjectOptimisticLockingFailureException | EmptyResultDataAccessException e) {
				log.warn("⚠️ Skipping stale/deleted News ID: {}", news.getsNo());
			} catch (Exception e) {
				log.error("❌ Unexpected error deleting News ID: {}", news.getsNo(), e);
			}
		});

		log.info("🗑️ Auto-deleted {} reviewed news older than 12 hours", expiredNewsList.size());
	}

	// 🕒 Runs every 1 hour to reactivate suspended users after 5 days
	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Kolkata")
	@Transactional
	public synchronized void reactivateSuspendedUsersAfter5Days() {
		log.info("🕒 Scheduler Triggered: Checking for users to reactivate...");

		LocalDateTime cutoffTime = LocalDateTime.now().minusDays(5); // prod: 5 days
		List<UserRegisterData> suspendedUsers = userRegisterDataRepo
				.findByAccountStatusAndSuspendedAtBefore(UserAccountStatus.SUSPENDED, cutoffTime);

		if (suspendedUsers.isEmpty()) {
			log.info("✅ No suspended users found older than 5 days.");
			return;
		}

		suspendedUsers.forEach(user -> {
			try {
				user.setAccountStatus(UserAccountStatus.ACTIVE);
				user.setSuspendedAt(null);
				userRegisterDataRepo.save(user);
				emailService.sendUserReactivationMail(user.getEmailId(), user.getUserName());
				log.info("✅ Reactivated user: {}", user.getEmailId());
			} catch (Exception e) {
				log.error("❌ Failed to reactivate user: {}", user.getEmailId(), e);
			}
		});

		log.info("🎉 Reactivated {} users who were suspended more than 5 days ago.", suspendedUsers.size());
	}
}
