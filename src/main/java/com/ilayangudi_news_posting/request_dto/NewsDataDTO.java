package com.ilayangudi_news_posting.request_dto;

import java.util.List;
import com.ilayangudi_news_posting.enums.NewsStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewsDataDTO {

	@NotBlank(message = "செய்தி தலைப்பை கண்டிப்பாக உள்ளிடவும்")
	@Size(max = 200, message = "தலைப்பு அதிகபட்சம் 200 எழுத்துகள் மட்டுமே உள்ளிட முடியும்")
	private String newsTitle;

	@NotBlank(message = "செய்தி விவரத்தை கண்டிப்பாக உள்ளிடவும்")
	private String newsDescription; // TEXT → no max limit

	@NotBlank(message = "வகை (Category) கண்டிப்பாக உள்ளிடவும்")
	@Size(max = 50, message = "வகை அதிகபட்சம் 50 எழுத்துகள் இருக்கலாம்")
	private String category;

	@Size(max = 5, message = "அதிகபட்சம் 5 குறிச்சொற்கள் (Tags) சேர்க்கலாம்")
	private List<String> tags;

	private NewsStatus status = NewsStatus.PUBLISHED;

	public NewsDataDTO() {

	}

	public NewsDataDTO(
			@NotBlank(message = "செய்தி தலைப்பை கண்டிப்பாக உள்ளிடவும்") @Size(max = 200, message = "தலைப்பு அதிகபட்சம் 200 எழுத்துகள் மட்டுமே உள்ளிட முடியும்") String newsTitle,
			@NotBlank(message = "செய்தி விவரத்தை கண்டிப்பாக உள்ளிடவும்") String newsDescription, 
			@NotBlank(message = "வகை (Category) கண்டிப்பாக உள்ளிடவும்") @Size(max = 50, message = "வகை அதிகபட்சம் 50 எழுத்துகள் இருக்கலாம்") String category,
			@Size(max = 5, message = "அதிகபட்சம் 5 குறிச்சொற்கள் (Tags) சேர்க்கலாம்") List<String> tags,
			NewsStatus status) {
		super();
		this.newsTitle = newsTitle;
		this.newsDescription = newsDescription;
		this.category = category;
		this.tags = tags;
		this.status = status;
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

	@Override
	public String toString() {
		return "NewsDataDTO [newsTitle=" + newsTitle + ", newsDescription=" + newsDescription + ", category=" + category
				+ ", tags=" + tags + ", status=" + status + "]";
	}

	

}
