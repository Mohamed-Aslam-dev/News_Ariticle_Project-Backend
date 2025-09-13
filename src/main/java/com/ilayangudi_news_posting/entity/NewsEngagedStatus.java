package com.ilayangudi_news_posting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class NewsEngagedStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long views = 0L;
	private Long likes = 0L;
	private Long unLikes = 0L;
	private Long reports = 0L;

	// Foreign key to NewsData
	@OneToOne
	@JoinColumn(name = "news_id", nullable = false, unique = true)
	private NewsData news;

	public NewsEngagedStatus(Long id, Long views, Long likes, Long unLikes, Long reports, NewsData news) {

		this.id = id;
		this.views = views;
		this.likes = likes;
		this.unLikes = unLikes;
		this.reports = reports;
		this.news = news;
	}

	public NewsEngagedStatus() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public NewsData getNews() {
		return news;
	}

	public void setNews(NewsData news) {
		this.news = news;
	}

	@Override
	public String toString() {
		return "NewsEngagedStatus [id=" + id + ", views=" + views + ", likes=" + likes + ", unLikes=" + unLikes
				+ ", reports=" + reports + ", news=" + news + "]";
	}

}
