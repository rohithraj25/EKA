package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.WorkFromHomeDao;
import com.example.entity.WorkFromHome;

 
@Service
public class WorkFromHomeService {
	
	@Autowired
	WorkFromHomeDao dao;
	
	public WorkFromHome addrequest(WorkFromHome work) {
		return dao.save(work);
	}

	public List<WorkFromHome>findallpendingrequests(long empnumber){
		String status="Pending";
		return dao.findByEmpnumberAndStatus(empnumber, status);
	}
	
	public List<WorkFromHome> findByEmpnumber(long empnumber){
		return dao.findByEmpnumber(empnumber);
	}
	
	public List<WorkFromHome> findByStatus(){
		String status="Pending";
		return dao.findByStatus(status);
	}
}
