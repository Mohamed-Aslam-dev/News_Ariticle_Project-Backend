package com.ilayangudi_news_posting.servicerepo;

import java.security.Principal;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ilayangudi_news_posting.entity.NewsEngagedStatus;
import com.ilayangudi_news_posting.request_dto.NewsDataDTO;
import com.ilayangudi_news_posting.response_dto.LikeResponseDTO;
import com.ilayangudi_news_posting.response_dto.NewsResponseDTO;
import com.ilayangudi_news_posting.response_dto.UnlikeResponseDTO;
import com.ilayangudi_news_posting.response_dto.ViewedResponseDTO;

public interface NewsDataServiceRepository {

	public void addANewsData(NewsDataDTO newsDataDto, MultipartFile[] file, Principal principal);

	List<NewsResponseDTO> getNewsDataFromHomePage();

	public List<NewsResponseDTO> getLastOneMonthNewsData(String userName);

	NewsDataDTO getNewsForEdit(Long sNo);

	public LikeResponseDTO toggleLike(Long newsId, String username);

	public UnlikeResponseDTO toggleUnLike(Long newsId, String username);

	public ViewedResponseDTO addView(Long newsId, String username);

	public boolean newsPostMoveToArchive(Long id, Principal principal);

	public boolean newsPostMoveToDraft(Long id, Principal principal);

	public boolean newsPostMoveToPublished(Long id, Principal principal);

}
