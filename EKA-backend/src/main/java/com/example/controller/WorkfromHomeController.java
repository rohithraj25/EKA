package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CommonApiResponse;
import com.example.entity.WorkFromHome;
import com.example.resource.WorkFromHomeResource;

 
@RestController
@RequestMapping("/workfromhome")
public class WorkfromHomeController {
	
	@Autowired
	WorkFromHomeResource resource;
	
	@PostMapping(value = "/request")
	public ResponseEntity<CommonApiResponse>addrequest(@RequestBody WorkFromHome work){
		return resource.addWorkFromHomeRequest(work);
	}
	
	@PutMapping(value = "/approve")
	public ResponseEntity<CommonApiResponse>approverequest(@RequestParam ("empnumber") long empnumber){
		return resource.approveWorkFromHomeRequest(empnumber);
	}
	
	@PutMapping(value = "/reject")
	public ResponseEntity<CommonApiResponse>rejectrequest(@RequestParam ("empnumber") long empnumber){
		return resource.rejectWorkFromHomeRequest(empnumber);
	}
	
	@GetMapping(value = "/findbyempnumber")
	 public ResponseEntity<CommonApiResponse> findByempnumber( @RequestParam ("empnumber") long emmnumber) {
		 return resource.findByempnumber(emmnumber);
	 }
	
	@GetMapping(value = "/allpendingRequests")
	 public ResponseEntity<CommonApiResponse>findallPendingRequests(){
		 return resource.findPendingRequests();
	 }
	
}
