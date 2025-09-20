package com.ilayangudi_news_posting.response_dto;

public class UnlikeResponseDTO {

	private Long newsId;
	private Long totalUnLikes;
	private boolean unLikedByCurrentUser;

	public UnlikeResponseDTO() {

	}

	public UnlikeResponseDTO(Long newsId, Long totalUnLikes, boolean unLikedByCurrentUser) {

		this.newsId = newsId;
		this.totalUnLikes = totalUnLikes;
		this.unLikedByCurrentUser = unLikedByCurrentUser;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public Long getTotalUnLikes() {
		return totalUnLikes;
	}

	public void setTotalUnLikes(Long totalUnLikes) {
		this.totalUnLikes = totalUnLikes;
	}

	public boolean isUnLikedByCurrentUser() {
		return unLikedByCurrentUser;
	}

	public void setUnLikedByCurrentUser(boolean unLikedByCurrentUser) {
		this.unLikedByCurrentUser = unLikedByCurrentUser;
	}

	@Override
	public String toString() {
		return "UnlikeResponseDTO [newsId=" + newsId + ", totalUnLikes=" + totalUnLikes + ", unLikedByCurrentUser="
				+ unLikedByCurrentUser + "]";
	}

}
