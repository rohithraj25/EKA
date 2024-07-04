package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ApplyLeaveRequestDto;
import com.example.dto.CommonApiResponse;
import com.example.dto.UserResponseDto;

import com.example.resource.LeaveRequestResource;

@RestController
@RequestMapping("/leaveRequests")
@CrossOrigin("http://localhost:5173")
public class LeaveRequestController {

	@Autowired
	LeaveRequestResource resource;
	
	//leave apply api
	@PostMapping(value = "/applyLeave")
	public ResponseEntity<CommonApiResponse>applyleave(@RequestBody List<ApplyLeaveRequestDto>leave){
		return resource.applyLeave(leave);
	}
	
	@PutMapping(value = "/approveLeave")
	public ResponseEntity<CommonApiResponse>approveleave(@RequestParam ("empnumber") Long empnumber){
		return resource.approveLeave(empnumber);
	}
	
	//leave reject api
	@PutMapping(value = "/rejectLeave")
	public ResponseEntity<CommonApiResponse>rejectleave(@RequestParam ("empnumber") Long empnumber){
		return resource.rejectLeave(empnumber);
	}
	//find pending leaves api
	@GetMapping(value = "/findpendingleaves")
	public ResponseEntity<UserResponseDto>findleavequestspending(){
		return resource.findallpendingleaves();
	}
	
	//api for find employee apply leaves history
	@GetMapping(value = "/findEmpleaves")
	public ResponseEntity<UserResponseDto>findempleavesrequests(@RequestParam ("empnumber") Long empnumber){
		return resource.findempleavesrequests(empnumber);
	}
}
