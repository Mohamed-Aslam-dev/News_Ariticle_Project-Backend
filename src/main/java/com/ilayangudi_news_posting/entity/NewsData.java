package com.ilayangudi_news_posting.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.ilayangudi_news_posting.convertor.NewsTagsConverter;
import com.ilayangudi_news_posting.enums.NewsStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class NewsData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sNo;
	private String newsTitle;
	@Column(columnDefinition = "TEXT")
	private String newsDescription;
	private String imageOrVideoUrl;
	private String author;
	private String category;
	@Convert(converter = NewsTagsConverter.class)
	private List<String> tags = new ArrayList<>();
	@Enumerated(EnumType.STRING)
	private NewsStatus status;
	private Long views = 0L;
	private Long likes = 0L;
	private Long unLikes = 0L;
	private Long reports = 0L;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	public NewsData() {

	}

	public NewsData(Long sNo, String newsTitle, String newsDescription, String imageOrVideoUrl, String author,
			String category, List<String> tags, NewsStatus status, Long views, Long likes, Long unLikes, Long reports,
			Date createdAt, Date updatedAt) {
		super();
		this.sNo = sNo;
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
		this.reports = reports;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getsNo() {
		return sNo;
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public NewsStatus getStatus() {
		return status;
	}

	public void setStatus(NewsStatus status) {
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

	public Long getReports() {
		return reports;
	}

	public void setReports(Long reports) {
		this.reports = reports;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
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

	public String getImageOrVideoUrl() {
		return imageOrVideoUrl;
	}

	public void setImageOrVideoUrl(String imageOrVideoUrl) {
		this.imageOrVideoUrl = imageOrVideoUrl;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "NewsData [sNo=" + sNo + ", newsTitle=" + newsTitle + ", newsDescription=" + newsDescription
				+ ", imageOrVideoUrl=" + imageOrVideoUrl + ", author=" + author + ", category=" + category + ", tags="
				+ tags + ", status=" + status + ", views=" + views + ", likes=" + likes + ", unLikes=" + unLikes
				+ ", reports=" + reports + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
