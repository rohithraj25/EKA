// service/impl/AttendanceServiceImpl.java
package com.example.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.dao.AttendanceRepository;
import com.example.dao.UserDao;
import com.example.dto.AttendanceDTO;
import com.example.dto.UserDTO;
import com.example.entity.Attendance;
import com.example.entity.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AttendanceService  {

    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private UserDao userRepository;
    
    @Autowired
    private EmailService emailService;
 
    @Autowired
    private JavaMailSender mailSender;
    private final Map<Long, AttendanceDTO> attendanceMap = new ConcurrentHashMap<>();


    public AttendanceDTO login(Long userId, String ipAddress, String location) {
        // Fetch the user from the repository
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the user already has an active session
        Optional<Attendance> activeSession = attendanceRepository.findFirstByUserIdAndStatusOrderByLoginTimeDesc(userId, "LOGIN");
        if (activeSession.isPresent() && activeSession.get().getLogoutTime() == null) {
            throw new RuntimeException("User already logged in. Please log out before logging in again.");
        }

        // Create a new attendance record for login
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setStatus("LOGIN");
        attendance.setLoginTime(LocalDateTime.now());
        attendance.setLoginIpAddress(ipAddress);
        attendance.setLoginLocation(location);

        // Save the attendance record
        attendanceRepository.save(attendance);

        // Check if login time is after 10am
        if (attendance.getLoginTime().toLocalTime().isAfter(LocalTime.of(10, 0))) {
            sendNotification(user.getEmail(), "Late Login Notification", "You have logged in late today.");
        }

        // Return the DTO
        return new AttendanceDTO(attendance);
    }

    public AttendanceDTO logout(Long userId, String ipAddress, String location) {
        // Fetch the last attendance record for the user
        Attendance attendance = attendanceRepository.findFirstByUserIdOrderByLoginTimeDesc(userId)
                .orElseThrow(() -> new RuntimeException("No attendance record found for the user"));

        // Update the attendance record with logout information
        attendance.setLogoutTime(LocalDateTime.now());
        attendance.setStatus("LOGOUT");
        attendance.setLogoutIpAddress(ipAddress);
        attendance.setLogoutLocation(location);

        long totalWorkingMinutes = Duration.between(attendance.getLoginTime(), attendance.getLogoutTime()).toMinutes();
        double totalWorkingHours = totalWorkingMinutes / 60.0;

        // Save the updated attendance record
        attendanceRepository.save(attendance);

        if (totalWorkingHours != 9) {
            sendNotification(attendance.getUser().getEmail(), "Working Hours Notification", 
                            "Your total working hours . It is: " + totalWorkingHours + " hours.");
        }
        return new AttendanceDTO(attendance);
    }

    private void sendNotification(String to, String subject, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

     
    public List<AttendanceDTO> getAttendanceForUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        List<Attendance> attendances = attendanceRepository.findByUserAndLoginTimeBetween(user.get(), LocalDateTime.now().minusDays(30), LocalDateTime.now());
        return attendances.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private AttendanceDTO convertToDTO(Attendance attendance) {
        if (attendance == null) {
            return null;
        }

        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setStatus(attendance.getStatus());
        dto.setLoginIpAddress(attendance.getLoginIpAddress());
        dto.setLoginLocation(attendance.getLoginLocation());
        dto.setLogoutIpAddress(attendance.getLogoutIpAddress());
        dto.setLogoutLocation(attendance.getLogoutLocation());
        dto.setLoginTime(attendance.getLoginTime());
        dto.setLogoutTime(attendance.getLogoutTime());

        User user = attendance.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setUser(new UserDTO(user));  // Use the updated UserDTO constructor
        } else {
            dto.setId(null);
            dto.setUser(null);
        }

        return dto;
    }


    
     
    public AttendanceDTO getById(Long id) {
        Optional<Attendance> attendance = attendanceRepository.findById(id);
        return attendance.map(this::convertToDTO).orElse(null);
    }

     
    public List<AttendanceDTO> getAll() {
        List<Attendance> attendances = attendanceRepository.findAll();
        return attendances.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
