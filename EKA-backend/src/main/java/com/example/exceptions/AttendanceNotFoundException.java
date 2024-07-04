package com.example.exceptions;

public class AttendanceNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AttendanceNotFoundException(String message) {
        super(message);
    }
}