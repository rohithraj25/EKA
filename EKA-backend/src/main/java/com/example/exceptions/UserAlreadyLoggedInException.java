package com.example.exceptions;

public class UserAlreadyLoggedInException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyLoggedInException(String message) {
        super(message);
    }
}
