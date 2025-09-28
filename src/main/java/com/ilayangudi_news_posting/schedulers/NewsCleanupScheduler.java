package com.ilayangudi_news_posting.schedulers;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.entity.NewsReports;
import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.enums.ReportStatus;
import com.ilayangudi_news_posting.enums.UserAccountStatus;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.NewsReportRepository;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import jakarta.transaction.Transactional;
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

	// run every 1 hour
	@Scheduled(cron = "0 */2 * * * *", zone = "Asia/Kolkata") // @Scheduled(cron = "0 0 * * * *") // every 1 hour
	@Transactional
	public void deleteReviewedNewsAfter12Hours() {

		System.out.println("Executed for ever 2 min");

		LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(5);

		// 🔑 Multiple reports fetch pannunga
		List<NewsReports> expiredReports = newsReportRepo.findByReasonAndReviewedAtBefore(ReportStatus.REVIEWED,
				cutoffTime);

		if (expiredReports.isEmpty()) {
			log.info("No reviewed news found older than 12 hours");
			return;
		}

		// 🔑 Collect related news
		List<NewsData> expiredNewsList = expiredReports.stream().map(NewsReports::getNews).toList();
		
		String author = expiredNewsList.stream().map(NewsData :: getAuthor).toString();
		
		UserRegisterData userData = userRegisterDataRepo.findByEmailId(author).get();

		if (!expiredNewsList.isEmpty()) {
			// Step 2: Delete NewsData (children Engaged + UserEngagement auto delete aagum)
			newsDataRepo.deleteAll(expiredNewsList);
			userData.setAccountStatus(UserAccountStatus.SUSPENDED);
			log.info("Auto-deleted {} reviewed news older than 12 hours", expiredNewsList.size());
		} else {
			
			log.info("No news to delete, but updated {} report(s) status to ALREADY_DELETED",
					expiredReports.size());
		}
	}

}
