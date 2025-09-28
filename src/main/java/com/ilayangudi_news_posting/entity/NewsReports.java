package com.ilayangudi_news_posting.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ilayangudi_news_posting.enums.ReportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;

@Entity
@Builder
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "user_email", "news_id", "user_mobile_number" }) })
public class NewsReports {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sNo;

	@Column(nullable = false)
	private String userEmail;

	private String userName;

	@Column(nullable = false)
	private String userMobileNumber;

	private String reportContent;

	@Enumerated(EnumType.STRING)
	private ReportStatus reason;

	@CreationTimestamp
	private LocalDateTime reportedAt;
	
	private LocalDateTime reviewedAt;
	
	private LocalDateTime deletedAt;

	// Foreign key to NewsData
	@ManyToOne
	@JoinColumn(name = "news_id", nullable = false)
	@JsonBackReference
	private NewsData news;

	public NewsReports() {
		
	}

	public NewsReports(Long sNo, String userEmail, String userName, String userMobileNumber, String reportContent,
			ReportStatus reason, LocalDateTime reportedAt, LocalDateTime reviewedAt, LocalDateTime deletedAt,
			NewsData news) {
		super();
		this.sNo = sNo;
		this.userEmail = userEmail;
		this.userName = userName;
		this.userMobileNumber = userMobileNumber;
		this.reportContent = reportContent;
		this.reason = reason;
		this.reportedAt = reportedAt;
		this.reviewedAt = reviewedAt;
		this.deletedAt = deletedAt;
		this.news = news;
	}

	public Long getsNo() {
		return sNo;
	}

	public void setsNo(Long sNo) {
		this.sNo = sNo;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

	public String getReportContent() {
		return reportContent;
	}

	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}

	public ReportStatus getReason() {
		return reason;
	}

	public void setReason(ReportStatus reason) {
		this.reason = reason;
	}

	public LocalDateTime getReportedAt() {
		return reportedAt;
	}

	public void setReportedAt(LocalDateTime reportedAt) {
		this.reportedAt = reportedAt;
	}
	
	public LocalDateTime getReviewedAt() {
		return reviewedAt;
	}

	public void setReviewedAt(LocalDateTime reviewedAt) {
		this.reviewedAt = reviewedAt;
	}
	
	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}

	public NewsData getNews() {
		return news;
	}

	public void setNews(NewsData news) {
		this.news = news;
	}

	@Override
	public String toString() {
		return "NewsReports [sNo=" + sNo + ", userEmail=" + userEmail + ", userName=" + userName + ", userMobileNumber="
				+ userMobileNumber + ", reportContent=" + reportContent + ", reason=" + reason + ", reportedAt="
				+ reportedAt + ", reviewedAt=" + reviewedAt + ", news=" + news + "]";
	}

	



	

}
