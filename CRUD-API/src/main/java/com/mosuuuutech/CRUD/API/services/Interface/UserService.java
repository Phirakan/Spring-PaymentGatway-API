package com.mosuuuutech.CRUD.API.services.Interface;

import com.mosuuuutech.CRUD.API.entity.User;

import java.util.List;

public interface UserService {
    User save(User user);
    List<User> findAll();
    User findById(Integer id);
    void deleteById(Integer id);
}
