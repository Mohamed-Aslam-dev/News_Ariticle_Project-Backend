package com.ilayangudi_news_posting.services;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.Ilayangudi_news.exceptions.ResourcesNotFoundException;
import com.ilayangudi_news_posting.entity.NewsData;
import com.ilayangudi_news_posting.entity.NewsEngagedStatus;
import com.ilayangudi_news_posting.entity.NewsReports;
import com.ilayangudi_news_posting.entity.NewsUserEngagement;
import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.enums.NewsStatus;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.message_services.EmailSenderService;
import com.ilayangudi_news_posting.message_services.NotificationService;
import com.ilayangudi_news_posting.repository.NewsDataRepository;
import com.ilayangudi_news_posting.repository.NewsReportRepository;
import com.ilayangudi_news_posting.repository.NewsStatusRepository;
import com.ilayangudi_news_posting.repository.NewsUserEngagementRepository;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.request_dto.NewsDataDTO;
import com.ilayangudi_news_posting.request_dto.NewsReportDTO;
import com.ilayangudi_news_posting.response_dto.LikeResponseDTO;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.response_dto.UnlikeResponseDTO;
import com.ilayangudi_news_posting.response_dto.ViewedResponseDTO;
import com.ilayangudi_news_posting.servicerepo.NewsDataServiceRepository;

import jakarta.transaction.Transactional;

@Service
public class NewsDataServiceImpl implements NewsDataServiceRepository {

	private final EmailSenderService emailSenderService;

	@Autowired
	private NewsDataRepository newsDataRepository;

	@Autowired
	private NewsStatusRepository newsStatusRepository;

	@Autowired
	private NewsUserEngagementRepository newsUserEngagementRepo;

	@Autowired
	private NewsReportRepository newsReportRepo;

	@Autowired
	private UserRegisterDataRepository userRegisterDataRepo;

	@Autowired
	private NewsImageAndVideoFile newsFileStore;

	@Autowired
	private NotificationService notificationService;

	NewsDataServiceImpl(EmailSenderService emailSenderService) {
		this.emailSenderService = emailSenderService;
	}

	public void addANewsData(NewsDataDTO newsDataDto, MultipartFile[] files, Principal principal) {
		try {
			String uploadFolder = "newsUploads";
			List<String> imagePaths = List.of(); // default empty list

			if (files != null && files.length > 0) {
				imagePaths = newsFileStore.getNewsImageAndVideoFilepaths(files, uploadFolder);
			}

			NewsData newsData = new NewsData();
			newsData.setNewsTitle(newsDataDto.getNewsTitle());
			newsData.setNewsDescription(newsDataDto.getNewsDescription());
			newsData.setImageOrVideoUrl(imagePaths);
			newsData.setAuthor(principal.getName());
			newsData.setCategory(newsDataDto.getCategory());
			newsData.setTags(newsDataDto.getTags());
			newsData.setStatus(newsDataDto.getStatus() != null ? newsDataDto.getStatus() : NewsStatus.PUBLISHED);

			newsDataRepository.save(newsData);

			// ✅ Fetch news author and liker
			UserRegisterData newsAuthor = userRegisterDataRepo.findByEmailId(principal.getName())
					.orElseThrow(() -> new RuntimeException("News author not found"));

			// ✅ 🔔 Send push notification before email
			if (newsAuthor.getDeviceToken() != null) {
				String title = "🔥 You Post a News!";
				String body = newsAuthor.getUserName() + " You posted a new news: \"" + newsData.getNewsTitle() + "\"";
				notificationService.sendCustomNotification(newsAuthor.getDeviceToken(), title, body,
						newsAuthor.getEmailId());
			}
		} catch (IOException e) {
			throw new RuntimeException("Error while saving news file", e);
		}
	}

	@Override
	public List<NewsResponseDTO> getNewsDataFromHomePage(String username) {
		Pageable limit = PageRequest.of(0, 4);
		return newsDataRepository.findTop4LatestNews(limit, username);
	}

	@Override
	public List<NewsResponseDTO> getLastOneMonthNewsData(String userName) {
		LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
		return newsDataRepository.findPublishedNewsLastMonth(oneMonthAgo, userName);
	}

	@Override
	public NewsDataDTO getNewsForEdit(Long sNo) {
		NewsData news = newsDataRepository.findById(sNo)
				.orElseThrow(() -> new RuntimeException("News not found with id: " + sNo));

		// Convert Entity -> DTO
		NewsDataDTO dto = new NewsDataDTO();
		dto.setNewsTitle(news.getNewsTitle());
		dto.setNewsDescription(news.getNewsDescription());
		dto.setCategory(news.getCategory());
		dto.setTags(news.getTags());
		dto.setStatus(news.getStatus());
		return dto;
	}

