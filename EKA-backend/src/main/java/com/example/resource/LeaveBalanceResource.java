package com.example.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.AddLeaveBalanceDto;
import com.example.dto.CommonApiResponse;
import com.example.dto.UserResponseDto;
import com.example.entity.LeaveBalance;
import com.example.entity.User;
import com.example.service.LeaveBalanceService;
import com.example.service.UserService;

@Service
public class LeaveBalanceResource {

	@Autowired
	LeaveBalanceService service;
	
	@Autowired
	UserService userservice;
	 public ResponseEntity<CommonApiResponse> addleave(List<AddLeaveBalanceDto> leaveList) {
	        CommonApiResponse response = new CommonApiResponse();

	        if(leaveList==null||leaveList.isEmpty()) {
				response.setMessage("missing input");
				response.setStatus(false);
				return new ResponseEntity<CommonApiResponse>(response,HttpStatus.BAD_REQUEST);
			}
	        
	        if (leaveList == null || leaveList.isEmpty()) {
	            response.setMessage("Leave list is empty");
	            response.setStatus(false);
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }

	        User byEmpNumber = userservice.findByEmpNumber(leaveList.get(0).getEmpnumber());

	        if (byEmpNumber == null) {
	            response.setMessage("Employee not found");
	            response.setStatus(false);
	            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	        }

	        for (AddLeaveBalanceDto leaveDto : leaveList) {
	            
	        	LeaveBalance leave = AddLeaveBalanceDto.toLeaveBalanceEntity(leaveDto, byEmpNumber);
	        	leave.setUser(byEmpNumber);
	        	service.addleave(leave);  // Save each leave balance
	        }

	        response.setMessage("Leave balances added successfully");
	        response.setStatus(true);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	
	 public ResponseEntity<UserResponseDto>checkavailabeleaves(Long empnumber){
		 UserResponseDto response = new UserResponseDto();
		 
		 if(empnumber==null) {
				response.setMessage("missing input");
				response.setStatus(false);
				return new ResponseEntity<UserResponseDto>(response,HttpStatus.BAD_REQUEST);
			}
		 
		 List<LeaveBalance> byEmpnumber = service.findByEmpnumber(empnumber);
		 
		 if(byEmpnumber.isEmpty()) {
			 response.setMessage("No data found or wrong emp number");
		        response.setStatus(false);
		        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		 }
		 response.setLeaveBalance(byEmpnumber);
		 response.setMessage("data found");
	        response.setStatus(true);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	 }
	 
}
