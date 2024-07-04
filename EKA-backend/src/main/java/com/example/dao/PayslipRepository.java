package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Payslip;
import com.example.entity.User;

@Repository
public interface PayslipRepository extends JpaRepository< Payslip, Long> {
	 Payslip findByEmail(String email);

	void save(User payslip);
}
