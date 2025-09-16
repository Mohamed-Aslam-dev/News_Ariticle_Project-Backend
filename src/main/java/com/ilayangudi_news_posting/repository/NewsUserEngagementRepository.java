package com.ilayangudi_news_posting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.entity.NewsUserEngagement;

public interface NewsUserEngagementRepository extends JpaRepository<NewsUserEngagement, Long>{

	Optional<NewsUserEngagement> findByNewsAndUsername(NewsData news, String username);

	
}
