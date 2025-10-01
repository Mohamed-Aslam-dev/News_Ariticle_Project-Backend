package com.ilayangudi_news_posting.response_dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

public class NewsResponseDTO {

	private Long sNo;
	private String newsTitle;
	private String newsDescription;
	private List<String> imageOrVideoUrl;
	private String authorEmail;
	private String authorName;
	private String authorProfileUrl;
	private String category;
	private String tags;
	private String status;
	private Long views;
	private Long likes;
	private Long unLikes;
	private boolean likedByCurrentUser; // ✅ indicate heart fill
	private boolean unLikedByCurrentUser; // ✅ indicate heart fill
	private boolean viewedByCurrentUser; // ✅ indicate heart fill
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Date createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Date updatedAt;

	public NewsResponseDTO() {

	}

	public NewsResponseDTO(Long sNo, String newsTitle, String newsDescription, String imageOrVideoUrl,
			String authorEmail, String authorName, String authorProfileUrl, String category, String tags, String status,
			Long views, Long likes, Long unlikes, boolean likedByCurrentUser, boolean unLikedByCurrentUser,
			boolean viewedByCurrentUser, Date createdAt, Date updatedAt) {
		this.sNo = sNo;
		this.newsTitle = newsTitle;
		this.newsDescription = newsDescription;
		this.authorEmail = authorEmail;
		this.authorName = authorName;
		this.authorProfileUrl = authorProfileUrl;
		// split comma-separated string into list
		this.imageOrVideoUrl = imageOrVideoUrl != null ? Arrays.asList(imageOrVideoUrl.split(",")) : new ArrayList<>();
		this.category = category;
		this.tags = tags;
		this.status = status;
		this.views = views;
		this.likes = likes;
		this.likedByCurrentUser = likedByCurrentUser;
		this.unLikedByCurrentUser = unLikedByCurrentUser;
		this.viewedByCurrentUser = viewedByCurrentUser;
		this.unLikes = unlikes;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getsNo() {
		return sNo;
	}

	public void setsNo(Long sNo) {
		this.sNo = sNo;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsDescription() {
		return newsDescription;
	}

	public void setNewsDescription(String newsDescription) {
		this.newsDescription = newsDescription;
	}

	public List<String> getImageOrVideoUrl() {
		return imageOrVideoUrl;
	}

	public void setImageOrVideoUrl(List<String> imageOrVideoUrl) {
		this.imageOrVideoUrl = imageOrVideoUrl;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorProfileUrl() {
		return authorProfileUrl;
	}

	public void setAuthorProfileUrl(String authorProfileUrl) {
		this.authorProfileUrl = authorProfileUrl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}

	public Long getLikes() {
		return likes;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}

	public Long getUnLikes() {
		return unLikes;
	}

	public void setUnLikes(Long unLikes) {
		this.unLikes = unLikes;
	}

	public boolean isLikedByCurrentUser() {
		return likedByCurrentUser;
	}

	public void setLikedByCurrentUser(boolean likedByCurrentUser) {
		this.likedByCurrentUser = likedByCurrentUser;
	}

	public boolean isUnLikedByCurrentUser() {
		return unLikedByCurrentUser;
	}

	public void setUnLikedByCurrentUser(boolean unLikedByCurrentUser) {
		this.unLikedByCurrentUser = unLikedByCurrentUser;
	}

	public boolean isViewedByCurrentUser() {
		return viewedByCurrentUser;
	}

	public void setViewedByCurrentUser(boolean viewedByCurrentUser) {
		this.viewedByCurrentUser = viewedByCurrentUser;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "NewsResponseDTO [sNo=" + sNo + ", newsTitle=" + newsTitle + ", newsDescription=" + newsDescription
				+ ", imageOrVideoUrl=" + imageOrVideoUrl + ", authorEmail=" + authorEmail + ", authorName=" + authorName
				+ ", authorProfileUrl=" + authorProfileUrl + ", category=" + category + ", tags=" + tags + ", status="
				+ status + ", views=" + views + ", likes=" + likes + ", unLikes=" + unLikes + ", likedByCurrentUser="
				+ likedByCurrentUser + ", unLikedByCurrentUser=" + unLikedByCurrentUser + ", viewedByCurrentUser="
				+ viewedByCurrentUser + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
