package com.example.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.User;



@Repository
public interface UserDao extends JpaRepository<User, Long>{
	User  findByEmailAndPassword(String email, String password);
	User findByEmail(String email);
 	List<User> findByRole(String role);

	//List<User> findByDesignation(String designation);
	User  findByEmpNumber(Long empNumber);
	
	User findByEmailAndRole(String email, String role);
	Optional<User> findById(Long userId);
	User findByEmpNumber(long empNumber);
	
	//birth
	@Query(value = "SELECT * FROM User u WHERE MONTH(u.dob) = :month AND DAY(u.dob) BETWEEN :startDay AND :endDay", nativeQuery = true)
    List<User> findUsersWithBirthdaysInMonthAndDayRange(@Param("month") int month, @Param("startDay") int startDay, @Param("endDay") int endDay);

    @Query(value = "SELECT * FROM User u WHERE (MONTH(u.dob) = :startMonth AND DAY(u.dob) >= :startDay) OR (MONTH(u.dob) = :endMonth AND DAY(u.dob) <= :endDay)", nativeQuery = true)
    List<User> findUsersWithBirthdaysInMonthAndDayRangeAcrossMonths(@Param("startMonth") int startMonth, @Param("startDay") int startDay, @Param("endMonth") int endMonth, @Param("endDay") int endDay);
	
		 @Query("SELECT u FROM User u WHERE u.dateofjoining BETWEEN :startDate AND :endDate")
	List<User> findUsersWithJoiningDatesBetween(LocalDate startDate, LocalDate endDate);
 }
