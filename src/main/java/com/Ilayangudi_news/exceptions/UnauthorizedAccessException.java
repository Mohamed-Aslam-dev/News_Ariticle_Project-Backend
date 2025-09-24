package com.Ilayangudi_news.exceptions;

public class UnauthorizedAccessException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public UnauthorizedAccessException(){
		super();
	}
	
	public UnauthorizedAccessException(String message) {
        super(message);
    }

}
