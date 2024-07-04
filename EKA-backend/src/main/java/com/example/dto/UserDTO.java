package com.example.dto;

 


import com.example.entity.BankDetails;
import com.example.entity.EducationDetails;
import com.example.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

 @Data
public class UserDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
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
	
	@JsonIgnore
	private String password;
	private String role;
	 private Long empNumber;
	 
	 private String status;
	 
	 //private String Designation;
	 
	 @OneToOne
	    @JoinColumn(name = "education_id") // optional, specifies the foreign key column
	    private EducationDetails education;
	 
	 @OneToOne
	 @JoinColumn(name="bank_id")
	 private BankDetails bank;
	 
	 public UserDTO() {}

	    public UserDTO(User user) {
	        this.id = user.getId();
	        this.firstname = user.getFirstname();
	        this.email = user.getEmail();
	        this.role = user.getRole();
	        this.mobileno = user.getMobileno();
	        this.empNumber = user.getEmpNumber();
	        this.status = user.getStatus();
	        this.department = user.getDepartment();
	    }
}
