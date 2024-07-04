package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.BankDetailsDao;
import com.example.entity.BankDetails;

@Service
public class BankDetailsService {
	
	@Autowired
	BankDetailsDao dao;
	
	public BankDetails savebankdetails(BankDetails bank) {
		return dao.save(bank);
	}
	
	public List<BankDetails>findallbankdetals(){
		return dao.findAll();
	}

}
