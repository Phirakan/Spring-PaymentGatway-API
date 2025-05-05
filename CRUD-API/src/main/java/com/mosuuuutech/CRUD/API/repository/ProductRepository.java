package com.mosuuuutech.CRUD.API.repository;

import com.mosuuuutech.CRUD.API.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository <Product, Integer> {


    Optional<Product> findByPnum(Integer Pnum);




}


