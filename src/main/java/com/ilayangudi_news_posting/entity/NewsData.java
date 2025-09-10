package com.ilayangudi_news_posting.entity;

import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Entity;
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
	private String newsDescription;
	private String imageOrVideoUrl;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public NewsData() {
		
	}

	public NewsData(Long sNo, String newsTitle, String newsDescription, String imageOrVideoUrl, Date createdAt) {
		
		this.sNo = sNo;
		this.newsTitle = newsTitle;
		this.newsDescription = newsDescription;
		this.imageOrVideoUrl = imageOrVideoUrl;
		this.createdAt = createdAt;
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
				+ ", imageOrVideoUrl=" + imageOrVideoUrl + ", createdAt=" + createdAt + "]";
	}

}
