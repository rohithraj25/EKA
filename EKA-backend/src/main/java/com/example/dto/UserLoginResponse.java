package com.example.dto;



import com.example.entity.User;

import lombok.Data;

@Data
public class UserLoginResponse extends CommonApiResponse {

	private User user;

	private String jwtToken;

	
}
