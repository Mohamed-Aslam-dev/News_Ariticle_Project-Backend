package com.ilayangudi_news_posting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class NewsUserEngagement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private boolean liked;
	private boolean disliked;
	private boolean viewed;

	@ManyToOne
	@JoinColumn(name = "news_id", nullable = false)
	@JsonBackReference
	private NewsData news;

	public NewsUserEngagement() {

	}

	public NewsUserEngagement(Long id, String username, boolean liked, boolean disliked, boolean viewed,
			NewsData news) {

		this.id = id;
		this.username = username;
		this.liked = liked;
		this.disliked = disliked;
		this.viewed = viewed;
		this.news = news;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public boolean isDisliked() {
		return disliked;
	}

	public void setDisliked(boolean disliked) {
		this.disliked = disliked;
	}

	public boolean isViewed() {
		return viewed;
	}

	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

	public NewsData getNews() {
		return news;
	}

	public void setNews(NewsData news) {
		this.news = news;
	}

	@Override
	public String toString() {
		return "NewUserEngagement [id=" + id + ", username=" + username + ", liked=" + liked + ", disliked=" + disliked
				+ ", viewed=" + viewed + ", news=" + news + "]";
	}

}
