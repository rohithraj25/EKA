package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empnumber")
    private Long empnumber;

    @Column(name = "degreememo")
    private String degreememoPath;

    @Column(name = "pancard")
    private String pancardPath;

    @Column(name = "sscmemo")
    private String sscmemoPath;

    @Column(name = "voterid")
    private String voteridPath;

    @Column(name = "imagePath")
    private String imagePath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