	@Transactional
	public LikeResponseDTO toggleLike(Long newsId, String username) {
		NewsData news = newsDataRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News not found"));

		NewsUserEngagement engagement = newsUserEngagementRepo.findByNewsAndUsername(news, username)
				.orElseGet(() -> new NewsUserEngagement(null, username, false, false, false, news));

		NewsEngagedStatus status = newsStatusRepository.findByNews(news).orElseGet(() -> {
			NewsEngagedStatus s = new NewsEngagedStatus();
			s.setNews(news);
			s.setLikes(0L);
			s.setUnLikes(0L);
			s.setReports(0L);
			s.setViews(0L);
			return s;
		});

		// Toggle like
		boolean likedNow;
		if (engagement.isLiked()) {
			engagement.setLiked(false);
			status.setLikes(Math.max(status.getLikes() - 1, 0));
			likedNow = false;
		} else {
			engagement.setLiked(true);
			// if previously liked → remove that like count
			if (engagement.isDisliked()) {
				engagement.setDisliked(false); // cannot dislike if liked
				status.setUnLikes(Math.max(status.getUnLikes() - 1, 0));
			}

			status.setLikes(status.getLikes() + 1);
			likedNow = true;
		}

		newsUserEngagementRepo.save(engagement);
		newsStatusRepository.save(status);

		// ✅ Fetch news author and liker
		UserRegisterData newsAuthor = userRegisterDataRepo.findByEmailId(news.getAuthor())
				.orElseThrow(() -> new RuntimeException("News author not found"));
		UserRegisterData liker = userRegisterDataRepo.findByEmailId(username)
				.orElseThrow(() -> new RuntimeException("Liker not found"));

		System.out.println("Run to notfications....");
		// ✅ 🔔 Send push notification before email
		if (newsAuthor.getDeviceToken() != null && likedNow) {
			String title = "🔥 New Like on Your Post!";
			String body = liker.getUserName() + " liked your news: \"" + news.getNewsTitle() + "\"";
			notificationService.sendCustomNotification(newsAuthor.getDeviceToken(), title, body, newsAuthor.getEmailId()

			);

		}

		System.out.println("Complete send to notfications....");

		return new LikeResponseDTO(news.getsNo(), status.getLikes(), likedNow);
	}

	@Transactional
	public UnlikeResponseDTO toggleUnLike(Long newsId, String username) {
		NewsData news = newsDataRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News not found"));

		NewsUserEngagement engagement = newsUserEngagementRepo.findByNewsAndUsername(news, username)
				.orElseGet(() -> new NewsUserEngagement(null, username, false, false, false, news));

		NewsEngagedStatus status = newsStatusRepository.findByNews(news).orElseGet(() -> {
			NewsEngagedStatus s = new NewsEngagedStatus();
			s.setNews(news);
			s.setLikes(0L);
			s.setUnLikes(0L);
			s.setReports(0L);
			s.setViews(0L);
			return s;
		});

		// Toggle unlike
		boolean unlikedNow;
		if (engagement.isDisliked()) {
			engagement.setDisliked(false);
			status.setUnLikes(Math.max(status.getUnLikes() - 1, 0));
			unlikedNow = false;
		} else {
			engagement.setDisliked(true);
			// if previously liked → remove that like count
			if (engagement.isLiked()) {
				engagement.setLiked(false);
				status.setLikes(Math.max(status.getLikes() - 1, 0));
			}
			status.setUnLikes(status.getUnLikes() + 1);
			unlikedNow = true;
		}

		newsUserEngagementRepo.save(engagement);
		newsStatusRepository.save(status);
		return new UnlikeResponseDTO(news.getsNo(), status.getUnLikes(), unlikedNow);
	}

	@Transactional
	public ViewedResponseDTO addView(Long newsId, String username) {
		NewsData news = newsDataRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News not found"));

		NewsUserEngagement engagement = newsUserEngagementRepo.findByNewsAndUsername(news, username)
				.orElseGet(() -> new NewsUserEngagement(null, username, false, false, false, news));

		NewsEngagedStatus status = newsStatusRepository.findByNews(news).orElseGet(() -> {
			NewsEngagedStatus s = new NewsEngagedStatus();
			s.setNews(news);
			s.setLikes(0L);
			s.setUnLikes(0L);
			s.setReports(0L);
			s.setViews(0L);
			return s;
		});

		// Add view only once per user
		boolean viewed = false;
		if (!engagement.isViewed()) {
			engagement.setViewed(true);
			status.setViews(status.getViews() + 1);
			viewed = true;
		}

		newsUserEngagementRepo.save(engagement);
		newsStatusRepository.save(status);
		return new ViewedResponseDTO(news.getsNo(), status.getViews(), viewed);
	}

