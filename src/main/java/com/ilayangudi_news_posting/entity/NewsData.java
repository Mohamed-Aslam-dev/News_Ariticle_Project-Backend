package com.ilayangudi_news_posting.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ilayangudi_news_posting.convertor.NewsTagsConverter;
import com.ilayangudi_news_posting.enums.NewsStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
	@Convert(converter = NewsTagsConverter.class) // same approach like tags
	@Column(length = 1000)
	private List<String> imageOrVideoUrl = new ArrayList<>();
	private String author;
	private String category;
	@Convert(converter = NewsTagsConverter.class)
	private List<String> tags = new ArrayList<>();
	@Enumerated(EnumType.STRING)
	private NewsStatus status;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	// Relationship
    @OneToOne(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private NewsEngagedStatus newsEngagedStatus;

	public NewsData() {

	}

	public NewsData(Long sNo, String newsTitle, String newsDescription, List<String> imageOrVideoUrl, String author,
			String category, List<String> tags, NewsStatus status, Date createdAt, Date updatedAt,
			NewsEngagedStatus newsEngagedStatus) {
		super();
		this.sNo = sNo;
		this.newsTitle = newsTitle;
		this.newsDescription = newsDescription;
		this.imageOrVideoUrl = imageOrVideoUrl;
		this.author = author;
		this.category = category;
		this.tags = tags;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.newsEngagedStatus = newsEngagedStatus;
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

	public List<String> getImageOrVideoUrl() {
		return imageOrVideoUrl;
	}

	public void setImageOrVideoUrl(List<String> imageOrVideoUrl) {
		this.imageOrVideoUrl = imageOrVideoUrl;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public NewsEngagedStatus getNewsEngagedStatus() {
		return newsEngagedStatus;
	}

	public void setNewsEngagedStatus(NewsEngagedStatus newsEngagedStatus) {
		this.newsEngagedStatus = newsEngagedStatus;
	}

	@Override
	public String toString() {
		return "NewsData [sNo=" + sNo + ", newsTitle=" + newsTitle + ", newsDescription=" + newsDescription
				+ ", imageOrVideoUrl=" + imageOrVideoUrl + ", author=" + author + ", category=" + category + ", tags="
				+ tags + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", newsEngagedStatus=" + newsEngagedStatus + "]";
	}

}
