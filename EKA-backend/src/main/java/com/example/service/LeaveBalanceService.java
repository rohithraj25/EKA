package com.example.service;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.LeaveBalanceDao;
import com.example.entity.LeaveBalance;
import com.example.entity.LeaveRequest;

@Service
public class LeaveBalanceService {
	
	@Autowired
	LeaveBalanceDao dao;

	public LeaveBalance addleave(LeaveBalance leave) {
		return dao.save(leave);
	}
	
	public List<LeaveBalance> findByEmpnumber(Long empnumber){
		return dao.findByEmpnumber(empnumber);
	}
	
	public void updateLeaveBalance(LeaveRequest leaveRequest) {
        LeaveBalance leaveBalance = dao.findByEmpnumberAndLeaveType(leaveRequest.getEmpnumber(), leaveRequest.getLeaveType());
        if (leaveBalance != null && leaveBalance.getAvailableDays() >= calculateLeaveDays(leaveRequest)) {
            leaveBalance.setAvailableDays(leaveBalance.getAvailableDays() - calculateLeaveDays(leaveRequest));
            dao.save(leaveBalance);
        }
    }

    private int calculateLeaveDays(LeaveRequest leaveRequest) {
        return (int) ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;
    }
    
    public LeaveBalance findByEmpnumberAndLeaveType(Long empnumber,String leavetype) {
    	return dao.findByEmpnumberAndLeaveType(empnumber, leavetype);
    }
    
    
    public void updateLeaveBalance(Long empnumber, String leaveType, int days) {
        LeaveBalance leaveBalance = dao.findByEmpnumberAndLeaveType(empnumber, leaveType);
        if (leaveBalance != null) {
            leaveBalance.setAvailableDays(leaveBalance.getAvailableDays() + days);
            dao.save(leaveBalance);
        }
    }
    
}
