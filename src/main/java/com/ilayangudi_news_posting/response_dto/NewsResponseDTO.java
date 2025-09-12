package com.ilayangudi_news_posting.response_dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class NewsResponseDTO {

	private String newsTitle;
	private String newsDescription;
	private List<String> imageOrVideoUrl;
	private String author;
	private String category;
	private String tags;
	private String status;
	private Long views;
	private Long likes;
	private Long unLikes;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Date createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Date updatedAt;

	public NewsResponseDTO() {

	}

	public NewsResponseDTO(String newsTitle, String newsDescription, List<String> imageOrVideoUrl, String author,
			String category, String tags, String status, Long views, Long likes, Long unLikes,
			Date createdAt, Date updatedAt) {
		super();
		this.newsTitle = newsTitle;
		this.newsDescription = newsDescription;
		this.imageOrVideoUrl = imageOrVideoUrl;
		this.author = author;
		this.category = category;
		this.tags = tags;
		this.status = status;
		this.views = views;
		this.likes = likes;
		this.unLikes = unLikes;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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
		return "NewsResponseDTO [newsTitle=" + newsTitle + ", newsDescription=" + newsDescription + ", imageOrVideoUrl="
				+ imageOrVideoUrl + ", author=" + author + ", category=" + category + ", tags=" + tags + ", status="
				+ status + ", views=" + views + ", likes=" + likes + ", unLikes=" + unLikes + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}

}
