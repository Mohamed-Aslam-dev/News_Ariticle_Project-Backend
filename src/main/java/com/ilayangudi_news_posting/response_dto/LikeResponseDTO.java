package com.ilayangudi_news_posting.response_dto;

public class LikeResponseDTO {

	private Long newsId;
	private Long totalLikes;
	private boolean likedByCurrentUser;

	public LikeResponseDTO() {

	}

	public LikeResponseDTO(Long newsId, Long totalLikes, boolean likedByCurrentUser) {

		this.newsId = newsId;
		this.totalLikes = totalLikes;
		this.likedByCurrentUser = likedByCurrentUser;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public Long getTotalLikes() {
		return totalLikes;
	}

	public void setTotalLikes(Long totalLikes) {
		this.totalLikes = totalLikes;
	}

	public boolean isLikedByCurrentUser() {
		return likedByCurrentUser;
	}

	public void setLikedByCurrentUser(boolean likedByCurrentUser) {
		this.likedByCurrentUser = likedByCurrentUser;
	}

	@Override
	public String toString() {
		return "LikeResponseDTO [newsId=" + newsId + ", totalLikes=" + totalLikes + ", likedByCurrentUser="
				+ likedByCurrentUser + "]";
	}

}
