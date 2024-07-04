package com.example.dto;

import org.springframework.beans.BeanUtils;

import com.example.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class RequestUserDto {
	private String firstname;
	private String middlename;
	private String lastname;
	private String gender;
	private String dateofbirth;
	
	private String email;
	private Long mobileno;
	private String workertype;
	private String timetype;
	private String joiningdate;
	private String jobtitle;
	private String reportingmanager;
	private String department;
	private String location;
	private String noticeperiod;
	
	
	private String password;

	 private Long empNumber;
	 
	
	
	public static User toUserEntity(RequestUserDto registerUserRequestDto) {
		User user = new User();
		BeanUtils.copyProperties(registerUserRequestDto, user);
		return user;
	}
}
