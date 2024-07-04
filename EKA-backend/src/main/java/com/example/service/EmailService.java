package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.dao.UserDao;
import com.example.entity.LeaveRequest;
import com.example.entity.User;

@Service
public class EmailService {
	 @Autowired
	    private JavaMailSender mailSender;

	    @Autowired
	    private UserService userService;
	    
	    @Autowired
	    UserDao dao;

	    public void sendEmailApproved(String employeeEmail, String subject, String text) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(employeeEmail);
	        message.setSubject(subject);
	        message.setText(text);
	        mailSender.send(message);
	    }

	    public void sendEmailRejected(String employeeEmail, String subject, String text) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(employeeEmail);
	        message.setSubject(subject);
	        message.setText(text);
	        mailSender.send(message);
	    }

	    public void sendEmailForLeave(String employeeEmail, String subject, String text) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(employeeEmail);
	        message.setSubject(subject);
	        message.setText(text);
	        mailSender.send(message);
	    }

	    public void notifyHrsAboutLeave(LeaveRequest leaveRequest) {
	        List<User> allHrs = userService.findallhrs();
	        String[] hrEmails = allHrs.stream().map(User::getEmail).toArray(String[]::new);
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(hrEmails);
	        message.setSubject("New Leave Request");
	        message.setText("Employee " + leaveRequest.getEmployeeName() + " has applied for leave.");
	        mailSender.send(message);
	    }

	    public void sendEmail(String to, String subject, String text) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(text);
	        mailSender.send(message);
	    }

	    public void sendRequestNotification(String employeeEmail) {
	        // Send notification to employee
	        String employeeSubject = "Work From Home Request Submitted";
	        String employeeText = "Your work from home request has been submitted and is pending approval.";
	        sendEmail(employeeEmail, employeeSubject, employeeText);

	        // Retrieve admin email from the database
	        List<User> adminList = dao.findByRole("Admin");

	        // If admin exists, send notification to admin
	        if (adminList != null && !adminList.isEmpty()) {
	            User admin = adminList.get(0); // Get the first admin from the list
	            String adminEmail = admin.getEmail();
	            String adminSubject = "New Work From Home Request";
	            String adminText = "A new work from home request has been submitted and is pending your approval.";
	            sendEmail(adminEmail, adminSubject, adminText);
	        }
	    }


	    public void sendApprovalNotificationToEmployee(String employeeEmail) {
	        String subject = "Work From Home Request Approved";
	        String text = "Your work from home request has been approved.";
	        sendEmail(employeeEmail, subject, text);
	    }

	    public void sendRejectionNotificationToEmployee(String employeeEmail) {
	        String subject = "Work From Home Request Rejected";
	        String text = "Your work from home request has been rejected.";
	        sendEmail(employeeEmail, subject, text);
	    }
	    }
