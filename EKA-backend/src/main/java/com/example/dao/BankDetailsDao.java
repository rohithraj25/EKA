package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.BankDetails;
import java.util.List;


@Repository
public interface BankDetailsDao extends JpaRepository<BankDetails, Integer> {

	List<BankDetails> findByEmpnumber(Long empnumber);
}
