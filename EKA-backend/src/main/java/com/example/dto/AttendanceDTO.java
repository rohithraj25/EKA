package com.example.dto;

import java.time.LocalDateTime;

import com.example.entity.Attendance;

import lombok.Data;

@Data
public class AttendanceDTO {
    private Long id;
    private Long userId;
    private UserDTO user;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private String status;
    private String loginIpAddress;
    private String loginLocation;
    private String logoutIpAddress;
    private String logoutLocation;
 
    public AttendanceDTO() {}

    public AttendanceDTO(Attendance attendance) {
        this.id = attendance.getId();
        this.status = attendance.getStatus();
        this.loginIpAddress = attendance.getLoginIpAddress();
        this.loginLocation = attendance.getLoginLocation();
        this.logoutIpAddress = attendance.getLogoutIpAddress();
        this.logoutLocation = attendance.getLogoutLocation();
        this.loginTime = attendance.getLoginTime();
        this.logoutTime = attendance.getLogoutTime();
         
        // Assuming you have a method to convert User to UserDTO
        if (attendance.getUser() != null) {
            this.user = new UserDTO(attendance.getUser());
            this.userId = attendance.getUser().getId();
        }
    }
}
