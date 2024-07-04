// controller/AttendanceController.java
package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.NetworkUtils;
import com.example.dto.AttendanceDTO;
import com.example.service.AttendanceService;
import com.example.service.LocationService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private LocationService locationService;

    @PostMapping("/clockin")
    public ResponseEntity<AttendanceDTO> login(@RequestParam Long userId) {
        String ipAddress = NetworkUtils.getLocalIpAddress(); // Assume this is a utility method to get local IP
        Map<String, Object> locationData = locationService.getLocationForSingleIp(ipAddress);
        String location = locationData != null ? locationData.get("city") + ", " + locationData.get("region") : "Unknown Location";

        AttendanceDTO attendanceDTO = attendanceService.login(userId, ipAddress, location);
        return ResponseEntity.ok(attendanceDTO);
    }

    @PostMapping("/clockout")
    public ResponseEntity<AttendanceDTO> logout(@RequestParam Long userId) {
        String ipAddress = NetworkUtils.getLocalIpAddress(); // Assume this is a utility method to get local IP
        Map<String, Object> locationData = locationService.getLocationForSingleIp(ipAddress);
        String location = locationData != null ? locationData.get("city") + ", " + locationData.get("region") : "Unknown Location";

        AttendanceDTO attendanceDTO = attendanceService.logout(userId, ipAddress, location);
        return ResponseEntity.ok(attendanceDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendances() {
        List<AttendanceDTO> attendanceList = attendanceService.getAll();
        return ResponseEntity.ok(attendanceList);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<AttendanceDTO> getById(@PathVariable Long id) {
        AttendanceDTO attendanceDTO = attendanceService.getById(id);
        if (attendanceDTO != null) {
            return ResponseEntity.ok(attendanceDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
