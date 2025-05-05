package com.mosuuuutech.CRUD.API.services.Interface;

import com.mosuuuutech.CRUD.API.entity.Product;
import com.mosuuuutech.CRUD.API.entity.User;

import java.util.List;
import java.util.Optional;


public interface ProductService {
    Product save(Product product);
    List<Product> findAll();
    Product findById(Integer Pid);
    Optional<Product> findByPnum(Integer Pnum);
    void deleteById(Integer Pid);



}

