package com.example.dto;

import org.springframework.beans.BeanUtils;
import com.example.entity.LeaveBalance;
import com.example.entity.User;
import lombok.Data;

@Data
public class AddLeaveBalanceDto {
    private String employeeName;
    private Long empnumber;
    private String leaveType;
    private int availableDays;

    public static LeaveBalance toLeaveBalanceEntity(AddLeaveBalanceDto leaveBalanceDto, User user) {
        LeaveBalance leaveBalance = new LeaveBalance();
        BeanUtils.copyProperties(leaveBalanceDto, leaveBalance);
        leaveBalance.setUser(user);
        leaveBalance.setEmployeeName(user.getFirstname());
        leaveBalance.setEmpnumber(user.getEmpNumber());
        return leaveBalance;
    }
}
