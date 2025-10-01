package com.ilayangudi_news_posting.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.response_dto.SuperAdminAllDataResponse;

@Repository
public interface NewsDataRepository extends JpaRepository<NewsData, Long> {

	@Query("""
				    SELECT new com.ilayangudi_news_posting.response_dto.NewsResponseDTO(
				    n.sNo,
				    n.newsTitle,
				    n.newsDescription,
				    CAST(n.imageOrVideoUrl AS string),
				    n.author,
				    u.userName,
				    u.profilePicUrl,
				    n.category,
				    CAST(n.tags AS string),
				    CAST(n.status AS string),
				    COALESCE(e.views, 0),
				    COALESCE(e.likes, 0),
				    COALESCE(e.unLikes, 0),
				    COALESCE(m.liked, false),
				    COALESCE(m.disliked, false),
				    COALESCE(m.viewed, false),
				    n.createdAt,
				    n.updatedAt
					)
					FROM NewsData n
					LEFT JOIN NewsEngagedStatus e ON e.news = n
					LEFT JOIN UserRegisterData u ON u.emailId = n.author
					LEFT JOIN NewsUserEngagement m
					ON m.news = n
					AND m.username = :currentUsername
					WHERE n.status = 'PUBLISHED'
					ORDER BY COALESCE(n.updatedAt, n.createdAt) DESC
			""")
	public List<NewsResponseDTO> findTop4LatestNews(Pageable pageable,
			@Param("currentUsername") String currentUsername);

	@Query("""
				    SELECT new com.ilayangudi_news_posting.response_dto.NewsResponseDTO(
				    n.sNo,
				    n.newsTitle,
				    n.newsDescription,
				    CAST(n.imageOrVideoUrl AS string),
				    n.author,
				    u.userName,
				    u.profilePicUrl,
				    n.category,
				    CAST(n.tags AS string),
				    CAST(n.status AS string),
				    COALESCE(e.views, 0),
				    COALESCE(e.likes, 0),
				    COALESCE(e.unLikes, 0),
				    COALESCE(m.liked, false),
				    COALESCE(m.disliked, false),
				    COALESCE(m.viewed, false),
				    n.createdAt,
				    n.updatedAt
					)
					FROM NewsData n
					LEFT JOIN NewsEngagedStatus e ON e.news = n
					LEFT JOIN UserRegisterData u ON u.emailId = n.author
					LEFT JOIN NewsUserEngagement m
					ON m.news = n
					AND m.username = :currentUsername
					WHERE n.status = 'PUBLISHED'
					AND n.createdAt >= :oneMonthAgo
					ORDER BY COALESCE(n.updatedAt, n.createdAt) DESC
			""")
	List<NewsResponseDTO> findPublishedNewsLastMonth(@Param("oneMonthAgo") LocalDateTime oneMonthAgo,
			@Param("currentUsername") String currentUsername);

	@Query("""
				    SELECT new com.ilayangudi_news_posting.response_dto.NewsResponseDTO(
				    n.sNo,
				    n.newsTitle,
				    n.newsDescription,
				    CAST(n.imageOrVideoUrl AS string),
				    n.author,
				    u.userName,
				    u.profilePicUrl,
				    n.category,
				    CAST(n.tags AS string),
				    CAST(n.status AS string),
				    COALESCE(e.views, 0),
				    COALESCE(e.likes, 0),
				    COALESCE(e.unLikes, 0),
				    COALESCE(m.liked, false),
				    COALESCE(m.disliked, false),
				    COALESCE(m.viewed, false),
				    n.createdAt,
				    n.updatedAt
					)
					FROM NewsData n
					LEFT JOIN NewsEngagedStatus e ON e.news = n
					LEFT JOIN UserRegisterData u ON u.emailId = n.author
					LEFT JOIN NewsUserEngagement m
					ON m.news = n
					AND m.username = :currentUsername
					WHERE n.status = 'PUBLISHED'
					AND n.createdAt >= :oneMonthAgo
					AND LOWER(n.author) = LOWER(:currentUsername)
					ORDER BY COALESCE(n.updatedAt, n.createdAt) DESC
			""")
	List<NewsResponseDTO> findUserPublishedNewsLastMonth(@Param("oneMonthAgo") LocalDateTime oneMonthAgo,
			@Param("currentUsername") String currentUsername);