	@Override
	public boolean addNewsReport(Long newsId, NewsReportDTO newsReportData, Principal principal) {

		// ✅ Check if report already exists
		boolean exists = newsReportRepo.existsByUserEmailAndNews_sNo(principal.getName(), newsId);
		if (exists)
			return false;

		// ✅ Fetch reporter
		UserRegisterData reporter = userRegisterDataRepo.findByEmailId(principal.getName())
				.orElseThrow(() -> new RuntimeException("Reporter not found"));

		// ✅ Fetch news
		NewsData news = newsDataRepository.findById(newsId)
				.orElseThrow(() -> new ResourcesNotFoundException("News not found"));

		// ✅ Create and save report
		NewsReports report = NewsReports.builder().userEmail(reporter.getEmailId()).userName(reporter.getUserName())
				.userMobileNumber(reporter.getUserMobileNumber()).reportContent(newsReportData.getReportContent())
				.reason(newsReportData.getReasonMode()).news(news).build();

		newsReportRepo.save(report);

		// ✅ Fetch news author
		UserRegisterData newsAuthor = userRegisterDataRepo.findByEmailId(news.getAuthor())
				.orElseThrow(() -> new RuntimeException("News author not found"));

		// ✅ 🔔 Send push notification before email
		if (newsAuthor.getDeviceToken() != null) {
			String reporterName = reporter.getUserName();
			String title = "⚠️ News Report Alert!";
			String body = "Your news '" + news.getNewsTitle() + "' has been reported by " + reporterName + ".";
			notificationService.sendCustomNotification(newsAuthor.getDeviceToken(), title, body,
					newsAuthor.getEmailId());
		}

		// ✅ Send email asynchronously
		emailSenderService.sendEmailPostReportReminderFromNewStatus(newsAuthor.getEmailId(), newsAuthor.getUserName(),
				news.getsNo(), news.getNewsTitle(), report.getsNo(), report.getReportContent());

		return true;
	}

	public boolean newsPostMoveToArchive(Long id, Principal principal) {
		Optional<NewsData> optionalNews = newsDataRepository.findById(id);

		if (optionalNews.isEmpty()) {
			return false; // News not found
		}

		NewsData news = optionalNews.get();
		boolean isValidPostForLoginUser = news.getAuthor().equals(principal.getName());

		if (!isValidPostForLoginUser) {
			return false; // Not the logged-in user's post
		}

		news.setStatus(NewsStatus.ARCHIEVED); // spelling ARCHIVED
		newsDataRepository.save(news);

		return true;
	}

	public boolean newsPostMoveToDraft(Long id, Principal principal) {
		Optional<NewsData> optionalNews = newsDataRepository.findById(id);

		if (optionalNews.isEmpty()) {
			return false; // not found
		}

		NewsData news = optionalNews.get();

		boolean isValidPostForLoginUser = news.getAuthor().equals(principal.getName());

		if (!isValidPostForLoginUser) {
			return false; // Not the logged-in user's post
		}

		news.setStatus(NewsStatus.DRAFT);
		newsDataRepository.save(news);

		return true;
	}

	public boolean newsPostMoveToPublished(Long id, Principal principal) {
		Optional<NewsData> optionalNews = newsDataRepository.findById(id);

		if (optionalNews.isEmpty()) {
			return false; // not found
		}

		NewsData news = optionalNews.get();

		boolean isValidPostForLoginUser = news.getAuthor().equals(principal.getName());

		if (!isValidPostForLoginUser) {
			return false; // Not the logged-in user's post
		}

		news.setStatus(NewsStatus.PUBLISHED);
		newsDataRepository.save(news);

		return true;
	}

	@Override
	@Transactional
	public boolean deleteNewsData(Long id, Principal principal) {
		String loggedInUser = principal.getName();

		// Step 1: Find the news by ID
		NewsData news = newsDataRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

		// Step 2: Check if logged-in user is the author
		if (!news.getAuthor().equalsIgnoreCase(loggedInUser)) {
			throw new RuntimeException("You are not authorized to delete this news post!");
		}

		// ✅ Step 3: Delete all files (image/video URLs)
		List<String> urls = news.getImageOrVideoUrl(); // 👈 Already a List<String>
		if (urls != null && !urls.isEmpty()) {
			for (String fileUrl : urls) {
				if (fileUrl != null && !fileUrl.isBlank()) {
					newsFileStore.deleteFileFromSupabase(fileUrl.trim());
				}
			}
		}

		// Step 3: Delete the news (cascades will handle related data)
		newsDataRepository.delete(news);

		return true;
	}

}
