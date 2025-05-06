package com.mosuuuutech.CRUD.API.repository;

import com.mosuuuutech.CRUD.API.entity.status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<status, Integer> {
    Optional<status> findByStatusname(String statusName);
}