	@Query("""
				    SELECT new com.ilayangudi_news_posting.response_dto.NewsResponseDTO(
				    n.sNo,
				    n.newsTitle,
				    n.newsDescription,
				    CAST(n.imageOrVideoUrl AS string),
				    n.author,
				    u.userName,
				    u.profilePicUrl,
				    n.category,
				    CAST(n.tags AS string),
				    CAST(n.status AS string),
				    COALESCE(e.views, 0),
				    COALESCE(e.likes, 0),
				    COALESCE(e.unLikes, 0),
				    COALESCE(m.liked, false),
				    COALESCE(m.disliked, false),
				    COALESCE(m.viewed, false),
				    n.createdAt,
				    n.updatedAt
					)
					FROM NewsData n
					LEFT JOIN NewsEngagedStatus e ON e.news = n
					LEFT JOIN UserRegisterData u ON u.emailId = n.author
					LEFT JOIN NewsUserEngagement m
					ON m.news = n
					AND m.username = :currentUsername
					WHERE n.status = 'DRAFT'
					AND n.createdAt >= :oneMonthAgo
					AND LOWER(n.author) = LOWER(:currentUsername)
					ORDER BY COALESCE(n.updatedAt, n.createdAt) DESC
			""")
	List<NewsResponseDTO> findUserDraftNewsLastMonth(@Param("oneMonthAgo") LocalDateTime oneMonthAgo,
			@Param("currentUsername") String currentUsername);

	@Query("""
				    SELECT new com.ilayangudi_news_posting.response_dto.NewsResponseDTO(
				    n.sNo,
				    n.newsTitle,
				    n.newsDescription,
				    CAST(n.imageOrVideoUrl AS string),
				    n.author,
				    u.userName,
				    u.profilePicUrl,
				    n.category,
				    CAST(n.tags AS string),
				    CAST(n.status AS string),
				    COALESCE(e.views, 0),
				    COALESCE(e.likes, 0),
				    COALESCE(e.unLikes, 0),
				    COALESCE(m.liked, false),
				    COALESCE(m.disliked, false),
				    COALESCE(m.viewed, false),
				    n.createdAt,
				    n.updatedAt
					)
					FROM NewsData n
					LEFT JOIN NewsEngagedStatus e ON e.news = n
					LEFT JOIN UserRegisterData u ON u.emailId = n.author
					LEFT JOIN NewsUserEngagement m
					ON m.news = n
					AND m.username = :currentUsername
					WHERE n.status = 'ARCHIEVED'
					AND n.createdAt >= :oneMonthAgo
					AND LOWER(n.author) = LOWER(:currentUsername)
					ORDER BY COALESCE(n.updatedAt, n.createdAt) DESC
			""")
	List<NewsResponseDTO> findUserArchievedNewsLastMonth(@Param("oneMonthAgo") LocalDateTime oneMonthAgo,
			@Param("currentUsername") String currentUsername);

	@Query("""
			SELECT new com.ilayangudi_news_posting.response_dto.SuperAdminAllDataResponse(
				 (SELECT COUNT(u) FROM UserRegisterData u),
			     (SELECT COUNT(n) FROM NewsData n),
			     (SELECT COUNT(n) FROM NewsData n WHERE n.status = 'ARCHIEVED'),
			     (SELECT COUNT(n) FROM NewsData n WHERE n.status = 'PUBLISHED'),
			     (SELECT COUNT(n) FROM NewsData n WHERE n.status = 'DRAFT'),
			     (SELECT COALESCE(SUM(e.likes),0) FROM NewsEngagedStatus e),
			     (SELECT COALESCE(SUM(e.unLikes),0) FROM NewsEngagedStatus e),
			     (SELECT COALESCE(SUM(e.views),0) FROM NewsEngagedStatus e),
			     (SELECT COUNT(r) FROM NewsReports r)
			)
			""")
	SuperAdminAllDataResponse findAllDatasResponseForSuperAdmin();

}
