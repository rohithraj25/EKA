package com.example.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.CommonApiResponse;
import com.example.dto.UserResponseDto;
import com.example.entity.EducationDetails;
import com.example.entity.User;
import com.example.service.EducationDetailsService;
import com.example.service.UserService;

@Service
public class EducationalDetailsResource {
	
	@Autowired
	UserService service;
	
	@Autowired
	EducationDetailsService educationService;

	  public ResponseEntity<CommonApiResponse>saveeducationaldetails(EducationDetails education){
	    	CommonApiResponse response = new CommonApiResponse();
	    	
	    	
	    	
	    	if(education==null) {
				response.setMessage("missing input");
				response.setStatus(false);
				return new ResponseEntity<CommonApiResponse>(response,HttpStatus.BAD_REQUEST);
			}
	    	User byEmpNumber = service.findByEmpNumber(education.getEmpnumber());
	    	if(byEmpNumber==null) {
	    		response.setMessage("provide proper employee number");
	    		response.setStatus(false);
	    		return new ResponseEntity<CommonApiResponse>(response,HttpStatus.BAD_REQUEST);
	    	}
	    	EducationDetails saveeducationdetails = educationService.saveeducationdetails(education);
	    	
	    	
	    	byEmpNumber.setEducation(saveeducationdetails);
	    	User savedata = service.savedata(byEmpNumber);
	    	if(savedata==null) {
	    		response.setMessage("failed to add education details");
	    		response.setStatus(false);
	    		return new ResponseEntity<CommonApiResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	    	}
	    	response.setMessage(" add education details");
			response.setStatus(true);
			return new ResponseEntity<CommonApiResponse>(response,HttpStatus.OK);
	    	
	    }
	
	  public ResponseEntity<UserResponseDto>findllemployeEducationaldetails(){
		  UserResponseDto response = new UserResponseDto();
		  
		  List<EducationDetails> educationalDetails = educationService.allemployeeEducationalDetails();
		  
		  
		  if(educationalDetails.isEmpty()) {
			  response.setMessage("no data found");
			  response.setStatus(false);
			  return new ResponseEntity<UserResponseDto>(response,HttpStatus.BAD_REQUEST);
		  }
		  response.setListEducationDetails(educationalDetails);
		  response.setMessage(" data found");
		  response.setStatus(true);
		  return new ResponseEntity<UserResponseDto>(response,HttpStatus.OK);
	  }
	  
}
