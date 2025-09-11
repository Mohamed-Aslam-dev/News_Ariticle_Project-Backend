package com.ilayangudi_news_posting.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ilayangudi_news_posting.entity.NewsData;

@Repository
public interface NewsDataRepository extends JpaRepository<NewsData, Long> {
	
	@Query("SELECT n FROM NewsData n ORDER BY COALESCE(n.updatedAt, n.createdAt) DESC")
	List<NewsData> findTop3LatestNews(Pageable pageable);

}
