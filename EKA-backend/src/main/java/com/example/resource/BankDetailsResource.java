package com.example.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.CommonApiResponse;
import com.example.dto.UserResponseDto;
import com.example.entity.BankDetails;
import com.example.entity.User;
import com.example.service.BankDetailsService;
import com.example.service.UserService;

@Service
public class BankDetailsResource {
	
	
	@Autowired
	UserService userservice;
	
	@Autowired
	BankDetailsService bankservice;
	
	
	public ResponseEntity<CommonApiResponse>addbankdetails(BankDetails bank){
		CommonApiResponse response = new CommonApiResponse();
		
		if(bank==null) {
			response.setMessage("missing input");
			response.setStatus(false);
			return new ResponseEntity<CommonApiResponse>(response,HttpStatus.BAD_REQUEST);
		}
		
		User byEmpNumber = userservice.findByEmpNumber(bank.getEmpnumber());
		
		if(byEmpNumber==null) {
			response.setMessage("wrong employee number");
			response.setStatus(false);
			return new ResponseEntity<CommonApiResponse>(response,HttpStatus.BAD_REQUEST);
		}
		
		BankDetails savebankdetails = bankservice.savebankdetails(bank);
		byEmpNumber.setBank(savebankdetails);
		User savedata = userservice.savedata(byEmpNumber);
		
		if(savedata==null) {
			response.setMessage("failed to add bank details");
			response.setStatus(false);
			return new ResponseEntity<CommonApiResponse>(response,HttpStatus.BAD_REQUEST);
		}
		response.setMessage("successfully added employee bank details");
		response.setStatus(true);
		return new ResponseEntity<CommonApiResponse>(response,HttpStatus.OK);
		
	}
	
	
	public ResponseEntity<UserResponseDto>findallemployessbankdetails(){
		
		UserResponseDto response = new UserResponseDto();
		
		List<BankDetails> findallbankdetals = bankservice.findallbankdetals();
		
		if(findallbankdetals.isEmpty()) {
			response.setMessage("no data found on bank details");
			response.setStatus(false);
			return new ResponseEntity<UserResponseDto>(response,HttpStatus.BAD_REQUEST);
		}
		response.setListBankdetails(findallbankdetals);
		response.setMessage(" data found on bank details");
		response.setStatus(true);
		return new ResponseEntity<UserResponseDto>(response,HttpStatus.OK);
	}

}
