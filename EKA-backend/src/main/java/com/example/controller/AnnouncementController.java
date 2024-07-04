package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CommonApiResponse;
import com.example.dto.ResponseDto;
import com.example.entity.Announcement;
import com.example.resource.AnnouncementResource;
 

@RestController
@RequestMapping("/announcments")
public class AnnouncementController {

	@Autowired
	AnnouncementResource resource;
	
	@PostMapping(value = "/addannouncement")
	public ResponseEntity<CommonApiResponse>addannouncement( @RequestBody Announcement announce){
		return resource.addannouncement(announce);
	}
	
	@GetMapping(value = "/findallannouncements")
	public ResponseEntity<ResponseDto>fetchallannouncements(){
		return resource.fetchallannouncements();
	}
}
