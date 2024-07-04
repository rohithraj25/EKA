package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Complaints;

@Repository
public interface ComplaintsDao extends JpaRepository<Complaints, Integer> {
	List<Complaints> findByEmpnumber(long empnumber);

}
