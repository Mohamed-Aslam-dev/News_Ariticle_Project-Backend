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
				    n.category,
				    CAST(n.tags AS string),
				    CAST(n.status AS string),
				    COALESCE(e.views, 0),
				    COALESCE(e.likes, 0),
				    COALESCE(e.unLikes, 0),
				    false,
				    false,
				    false,
				    n.createdAt,
				    n.updatedAt
					)
					FROM NewsData n
					LEFT JOIN NewsEngagedStatus e ON e.news = n
					LEFT JOIN UserRegisterData u ON u.emailId = n.author
					WHERE n.status = 'PUBLISHED'
					ORDER BY COALESCE(n.updatedAt, n.createdAt) DESC
			""")
	public List<NewsResponseDTO> findTop4LatestNews(Pageable pageable);

	@Query("""
				    SELECT new com.ilayangudi_news_posting.response_dto.NewsResponseDTO(
				    n.sNo,
				    n.newsTitle,
				    n.newsDescription,
				    CAST(n.imageOrVideoUrl AS string),
				    n.author,
				    u.userName,
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

}
