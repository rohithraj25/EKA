package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public class EducationDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	private Long empnumber;

private String graduationcollegeOrInstitute;
private String graduationUniversityOrBord;
private int graduationpassedOutYear;
private double graduationMarksOrCGPA;
private String graduationBranch;
private String graduationCity;


private String intercollegeOrInstitute;
private String interUniversityOrBord;
private int interpassedOutYear;
private double intermarksOrCGPA;
private String interBranch;
private String interCity;

private String ssccollegeOrInstitute;
private String sscuniversityOrBord;
private int sscpassedOutYear;
private double sscmarksOrCGPA;
private String sscCity;



}
