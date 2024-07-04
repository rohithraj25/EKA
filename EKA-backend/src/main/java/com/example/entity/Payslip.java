package com.example.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String uan;
    private String pan;
    private String designation;
    private String department;
    private String bankName;
    private String bankAccountNumber;
    private LocalDate joiningDate;
    private double basicSalary;
    private double hra;
    private double conveyanceAllowance;
    private double medicalAllowance;
    private double otherAllowance;
    private double healthInsurance;
    private double professionalTax;

 
}