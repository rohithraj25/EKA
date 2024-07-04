package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.LeaveRequest;
import java.util.List;


@Repository
public interface LeaveRequestDao extends JpaRepository<LeaveRequest, Integer> {
List<LeaveRequest> findByStatus(String status);

 LeaveRequest findByEmpnumberAndStatus(Long empnumber, String status);
List<LeaveRequest> findByEmpnumber(Long empnumber);
}
