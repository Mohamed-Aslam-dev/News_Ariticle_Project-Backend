package com.ilayangudi_news_posting.response_dto;

public record ApiResponse<T>(String message, T data) {

}
