package com.example.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String employeeName;
    @ManyToOne
    @JoinColumn(name = "Userid", referencedColumnName = "id")  // Ensure the case matches your column names
    @JsonIgnore
    private User user;
    private Long empnumber;
    private String  leaveType;
    private int availableDays;
    
}