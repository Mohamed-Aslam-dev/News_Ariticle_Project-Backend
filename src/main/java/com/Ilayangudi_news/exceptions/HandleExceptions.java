package com.Ilayangudi_news.exceptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class HandleExceptions {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException validException) {

	    List<String> errorMessages = new ArrayList<>();

	    validException.getBindingResult().getAllErrors().forEach((error) -> {
	        errorMessages.add(error.getDefaultMessage()); // Only message
	    });

	    return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<?> ioExceptionhandler(IOException ioException){
		return new ResponseEntity<>(ioException.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> runtimeExceptionHandler(RuntimeException ex) {
	    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)  // 413
                .body("கோப்பின் அளவு மிக அதிகம்! அதிகபட்சமாக அனுமதிக்கப்பட்ட அளவு 50MB தான்.");
    }
	

}
