package com.mosuuuutech.CRUD.API.services;

import com.mosuuuutech.CRUD.API.entity.User;
import com.mosuuuutech.CRUD.API.repository.UsersRepository;
import com.mosuuuutech.CRUD.API.services.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceAction implements UserService {

    private UsersRepository usersRepository;

    @Autowired
    public UserServiceAction(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public User save(User user) {
        return usersRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        Optional <User> result = usersRepository.findById(id);
        User data = null;
        if(result.isPresent()){
           data = result.get();
        }else {
           throw new RuntimeException("ไม่พบผู้ใช้" + id);
        }
            return data;
    }

    @Override
    public void deleteById(Integer id) {
        usersRepository.deleteById(id);
    }

}
