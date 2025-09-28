package com.ilayangudi_news_posting.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ilayangudi_news_posting.entity.NewsReports;
import com.ilayangudi_news_posting.enums.ReportStatus;
import com.ilayangudi_news_posting.response_dto.SuperAdminReportsResponse;

@Repository
public interface NewsReportRepository extends JpaRepository<NewsReports, Long> {

	boolean existsByUserEmailAndNews_sNo(String userEmail, Long newsId);

	@Query("""
				SELECT new com.ilayangudi_news_posting.response_dto.SuperAdminReportsResponse(
					n.sNo,
					n.userEmail,
					n.userName,
					n.userMobileNumber,
					n.reportContent,
					n.reason,
					m.sNo,
					m.newsTitle,
					n.reportedAt,
					u.userName,
					u.emailId,
					u.userMobileNumber,
					n.reviewedAt,
					n.deletedAt
				)
				FROM NewsReports n
				LEFT JOIN n.news m
				LEFT JOIN UserRegisterData u ON u.emailId = m.author
			""")
	List<SuperAdminReportsResponse> findReportsDetailForSuperAdmin();

	List<NewsReports> findByReasonAndReviewedAtBefore(ReportStatus status, LocalDateTime cutoff);

}
