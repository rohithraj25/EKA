package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CommonApiResponse;
import com.example.dto.UserResponseDto;
import com.example.entity.EducationDetails;
import com.example.resource.EducationalDetailsResource;

@RestController
@RequestMapping("/educationaldetails")
@CrossOrigin("http://localhost:5173")
public class EducationalDetailsController {

	@Autowired
	EducationalDetailsResource resource;
	
	
	@PostMapping(value = "/saveeducationaldetails")
    public ResponseEntity<CommonApiResponse>addeducationaldetails(@RequestBody EducationDetails education){
    	return resource.saveeducationaldetails(education);
    }
	
	@GetMapping(value = "/getallEducationalDetails")
	public ResponseEntity<UserResponseDto>findalleducationaldetaails(){
		return resource.findllemployeEducationaldetails();
	}
}
