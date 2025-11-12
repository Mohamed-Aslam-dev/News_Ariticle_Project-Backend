package com.ilayangudi_news_posting.request_dto;

public class NotificationDevicetokenDTO {

	private String deviceToken;

	public NotificationDevicetokenDTO() {

	}

	public NotificationDevicetokenDTO(String deviceToken) {

		this.deviceToken = deviceToken;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	@Override
	public String toString() {
		return "NotificationDevicetokenDTO [deviceToken=" + deviceToken + "]";
	}

}
