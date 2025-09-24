package com.ilayangudi_news_posting.response_dto;

import java.time.LocalDateTime;
import com.ilayangudi_news_posting.enums.ReportStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class SuperAdminReportsResponse {

	private String reporterEmail;

	private String reporterName;

	private String reporterMobileNumber;

	private String reportContent;

	@Enumerated(EnumType.STRING)
	private ReportStatus reportStatus;

	private Long newsId;

	private String newsTitle;

	private LocalDateTime reportedAt;

	private String newsPosterName;

	private String newsPosterEmail;

	private String newsPosterMobileNumber;

	public SuperAdminReportsResponse() {

	}

	public SuperAdminReportsResponse(String reporterEmail, String reporterName, String reporterMobileNumber,
			String reportContent, ReportStatus reportStatus, Long newsId, String newsTitle, LocalDateTime reportedAt,
			String newsPosterName, String newsPosterEmail, String newsPosterMobileNumber) {

		this.reporterEmail = reporterEmail;
		this.reporterName = reporterName;
		this.reporterMobileNumber = reporterMobileNumber;
		this.reportContent = reportContent;
		this.reportStatus = reportStatus;
		this.newsId = newsId;
		this.newsTitle = newsTitle;
		this.reportedAt = reportedAt;
		this.newsPosterName = newsPosterName;
		this.newsPosterEmail = newsPosterEmail;
		this.newsPosterMobileNumber = newsPosterMobileNumber;
	}

	public String getReporterEmail() {
		return reporterEmail;
	}

	public void setReporterEmail(String reporterEmail) {
		this.reporterEmail = reporterEmail;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getReporterMobileNumber() {
		return reporterMobileNumber;
	}

	public void setReporterMobileNumber(String reporterMobileNumber) {
		this.reporterMobileNumber = reporterMobileNumber;
	}

	public String getReportContent() {
		return reportContent;
	}

	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}

	public ReportStatus getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(ReportStatus reportStatus) {
		this.reportStatus = reportStatus;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public LocalDateTime getReportedAt() {
		return reportedAt;
	}

	public void setReportedAt(LocalDateTime reportedAt) {
		this.reportedAt = reportedAt;
	}

	public String getNewsPosterName() {
		return newsPosterName;
	}

	public void setNewsPosterName(String newsPosterName) {
		this.newsPosterName = newsPosterName;
	}

	public String getNewsPosterEmail() {
		return newsPosterEmail;
	}

	public void setNewsPosterEmail(String newsPosterEmail) {
		this.newsPosterEmail = newsPosterEmail;
	}

	public String getNewsPosterMobileNumber() {
		return newsPosterMobileNumber;
	}

	public void setNewsPosterMobileNumber(String newsPosterMobileNumber) {
		this.newsPosterMobileNumber = newsPosterMobileNumber;
	}

	@Override
	public String toString() {
		return "SuperAdminReportsResponse [reporterEmail=" + reporterEmail + ", reporterName=" + reporterName
				+ ", reporterMobileNumber=" + reporterMobileNumber + ", reportContent=" + reportContent
				+ ", reportStatus=" + reportStatus + ", newsId=" + newsId + ", newsTitle=" + newsTitle + ", reportedAt="
				+ reportedAt + ", newsPosterName=" + newsPosterName + ", newsPosterEmail=" + newsPosterEmail
				+ ", newsPosterMobileNumber=" + newsPosterMobileNumber + "]";
	}

}
