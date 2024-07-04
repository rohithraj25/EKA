package com.example.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.UserDao;
import com.example.dto.UserLoginRequest;
import com.example.entity.User;




@Service
public class UserService {
	
	@Autowired
UserDao repo;
	
	public User savedata(User us) {
		return repo.save(us);
		
	}
	
	public User logind(UserLoginRequest login) {
		return repo.findByEmailAndPassword(login.getEmailId(), login.getPassword());
	}
	
	public User getbyemail(String email) {
		return repo.findByEmail(email);
	}
	
	
	public List<User> findall() {
		return repo.findAll();
	}
	
	public boolean grantRole(String email) {
		String role="Special";
        User user = repo.findByEmail(email);
        if (user != null) {
            user.setRole(role);
            repo.save(user);
            return true;
        }
        return false;
    }

    public boolean revokeRole(String email) {
    	String role="HR";
        User user = repo.findByEmail(email);
        if (user != null) {
            user.setRole(role);;
            repo.save(user);
            return true;
        }
        return false;
    }
    
    ///
    public List<User> findByRole(String role){
    	return repo.findByRole(role);
    }
    
	/*
	 * public List<User> findByPosition(String Desidnation){ return
	 * repo.findByDesignation(Desidnation); }
	 */
    
    public User findByEmpNumber(Long empNumber) {
    	return repo.findByEmpNumber(empNumber);
    }
    
    public User findByEmailAndRole(String email, String role) {
    	return repo.findByEmailAndRole(email, role);
    }
    
    public List<User>findallhrs(){
    	String role="HR";
    	return repo.findByRole(role);
    }

    public User findbyempNumber(long empnumber) {
		return repo.findByEmpNumber(empnumber);
	}
    public List<User> findUsersWithBirthdaysBetween(LocalDate startDate, LocalDate endDate) {
        List<User> usersWithUpcomingBirthdays;

        int startMonth = startDate.getMonthValue();
        int startDay = startDate.getDayOfMonth();
        int endMonth = endDate.getMonthValue();
        int endDay = endDate.getDayOfMonth();

        if (startMonth == endMonth) {
            usersWithUpcomingBirthdays = repo.findUsersWithBirthdaysInMonthAndDayRange(startMonth, startDay, endDay);
        } else {
            usersWithUpcomingBirthdays = repo.findUsersWithBirthdaysInMonthAndDayRangeAcrossMonths(startMonth, startDay, endMonth, endDay);
        }

        return usersWithUpcomingBirthdays;
    }
	
	 public List<User> findUsersWithJoiningDatesBetween(LocalDate startDate, LocalDate endDate) {
	        return repo.findUsersWithJoiningDatesBetween(startDate, endDate);
	    }
}
