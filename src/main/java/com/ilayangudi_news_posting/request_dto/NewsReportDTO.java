package com.ilayangudi_news_posting.request_dto;

import com.ilayangudi_news_posting.enums.ReportReason;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewsReportDTO {

	@NotBlank(message = "அறிக்கை உள்ளடக்கம் காலியாக இருக்க கூடாது")
	@Size(max = 500, message = "அறிக்கை உள்ளடக்கம் 500 எழுத்துக்களை மீறக் கூடாது")
	private String reportContent;

	private ReportReason reasonMode = ReportReason.NEW; // Optional

	public NewsReportDTO() {

	}

	public NewsReportDTO(
			@NotBlank(message = "அறிக்கை உள்ளடக்கம் காலியாக இருக்க கூடாது") @Size(max = 500, message = "அறிக்கை உள்ளடக்கம் 500 எழுத்துக்களை மீறக் கூடாது") String reportContent,
			ReportReason reasonMode) {

		this.reportContent = reportContent;
		this.reasonMode = reasonMode;
	}

	public String getReportContent() {
		return reportContent;
	}

	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}

	public ReportReason getReasonMode() {
		return reasonMode;
	}

	public void setReasonMode(ReportReason reasonMode) {
		this.reasonMode = reasonMode;
	}

	@Override
	public String toString() {
		return "NewsReportDTO [reportContent=" + reportContent + ", reasonMode=" + reasonMode + "]";
	}

}
