package com.example.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public class WorkFromHome {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private long empnumber;
	
	private LocalDate startdate;
	private LocalDate enddate;
	private String reason;
	private long nodays;
	private String status;
	
}
