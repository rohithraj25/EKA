package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.dto.CommonApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<CommonApiResponse> handleUserNotFoundException(UserNotFoundException ex) {
		String responseMessage = ex.getMessage();

		CommonApiResponse apiResponse = new CommonApiResponse();

		apiResponse.setMessage(responseMessage);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(UserSaveFailedException.class)
	public ResponseEntity<CommonApiResponse> handleUserRegistrationFailedException(UserSaveFailedException ex) {
		String responseMessage = ex.getMessage();

		CommonApiResponse apiResponse = new CommonApiResponse();

		apiResponse.setMessage(responseMessage);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	@ExceptionHandler(MissingInputException.class)
	public ResponseEntity<CommonApiResponse>handleMissinginputException(MissingInputException m){
		
		String message = m.getMessage();
		CommonApiResponse apiResponse = new CommonApiResponse();
		apiResponse.setMessage(message);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(BankDetailsAddFailedException.class)
	public ResponseEntity<CommonApiResponse>handlebankdetailfailedexceptions(BankDetailsAddFailedException ex){
		String message = ex.getMessage();
		CommonApiResponse apiResponse = new CommonApiResponse();

		apiResponse.setMessage(message);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(EducationDetailsFaileToAddException.class)
	public ResponseEntity<CommonApiResponse>handleeducationdetailsfailedException(EducationDetailsFaileToAddException ex){
		String message = ex.getMessage();
		CommonApiResponse apiResponse = new CommonApiResponse();

		apiResponse.setMessage(message);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	

	@ExceptionHandler(LeaveBalanceAddFailedException.class)
	public ResponseEntity<CommonApiResponse>handleleavebalnceException(LeaveBalanceAddFailedException ex){
		String message = ex.getMessage();
		CommonApiResponse apiResponse = new CommonApiResponse();

		apiResponse.setMessage(message);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler(DataNotFound.class)
	public ResponseEntity<CommonApiResponse>handleMissinginputException(DataNotFound m){
		
		String message = m.getMessage();
		CommonApiResponse apiResponse = new CommonApiResponse();
		apiResponse.setMessage(message);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(AnnouncementAddFailedException.class)
	public ResponseEntity<CommonApiResponse>handleMissinginputException(AnnouncementAddFailedException m){
		
		String message = m.getMessage();
		CommonApiResponse apiResponse = new CommonApiResponse();
		apiResponse.setMessage(message);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(UserAlreadyLoggedInException.class)
	public ResponseEntity<CommonApiResponse> handleUserNotFoundException(UserAlreadyLoggedInException ex) {
		String responseMessage = ex.getMessage();

		CommonApiResponse apiResponse = new CommonApiResponse();

		apiResponse.setMessage(responseMessage);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(AttendanceNotFoundException.class)
	public ResponseEntity<CommonApiResponse> handleAttendanceNotFoundException(AttendanceNotFoundException ex) {
		String responseMessage = ex.getMessage();

		CommonApiResponse apiResponse = new CommonApiResponse();

		apiResponse.setMessage(responseMessage);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler(ComplaintAddFailedException.class)
	public ResponseEntity<CommonApiResponse>handleMissinginputException(ComplaintAddFailedException m){
		
		String message = m.getMessage();
		CommonApiResponse apiResponse = new CommonApiResponse();
		apiResponse.setMessage(message);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		
	}
	
	
	@ExceptionHandler(WorkFromHomeRequestFailedException.class)
	public ResponseEntity<CommonApiResponse>WorkFromHomeRequestFailedException(WorkFromHomeRequestFailedException m){
		
		String message = m.getMessage();
		CommonApiResponse apiResponse = new CommonApiResponse();
		apiResponse.setMessage(message);
		apiResponse.setStatus(false);
		return new ResponseEntity<CommonApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		
	}
	
}
