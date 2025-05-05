package com.mosuuuutech.CRUD.API.repository;

import com.mosuuuutech.CRUD.API.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Integer> {

    Optional<Auth> findByUsername(String username);
    Boolean existByUsername(String username);
}
