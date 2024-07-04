package com.example.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Documents;

 
 
public interface DocumentsDao extends JpaRepository<Documents, Long> {
    Optional<Documents> findByEmpnumber(Long empnumber);

}

