package com.example.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.entity.BankDetails;
import com.example.entity.EducationDetails;
import com.example.entity.LeaveBalance;
import com.example.entity.LeaveRequest;
import com.example.entity.User;

import lombok.Data;

@Data
public class UserResponseDto extends CommonApiResponse {

	private List<User>user=new ArrayList<>();
	private User singleuser;
	
	private List<LeaveBalance>leaveBalance=new ArrayList<>();
	private List<LeaveRequest>leaverequest=new ArrayList<>();
	
	
	private List<BankDetails>listBankdetails=new ArrayList<>();
	
	private BankDetails bankdetails;
	
	private List<EducationDetails>listEducationDetails=new ArrayList<>();
}
