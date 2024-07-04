package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dao.EducationDetailsDao;
import com.example.entity.EducationDetails;

@Service
public class EducationDetailsService {
	
	@Autowired
	EducationDetailsDao dao;
	
	
	public EducationDetails findbyemepnumber(Long empnumber) {
		return dao.findByEmpnumber(empnumber);
	}

	
	public EducationDetails saveeducationdetails(EducationDetails education) {
		return dao.save(education);
	}
	
	public List<EducationDetails>allemployeeEducationalDetails(){
		return dao.findAll();
	}
}
