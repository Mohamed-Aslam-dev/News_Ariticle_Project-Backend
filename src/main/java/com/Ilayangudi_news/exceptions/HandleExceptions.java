package com.Ilayangudi_news.exceptions;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class HandleExceptions {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException validException) {

		HashMap<String, String> exceptionMap = new HashMap<>();

		validException.getBindingResult().getAllErrors().forEach((exception) -> {
			String errorField = ((FieldError) exception).getField();
			String errorMessage = exception.getDefaultMessage();
			exceptionMap.put(errorField, errorMessage);
		});

		return new ResponseEntity<>(exceptionMap, HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<?> ioExceptionhandler(IOException ioException){
		return new ResponseEntity<>(ioException.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> runtimeExceptionHandler(RuntimeException ex) {
	    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}
