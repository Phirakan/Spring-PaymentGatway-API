package com.mosuuuutech.CRUD.API.controllers;


import com.mosuuuutech.CRUD.API.entity.User;
import com.mosuuuutech.CRUD.API.services.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserControllers {

    private UserService userService;

    @Autowired
    public UserControllers(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add_users")
    public User addUser(@RequestBody User user){
        user.setId(0);
        return userService.save(user);
    }

    @GetMapping("/get_users")
    public List<User> getAllUsers(){
        return userService.findAll();

    }

    @GetMapping("/get_users/{id}")
    public User getUser(@PathVariable int id) {
        return userService.findById(id);
    }

    @DeleteMapping("/delete_users/{id}")
    public String deleteUser(@PathVariable int id){
        User myUser = userService.findById(id);
        if (myUser==null){
            throw new RuntimeException("ไม่พบผู้ใช้" + id);
        }
        userService.deleteById(id);
        return "Delete user id" + id + " successfully";
    }


    @PutMapping("/update_users")
    public User UpdateUser(@RequestBody User user){
        return userService.save(user);
    }
}



