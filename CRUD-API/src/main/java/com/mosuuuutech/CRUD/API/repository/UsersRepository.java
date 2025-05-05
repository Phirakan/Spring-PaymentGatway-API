package com.mosuuuutech.CRUD.API.repository;

import com.mosuuuutech.CRUD.API.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository  extends JpaRepository<User, Integer> {

}
