package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.LeaveRequestDao;
import com.example.entity.LeaveRequest;

@Service
public class LeaveRequestService {

	@Autowired
    private LeaveRequestDao leaveRequestDao;

    public boolean applyLeave(LeaveRequest leaveRequest) {
        LeaveRequest savedLeaveRequest = leaveRequestDao.save(leaveRequest);
        return savedLeaveRequest != null;
    }

   public LeaveRequest findByEmpnumberAndStatus(Long empnumber, String status){
	   return leaveRequestDao.findByEmpnumberAndStatus(empnumber,status);
   }

    public void approveLeave(LeaveRequest leaveRequest) {
        leaveRequest.setStatus("Approved");
        leaveRequestDao.save(leaveRequest);
    }

    public void rejectLeave(LeaveRequest leaveRequest) {
        leaveRequest.setStatus("Rejected");
        leaveRequestDao.save(leaveRequest);
    }
    
    public List<LeaveRequest> findbyleaverequests( ){
    	String status="pending";
    	return leaveRequestDao.findByStatus(status);
    }
    
    public List<LeaveRequest> findByEmpnumber(Long empnumber){
    	return leaveRequestDao.findByEmpnumber(empnumber);
    }
}
