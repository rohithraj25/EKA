package com.example.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
@Service
public class OtpService {

    private static final Logger logger = Logger.getLogger(OtpService.class.getName());

    @Autowired
    private JavaMailSender mailSender;

    private Map<String, String> otpStore = new HashMap<>();

    public void generateAndSendOtp(String email) throws MessagingException {
        String otp = generateOtp();
        otpStore.put(email, otp);
        try {
            sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "Failed to send OTP email to " + email, e);
            throw e;
        }
    }

    public boolean validateOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void sendOtpEmail(String to, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Your OTP Code");
        helper.setText("Your OTP code is: " + otp);
        mailSender.send(message);
    }
}