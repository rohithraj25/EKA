package com.example.dao;

 import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Attendance;
import com.example.entity.User;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUserId(Long userId);

	List<Attendance> findByUserAndLoginTimeBetween(User user, LocalDateTime minusDays, LocalDateTime now);

	Optional<User> findByUserIdAndStatus(Long userId, String string);

    Optional<Attendance> findFirstByUserIdOrderByLoginTimeDesc(Long userId);

	Optional<Attendance> findFirstByUserIdAndStatusOrderByLoginTimeDesc(Long userId, String string);

}
