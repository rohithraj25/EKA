package com.example.dto;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import com.example.entity.LeaveRequest;
import com.example.entity.User;

import lombok.Data;

@Data
public class ApplyLeaveRequestDto {

    private String employeeName;
    private Long empnumber;
    private String employeeEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String leaveType;

    public static LeaveRequest toLeaveRequestEntity(ApplyLeaveRequestDto leaveRequestDto, User user) {
        LeaveRequest leaveRequest = new LeaveRequest();
        BeanUtils.copyProperties(leaveRequestDto, leaveRequest);
        leaveRequest.setUser(user);
        return leaveRequest;
    }
}
