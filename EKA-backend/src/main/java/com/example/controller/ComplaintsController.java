package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CommonApiResponse;
import com.example.entity.Complaints;
import com.example.resource.ComplaintsResource;

 
@RestController
@RequestMapping( "/complaints")
public class ComplaintsController {

	@Autowired
	ComplaintsResource resource;
	
	@PostMapping(value = "/addcomplaint")
	public ResponseEntity<CommonApiResponse>addcomplaint(@RequestBody Complaints com){
		return resource.addComplaints(com);
	}
	
	@GetMapping(value = "/findbyempnumber")
	public ResponseEntity<CommonApiResponse>findcompliantsByempnumber(@RequestParam ("empnumber") long empnumber){
		return resource.findcomplaintsByempnumber(empnumber);
	} 
	
	@GetMapping(value = "/allcomplaints")
	public ResponseEntity<CommonApiResponse>allcomplaints(){
		return resource.findallcomplaints();
	}
}
