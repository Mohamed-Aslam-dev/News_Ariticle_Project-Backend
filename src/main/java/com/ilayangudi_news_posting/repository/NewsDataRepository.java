package com.ilayangudi_news_posting.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ilayangudi_news_posting.entity.NewsData;

@Repository
public interface NewsDataRepository extends JpaRepository<NewsData, Long> {
	
	@Query("SELECT n FROM NewsData n WHERE n.status = 'PUBLISHED' ORDER BY COALESCE(n.updatedAt, n.createdAt) DESC")
	List<NewsData> findTop4LatestNews(Pageable pageable);
	
	@Query("SELECT n FROM NewsData n " +
		       "WHERE n.status = 'PUBLISHED' " +
		       "AND n.createdAt >= :oneMonthAgo " +
		       "ORDER BY COALESCE(n.updatedAt, n.createdAt) DESC")
		List<NewsData> findPublishedNewsLastMonth(@Param("oneMonthAgo") LocalDateTime oneMonthAgo);

}
