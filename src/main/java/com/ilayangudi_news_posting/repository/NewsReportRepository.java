package com.ilayangudi_news_posting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ilayangudi_news_posting.entity.NewsReports;

@Repository
public interface NewsReportRepository extends JpaRepository<NewsReports, Long>{

	boolean existsByUserEmailAndNews_sNo(String userEmail, Long newsId);
	
}
