package com.ilayangudi_news_posting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.entity.NewsEngagedStatus;


public interface NewsStatusRepository extends JpaRepository<NewsEngagedStatus, Long> {

	Optional<NewsEngagedStatus> findByNews(NewsData news);

	
}
