package com.example.entity;



import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class LeaveRequest {

    @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "Userid", referencedColumnName = "id")  // Ensure the case matches your column names
    private User user;
    private String employeeName;
    private Long empnumber;
    private String employeeEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
	public String  leaveType;

}
