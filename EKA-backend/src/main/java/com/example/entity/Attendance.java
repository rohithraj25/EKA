
package com.example.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 
    @ManyToOne
    @JoinColumn(name = "user_id")
     private User user;

    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    
     
     
    private String LoginIpAddress;
    @Column(length = 1000) 
    private String LoginLocation;
    private String LogoutIpAddress;
    @Column(length = 1000) 
    private String LogoutLocation;
    private String status; 
  
    
    public Attendance(User user, String status, String ipAddress, String location ) {
        this.user = user;
        this.status = status;
        this.LoginLocation = LoginIpAddress;
        this.LoginLocation = LoginLocation;
        
    }


 

	public Attendance() {
		// TODO Auto-generated constructor stub
	}




	 

  
}
