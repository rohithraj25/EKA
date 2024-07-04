package com.example.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.entity.Announcement;
import com.example.entity.User;

import lombok.Data;

@Data
public class ResponseDto extends CommonApiResponse {
	
	private List<Announcement>listannounce=new ArrayList<>();
	
	private List<User>listusers=new ArrayList<>();

}
