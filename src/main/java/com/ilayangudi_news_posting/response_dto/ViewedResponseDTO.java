package com.ilayangudi_news_posting.response_dto;

public class ViewedResponseDTO {

	private Long newsId;
	private Long totalViews;
	private boolean viewedByCurrentUser;

	public ViewedResponseDTO() {

	}

	public ViewedResponseDTO(Long newsId, Long totalViews, boolean viewedByCurrentUser) {
		super();
		this.newsId = newsId;
		this.totalViews = totalViews;
		this.viewedByCurrentUser = viewedByCurrentUser;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public Long getTotalViews() {
		return totalViews;
	}

	public void setTotalViews(Long totalViews) {
		this.totalViews = totalViews;
	}

	public boolean isViewedByCurrentUser() {
		return viewedByCurrentUser;
	}

	public void setViewedByCurrentUser(boolean viewedByCurrentUser) {
		this.viewedByCurrentUser = viewedByCurrentUser;
	}

	@Override
	public String toString() {
		return "ViewedResponseDTO [newsId=" + newsId + ", totalViews=" + totalViews + ", viewedByCurrentUser="
				+ viewedByCurrentUser + "]";
	}

}
