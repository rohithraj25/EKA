package com.example.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.CommonApiResponse;
import com.example.dto.ResponseDto;
import com.example.entity.Announcement;
import com.example.entity.User;
import com.example.service.AnnouncementService;
import com.example.service.UserService;

 
@Service
public class AnnouncementResource {

	@Autowired
	AnnouncementService service;
	
	@Autowired
	UserService userservice;
	
	public ResponseEntity<CommonApiResponse>addannouncement(Announcement announce){
		
		CommonApiResponse response = new CommonApiResponse();
		
		User user = userservice.findbyempNumber(announce.getEmpnumber());
		
		if(user==null) {
			response.setMessage("your are not authorised person using this api");
			response.setStatus(false);
			return new ResponseEntity<CommonApiResponse>(response,HttpStatus.BAD_REQUEST);
		}
		announce.setUser(user);
		Announcement addannouncemnt = service.addannouncemnt(announce);
		if(addannouncemnt==null) {
			response.setMessage("failed to add announcement");
			response.setStatus(false);
			return new ResponseEntity<CommonApiResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.setMessage("successfully added announcement");
		response.setStatus(true);
		return new ResponseEntity<CommonApiResponse>(response,HttpStatus.OK);
		
	}
	
	public ResponseEntity<ResponseDto>fetchallannouncements(){
		
		ResponseDto dto = new ResponseDto();
		
		List<Announcement> findall = service.findall();
		if(findall.isEmpty()) {
			
			dto.setMessage("no data found");
			dto.setStatus(false);
			return new ResponseEntity<ResponseDto>(dto,HttpStatus.BAD_REQUEST);
		}
		dto.setMessage(" data found");
		dto.setListannounce(findall);
		dto.setStatus(true);
		return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);
	}
}
