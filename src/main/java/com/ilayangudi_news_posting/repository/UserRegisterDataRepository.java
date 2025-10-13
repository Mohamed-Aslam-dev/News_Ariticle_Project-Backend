package com.ilayangudi_news_posting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ilayangudi_news_posting.entity.UserRegisterData;

@Repository
public interface UserRegisterDataRepository extends JpaRepository<UserRegisterData, Long> {

	@Query("SELECT u FROM UserRegisterData u WHERE u.emailId = :login OR u.userMobileNumber = :login")
	Optional<UserRegisterData> findByEmailIdOrUserMobileNumber(@Param("login") String login);

	boolean existsByEmailId(String emailId);

	boolean existsByUserMobileNumber(String userMobileNumber);
	
	Optional<UserRegisterData> findByResetToken(String resetToken);

	Optional<UserRegisterData> findByEmailId(String email);
	
	boolean existsByEmailIdAndRole(String emailId, String role);

	Optional<UserRegisterData> findByEmailIdOrUserMobileNumber(String emailId, String mobileNumber);

	
}
