package com.ilayangudi_news_posting.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewsDataDTO {

	@NotBlank(message = "செய்தி தலைப்பு அவசியம் தேவை")
	@Size(max = 100, message = "செய்தி தலைப்பு அதிகபட்சம் 100 எழுத்துகள் மட்டுமே இருக்க வேண்டும்")
	private String newsTitle;
	@NotBlank(message = "செய்தி விவரம் அவசியம் தேவை")
	private String newsDescription;

	public NewsDataDTO() {

	}

	public NewsDataDTO(
			@NotBlank(message = "செய்தி தலைப்பு அவசியம் தேவை") @Size(max = 100, message = "செய்தி தலைப்பு அதிகபட்சம் 100 எழுத்துகள் மட்டுமே இருக்க வேண்டும்") String newsTitle,
			@NotBlank(message = "செய்தி விவரம் அவசியம் தேவை") String newsDescription) {

		this.newsTitle = newsTitle;
		this.newsDescription = newsDescription;

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


	@Override
	public String toString() {
		return "NewsDataDTO [newsTitle=" + newsTitle + ", newsDescription=" + newsDescription + "]";
	}

}
