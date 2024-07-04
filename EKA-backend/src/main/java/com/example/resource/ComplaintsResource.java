package com.example.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dao.UserDao;
import com.example.dto.CommonApiResponse;
import com.example.entity.Complaints;
import com.example.entity.User;
import com.example.service.ComplaintsService;
 @Service
public class ComplaintsResource {

	@Autowired
	ComplaintsService service;
	@Autowired
	UserDao dao;
	
	public ResponseEntity<CommonApiResponse> addComplaints( Complaints com) {
        CommonApiResponse response = new CommonApiResponse();

        User user = dao.findByEmpNumber(com.getEmpnumber());
        if (user == null) {
            response.setMessage("You're not an authorized person");
            response.setStatus(false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Complaints addComplaints = service.addcomplaints(com);
        if (addComplaints == null) {
            response.setMessage("Failed to send complaint");
            response.setStatus(false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.setMessage("Successfully sent complaint");
        response.setStatus(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	public ResponseEntity<CommonApiResponse>findcomplaintsByempnumber(long empnumber){
		CommonApiResponse response = new CommonApiResponse();
		
		List<Complaints> findbyempnumber = service.findbyempnumber(empnumber);
		
		if(findbyempnumber.isEmpty()) {
			response.setMessage("no complaints found");
			response.setStatus(false);
			return new ResponseEntity<CommonApiResponse>(response,HttpStatus.NOT_FOUND);
		}
		response.setListcomplaints(findbyempnumber);
		response.setMessage("data found");
		response.setStatus(true);
		return new ResponseEntity<CommonApiResponse>(response,HttpStatus.OK);
	}
	
	public ResponseEntity<CommonApiResponse>findallcomplaints(){
		
		CommonApiResponse response = new CommonApiResponse();
		
		List<Complaints> findallcomplaints = service.findallcomplaints();
		if(findallcomplaints.isEmpty()) {
			response.setMessage("no complaints found");
			response.setStatus(false);
			return new ResponseEntity<CommonApiResponse>(response,HttpStatus.NOT_FOUND);
		}
		
		response.setListcomplaints(findallcomplaints);
		response.setMessage("data found");
		response.setStatus(true);
		return new ResponseEntity<CommonApiResponse>(response,HttpStatus.OK);
	}
}
