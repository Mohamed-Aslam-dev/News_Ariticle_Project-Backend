package com.ilayangudi_news_posting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ilayangudi_news_posting.message_services.OtpData;

public interface OtpRepository extends JpaRepository<OtpData, Long> {

	public Optional<OtpData> findByEmail(String email);
	
	void deleteByEmail(String emailId);
	
}
