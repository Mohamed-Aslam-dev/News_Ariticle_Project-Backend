package com.Ilayangudi_news.exceptions;

public class ResourcesNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L; // ✅ add this

	public ResourcesNotFoundException(){
		super();
	}
	
	public ResourcesNotFoundException(String message) {
        super(message);
    }
	
}

