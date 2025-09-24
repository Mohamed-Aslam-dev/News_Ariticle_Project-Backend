package com.ilayangudi_news_posting.response_dto;

public class SuperAdminAllDataResponse {

	private Long usersCounts;
	private Long totalNewsCounts;
	private Long totalArchievedNewsCounts;
	private Long totalPublishedNewsCounts;
	private Long totalDraftNewsCounts;
	private Long totalLikes;
	private Long totalUnlikes;
	private Long totalViews;
	private Long totalReports;

	public SuperAdminAllDataResponse() {

	}

	public SuperAdminAllDataResponse(Long usersCounts, Long totalNewsCounts, Long totalArchievedNewsCounts,
			Long totalPublishedNewsCounts, Long totalDraftNewsCounts, Long totalLikes, Long totalUnlikes,
			Long totalViews, Long totalReports) {

		this.usersCounts = usersCounts;
		this.totalNewsCounts = totalNewsCounts;
		this.totalArchievedNewsCounts = totalArchievedNewsCounts;
		this.totalPublishedNewsCounts = totalPublishedNewsCounts;
		this.totalDraftNewsCounts = totalDraftNewsCounts;
		this.totalLikes = totalLikes;
		this.totalUnlikes = totalUnlikes;
		this.totalViews = totalViews;
		this.totalReports = totalReports;
	}

	public Long getUsersCounts() {
		return usersCounts;
	}

	public void setUsersCounts(Long usersCounts) {
		this.usersCounts = usersCounts;
	}

	public Long getTotalNewsCounts() {
		return totalNewsCounts;
	}

	public void setTotalNewsCounts(Long totalNewsCounts) {
		this.totalNewsCounts = totalNewsCounts;
	}

	public Long getTotalArchievedNewsCounts() {
		return totalArchievedNewsCounts;
	}

	public void setTotalArchievedNewsCounts(Long totalArchievedNewsCounts) {
		this.totalArchievedNewsCounts = totalArchievedNewsCounts;
	}

	public Long getTotalPublishedNewsCounts() {
		return totalPublishedNewsCounts;
	}

	public void setTotalPublishedNewsCounts(Long totalPublishedNewsCounts) {
		this.totalPublishedNewsCounts = totalPublishedNewsCounts;
	}

	public Long getTotalDraftNewsCounts() {
		return totalDraftNewsCounts;
	}

	public void setTotalDraftNewsCounts(Long totalDraftNewsCounts) {
		this.totalDraftNewsCounts = totalDraftNewsCounts;
	}

	public Long getTotalLikes() {
		return totalLikes;
	}

	public void setTotalLikes(Long totalLikes) {
		this.totalLikes = totalLikes;
	}

	public Long getTotalUnlikes() {
		return totalUnlikes;
	}

	public void setTotalUnlikes(Long totalUnlikes) {
		this.totalUnlikes = totalUnlikes;
	}

	public Long getTotalViews() {
		return totalViews;
	}

	public void setTotalViews(Long totalViews) {
		this.totalViews = totalViews;
	}

	public Long getTotalReports() {
		return totalReports;
	}

	public void setTotalReports(Long totalReports) {
		this.totalReports = totalReports;
	}

	@Override
	public String toString() {
		return "SuperAdminAllDataResponse [usersCounts=" + usersCounts + ", totalNewsCounts=" + totalNewsCounts
				+ ", totalArchievedNewsCounts=" + totalArchievedNewsCounts + ", totalPublishedNewsCounts="
				+ totalPublishedNewsCounts + ", totalDraftNewsCounts=" + totalDraftNewsCounts + ", totalLikes="
				+ totalLikes + ", totalUnlikes=" + totalUnlikes + ", totalViews=" + totalViews + ", totalReports="
				+ totalReports + "]";
	}

}
