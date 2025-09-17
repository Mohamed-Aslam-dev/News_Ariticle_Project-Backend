package com.ilayangudi_news_posting.servicerepo;

import java.security.Principal;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ilayangudi_news_posting.entity.NewsEngagedStatus;
import com.ilayangudi_news_posting.request_dto.NewsDataDTO;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;

public interface NewsDataServiceRepository {

	public void addANewsData(NewsDataDTO newsDataDto, MultipartFile[] file, Principal principal);

	List<NewsResponseDTO> getNewsDataFromHomePage();

	public List<NewsResponseDTO> getLastOneMonthNewsData();

	NewsDataDTO getNewsForEdit(Long sNo);

	public NewsEngagedStatus toggleLike(Long newsId, String username);

	public NewsEngagedStatus toggleUnLike(Long newsId, String username);

	public NewsEngagedStatus addView(Long newsId, String username);

	public List<NewsResponseDTO> getLastOneMonthPublishedNewsData(Principal principal);

	public List<NewsResponseDTO> getLastOneMonthDraftNewsData(Principal principal);

	public List<NewsResponseDTO> getLastOneMonthArchievedNewsData(Principal principal);

	public boolean newsPostMoveToArchive(Long id, Principal principal);

	public boolean newsPostMoveToDraft(Long id, Principal principal);

	public boolean newsPostMoveToPublished(Long id, Principal principal);

}
